package com.qadmni.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.Location;

import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerTabStrip;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.crash.FirebaseCrash;
import com.google.gson.Gson;
import com.qadmni.MainActivity;
import com.qadmni.R;
import com.qadmni.adapters.CategoryFragmentAdapter;
import com.qadmni.adapters.ItemListAdapter;
import com.qadmni.data.CategoryMasterDTO;
import com.qadmni.data.ItemListDetailsDTO;
import com.qadmni.data.ProducerLocationDetailsDTO;
import com.qadmni.data.requestDataDTO.AddFavReqDTO;
import com.qadmni.data.requestDataDTO.BaseRequestDTO;
import com.qadmni.data.requestDataDTO.GetItemListDTO;
import com.qadmni.data.responseDataDTO.CategoryListResponseDTO;
import com.qadmni.data.responseDataDTO.ItemInfoList;
import com.qadmni.data.responseDataDTO.ItemListResponseDTO;
import com.qadmni.data.responseDataDTO.ProducerLocations;
import com.qadmni.utils.Constants;
import com.qadmni.utils.NetworkUtils;
import com.qadmni.utils.ServerRequestConstants;
import com.qadmni.utils.ServerSyncManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by shrinivas on 13-01-2017.
 */
public class ItemListFragment extends BaseFragment implements ServerSyncManager.OnSuccessResultReceived,
        ServerSyncManager.OnErrorResultReceived, ItemListAdapter.CustomButtonListener,
        MainActivity.OnFilterApply {
    public static final String ARG_OBJECT = "objectPar";
    private static final String TAG = ItemListFragment.class.getSimpleName();
    private CategoryMasterDTO categoryMasterDTO;
    private ListView mListView;
    private ArrayList<ProducerLocations> producerLocationses;
    private ArrayList<ItemInfoList> itemInfoLists;
    private ArrayList<ProducerLocationDetailsDTO> producerLocationDetailsDTOs;
    private ArrayList<ItemListDetailsDTO> itemListDetailsDTOArrayList = new ArrayList<>();
    private ItemListAdapter itemListAdapter;
    private MainActivity activity;
    private Location mLastLocation;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        activity.setOnFilterApply(this);
        //activity.setOnSearchClickListener(this);
        mLastLocation = activity.getLastLocation();
        Bundle args = getArguments();
        if (args != null) {
            if (!isGooglePlayServicesAvailable()) {
                getActivity().finish();
            }
            categoryMasterDTO = args.getParcelable(ARG_OBJECT);
        }

        Log.d(TAG, "## fragment created" + categoryMasterDTO.getCategory());
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated
        // properly.
        View rootView = inflater.inflate(R.layout.fragment_item_list_collection, container, false);
        mListView = (ListView) rootView.findViewById(R.id.item_list);
        itemListAdapter = new ItemListAdapter(this.itemListDetailsDTOArrayList, getContext(), mSessionManager);
        itemListAdapter.setCustomButtonListner(this);
        mListView.setAdapter(itemListAdapter);
        Log.d(TAG, "## fragment created View" + categoryMasterDTO.getCategory());
        mServerSyncManager.setOnStringErrorReceived(this);
        mServerSyncManager.setOnStringResultReceived(this);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        callToWebService();
        Log.d(TAG, "## fragment resume" + categoryMasterDTO.getCategory());
    }

    private void callToWebService() {
        String categoryIdStr = "" + categoryMasterDTO.getCategoryId();
        GetItemListDTO getItemListDTO = new GetItemListDTO(categoryIdStr);
        Gson gson = new Gson();
        String serializedJsonString = gson.toJson(getItemListDTO);
        BaseRequestDTO baseRequestDTO = new BaseRequestDTO();
        baseRequestDTO.setData(serializedJsonString);
        mServerSyncManager.uploadDataToServer(ServerRequestConstants.REQUEST_GET_ITEM_LIST,
                mSessionManager.getItemListUrl(), baseRequestDTO);

    }


    @Override
    public void onVolleyErrorReceived(@NonNull VolleyError error, int requestToken) {
        // progressDialog.dismiss();
        // customAlterDialog(getResources().getString(R.string.str_err_server_err), error.getMessage());
    }

    @Override
    public void onDataErrorReceived(int errorCode, String errorMessage, int requestToken) {
        //  progressDialog.dismiss();
        //   customAlterDialog(getResources().getString(R.string.str_err_server_err), errorMessage);
    }

    @Override
    public void onResultReceived(@NonNull String data, int requestToken) {
        //   progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestConstants.REQUEST_GET_ITEM_LIST:
                ItemListResponseDTO itemListResponseDTO = ItemListResponseDTO.deserializeJson(data);
                itemInfoLists = ItemInfoList.deSerializedToJson(itemListResponseDTO.getItemInfoLists());
                producerLocationses = ProducerLocations.deSerializedToJson(itemListResponseDTO.getProducerLocations());
                setData();
                break;
            case ServerRequestConstants.REQUEST_ADD_REMOVE_FAV:
                break;
        }

    }


    public void setData() {
        Location location = new Location("");
        try {
            location.setLatitude(mLastLocation.getLatitude());
            location.setLongitude(mLastLocation.getLongitude());
        } catch (NullPointerException e) {
            FirebaseCrash.log(TAG + " error in current location" + e.getMessage());
        }

        if (producerLocationses != null) {
            producerLocationDetailsDTOs = new ArrayList<>();
            for (int i = 0; i < producerLocationses.size(); i++) {
                ProducerLocations producerLocations = producerLocationses.get(i);

                Location productLocation = new Location("");
                productLocation.setLatitude(producerLocations.getBusinessLat());
                productLocation.setLongitude(producerLocations.getBusinessLong());

                ProducerLocationDetailsDTO producerLocationDetailsDTO = new ProducerLocationDetailsDTO(producerLocations.getProducerId(),
                        producerLocations.getBusinessName(), producerLocations.getBusinessLat(), producerLocations.getBusinessLong(),
                        mLastLocation.getLatitude(), mLastLocation.getLongitude());
                producerLocationDetailsDTOs.add(producerLocationDetailsDTO);
            }
            callToPrepareArrayList(producerLocationDetailsDTOs);
        }

    }


    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, getActivity(), 0).show();
            return false;
        }
    }


    public void callToPrepareArrayList(ArrayList<ProducerLocationDetailsDTO> producerLocationDetailsDTOs) {
        if (producerLocationDetailsDTOs != null) {
            //producerLocationDetailsDTOs.size();
            for (int i = 0; i < producerLocationDetailsDTOs.size(); i++) {
                ProducerLocationDetailsDTO producerLocationDetailsDTO = producerLocationDetailsDTOs.get(i);
                if (itemInfoLists != null) {
                    this.itemListDetailsDTOArrayList.clear();
                    for (int j = 0; j < itemInfoLists.size(); j++) {
                        ItemInfoList itemInfoList = itemInfoLists.get(j);
                        if (producerLocationDetailsDTO.getProducerId() == itemInfoList.getProducerId()) {
                            int quantity = qadmniHelper.getItemQuantity(itemInfoList.getItemId());
                            boolean isMyFav = qadmniHelper.getMyFavItem(itemInfoList.getItemId());
                            ItemListDetailsDTO itemListDetailsDTO = new ItemListDetailsDTO(itemInfoList.getItemId(),
                                    itemInfoList.getItemDesc(), itemInfoList.getItemName(),
                                    itemInfoList.getUnitPrice(), itemInfoList.getOfferText(), itemInfoList.getRating(),
                                    itemInfoList.getImageUrl(), itemInfoList.getProducerId(),
                                    producerLocationDetailsDTO.getBusinessName(), producerLocationDetailsDTO.getBusinessLat(),
                                    producerLocationDetailsDTO.getBusinessLong(), producerLocationDetailsDTO.getUserLat(),
                                    producerLocationDetailsDTO.getUserLon(), "",
                                    "", itemInfoList.getReviews());
                            itemListDetailsDTO.setQuantity(quantity);
                            itemListDetailsDTO.setMyFav(isMyFav);
                            this.itemListDetailsDTOArrayList.add(itemListDetailsDTO);

                        }
                    }
                }
            }
                /*Set Adapter*/
            if (NetworkUtils.isActiveNetworkAvailable(getContext())) {
                new ApiDirectionsAsyncTask().execute();
            } else {
                Log.d("TAG", "No internet connection");
            }
        }
    }

    @Override
    public void applyFilterClick() {
        //itemListAdapter.sortNFilter();
        //Log.d(TAG, "On filter click" + itemListAdapter.getItem(1));
    }

    // @Override
    public void onSearchClickListener(String query) {
        Log.d(TAG, "##category Name" + categoryMasterDTO.getCategory());
        if (this.itemListAdapter != null) {
            if (!TextUtils.isEmpty(query)) {
                ArrayList<ItemListDetailsDTO> searchItems = new ArrayList<>();
                for (ItemListDetailsDTO itemDetails : this.itemListDetailsDTOArrayList) {
                    if (itemDetails.getItemName().toLowerCase().contains(query.toLowerCase())) {
                        searchItems.add(itemDetails);
                    } else if (itemDetails.getItemDesc().toLowerCase().contains(query.toLowerCase())) {
                        searchItems.add(itemDetails);
                    } else if (itemDetails.getBusinessName().toLowerCase().contains(query.toLowerCase())) {
                        searchItems.add(itemDetails);
                    }
                }
                itemListAdapter.setItemListDetailsDTOs(searchItems);
            } else if (query.equals("")) {
                this.itemListDetailsDTOArrayList.clear();
                callToWebService();
            }
        }
    }

    public class ApiDirectionsAsyncTask extends AsyncTask<Void, Integer, Void> {
        private static final String DIRECTIONS_API_BASE = "https://maps.googleapis.com/maps/api/distancematrix/json?units=metric";
        // API KEY of the project Google Map Api For work
        //   private static final String API_KEY = "AIzaSyBPyqI2_jmK7TOBS0x5uF35x7vSBvP6JX0";
        private final String API_KEY = getContext().getResources().getString(R.string.str_google_map_key);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            for (int i = 0; i < itemListDetailsDTOArrayList.size(); i++) {
                Log.i("TAG", "doInBackground of ApiDirectionsAsyncTask");
                ItemListDetailsDTO itemListDetailsDTO = itemListDetailsDTOArrayList.get(i);
                HttpURLConnection mUrlConnection = null;
                StringBuilder mJsonResults = new StringBuilder();
                double dist = 0.0;
                double time = 0.0;
                float fDistance;
                float fTime;
                try {
                    StringBuilder sb = new StringBuilder(DIRECTIONS_API_BASE);
                    sb.append("&origins=" + URLEncoder.encode(itemListDetailsDTO.getUserLat() + "," + itemListDetailsDTO.getUserLon(), "utf8"));
                    sb.append("&destinations=" + URLEncoder.encode(itemListDetailsDTO.getBusinessLat() + "," + itemListDetailsDTO.getBusinessLong(), "utf8"));
                    sb.append("&key=" + API_KEY);

                    URL url = new URL(sb.toString());
                    mUrlConnection = (HttpURLConnection) url.openConnection();
                    InputStreamReader in = new InputStreamReader(mUrlConnection.getInputStream());

                    // Load the results into a StringBuilder
                    int read;
                    char[] buff = new char[1024];
                    while ((read = in.read(buff)) != -1) {
                        mJsonResults.append(buff, 0, read);
                    }

                    JSONObject jsonObject = new JSONObject();
                    try {

                        try {
                            jsonObject = new JSONObject(mJsonResults.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        JSONArray array = jsonObject.getJSONArray("rows");

                        JSONObject routes = array.getJSONObject(0);

                        JSONArray elements = routes.getJSONArray("elements");

                        JSONObject steps = elements.getJSONObject(0);

                        JSONObject duration = steps.getJSONObject("duration");
                        String strDuration = duration.getString("text");

                        JSONObject distance = steps.getJSONObject("distance");
                        String strTime = distance.getString("text");
                        double doubleDistance = distance.getDouble("value");

                        itemListDetailsDTO.setUserTime(strDuration);
                        itemListDetailsDTO.setUserDistance(strTime);
                        itemListDetailsDTO.setDoubleDistance(doubleDistance);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } catch (MalformedURLException e) {
                    Log.e("TAG", "Error processing Distance Matrix API URL");
                    //return null;

                } catch (IOException e) {
                    System.out.println("Error connecting to Distance Matrix");
                    // return null;
                } finally {
                    if (mUrlConnection != null) {
                        mUrlConnection.disconnect();
                    }
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            itemListAdapter.setItemListDetailsDTOs(itemListDetailsDTOArrayList);
        }
    }

    @Override
    public void onButtonClickListener(int id, int position, int value, ItemListDetailsDTO itemListDetailsDTOs) {
        if (id == R.id.plus_product) {
            long test = mSessionManager.getProducerId();
            if (mSessionManager.getProducerId() == 0) {
                mSessionManager.setProducerId(itemListDetailsDTOs.getProducerId());
                itemListDetailsDTOs.setQuantity(value + 1);
                boolean result = qadmniHelper.insertOrUpdateCart(itemListDetailsDTOs);
                int record = qadmniHelper.getCountCartTable();
                itemListAdapter.notifyDataSetChanged();
                Intent mSendIntent = new Intent(Constants.SEND_BROADCAST_SIGNAL);
                mSendIntent.setAction(Constants.SEND_BROADCAST_SIGNAL);
                mSendIntent.putExtra(Constants.ENDED_DATA_SIGNAL, record);
                getActivity().sendBroadcast(mSendIntent);

            } else if (mSessionManager.getProducerId() != 0) {
                if (mSessionManager.getProducerId() == itemListDetailsDTOs.getProducerId()) {
                    itemListDetailsDTOs.setQuantity(value + 1);
                    boolean result1 = qadmniHelper.insertOrUpdateCart(itemListDetailsDTOs);
                    int record = qadmniHelper.getCountCartTable();
                    itemListAdapter.notifyDataSetChanged();
                    Intent mSendIntent = new Intent(Constants.SEND_BROADCAST_SIGNAL);
                    mSendIntent.setAction(Constants.SEND_BROADCAST_SIGNAL);
                    mSendIntent.putExtra(Constants.ENDED_DATA_SIGNAL, record);
                    getActivity().sendBroadcast(mSendIntent);

                } else {
                    customAlterDialog(getContext().getResources().getString(R.string.app_name), getContext().getResources().getString(R.string.str_cart_validation));
                    //Toast.makeText(getActivity(), "You cannot add from another vendor", Toast.LENGTH_LONG).show();
                }

            }


        }
        if (id == R.id.minus_product) {
            if (value > 0) {
                itemListDetailsDTOs.setQuantity(value - 1);
                qadmniHelper.insertOrUpdateCart(itemListDetailsDTOs);
                boolean result1 = qadmniHelper.insertOrUpdateCart(itemListDetailsDTOs);
                int record = qadmniHelper.getCountCartTable();
                itemListAdapter.notifyDataSetChanged();
                Intent mSendIntent = new Intent(Constants.SEND_BROADCAST_SIGNAL);
                mSendIntent.setAction(Constants.SEND_BROADCAST_SIGNAL);
                mSendIntent.putExtra(Constants.ENDED_DATA_SIGNAL, record);
                getActivity().sendBroadcast(mSendIntent);

            } else {
                Toast.makeText(getActivity(), "Quantity Should be greater than 0", Toast.LENGTH_LONG).show();
                mSessionManager.setProducerId(0);
                int record = qadmniHelper.getCountCartTable();
                Intent mSendIntent = new Intent(Constants.SEND_BROADCAST_SIGNAL);
                mSendIntent.setAction(Constants.SEND_BROADCAST_SIGNAL);
                mSendIntent.putExtra(Constants.ENDED_DATA_SIGNAL, record);
                getActivity().sendBroadcast(mSendIntent);
            }

        }

        if (id == R.id.ic_favourite) {
            boolean isFav = itemListDetailsDTOs.isMyFav();
            itemListDetailsDTOs.setMyFav(!isFav);
            itemListAdapter.notifyDataSetChanged();
            callToaddRemoveFav(itemListDetailsDTOs.getItemId(), !isFav);
        }
    }

    private void callToaddRemoveFav(long itemId, boolean isFav) {
        if (isFav) {
            qadmniHelper.insertFav(itemId);
        } else {
            qadmniHelper.deleteMyFav(itemId);
        }
        AddFavReqDTO addFavReqDTO = new AddFavReqDTO(itemId, isFav ? 1 : 0);
        Gson gson = new Gson();
        String serializedJsonString = gson.toJson(addFavReqDTO);
        BaseRequestDTO baseRequestDTO = new BaseRequestDTO();
        baseRequestDTO.setData(serializedJsonString);
        mServerSyncManager.uploadDataToServer(ServerRequestConstants.REQUEST_ADD_REMOVE_FAV,
                mSessionManager.addOrRemoveFav(), baseRequestDTO);
    }

    public CategoryMasterDTO getCategoryMasterDTO() {
        return this.categoryMasterDTO;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        progressDialog.dismiss();
    }
}
