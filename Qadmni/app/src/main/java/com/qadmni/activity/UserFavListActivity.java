package com.qadmni.activity;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.crash.FirebaseCrash;
import com.google.gson.Gson;
import com.qadmni.R;
import com.qadmni.adapters.ItemListAdapter;
import com.qadmni.data.ItemListDetailsDTO;
import com.qadmni.data.ProducerLocationDetailsDTO;
import com.qadmni.data.requestDataDTO.BaseRequestDTO;
import com.qadmni.data.requestDataDTO.CustomerLoginReqDTO;
import com.qadmni.data.responseDataDTO.ItemInfoList;
import com.qadmni.data.responseDataDTO.ProducerLocations;
import com.qadmni.data.responseDataDTO.UserFavResDTO;
import com.qadmni.utils.Constants;
import com.qadmni.utils.NetworkUtils;
import com.qadmni.utils.OneSignalIdHandler;
import com.qadmni.utils.ServerRequestConstants;
import com.qadmni.utils.ServerSyncManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class UserFavListActivity extends BaseActivity implements ServerSyncManager.OnSuccessResultReceived,
        ServerSyncManager.OnErrorResultReceived, LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, ItemListAdapter.CustomButtonListener {
    private static final String TAG = UserFavListActivity.class.getSimpleName();
    private UserFavResDTO userFavResDTO;
    private int EDIT_LOCATION_PERMISSION_CODE = 566;
    private LocationRequest mLocationRequest;
    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private ArrayList<ItemInfoList> itemInfoLists;
    private ArrayList<ProducerLocationDetailsDTO> producerLocationDetailsDTOs;
    private ArrayList<ProducerLocations> producerLocationses;
    private ArrayList<ItemListDetailsDTO> itemListDetailsDTOArrayList = new ArrayList<>();
    private ItemListAdapter itemListAdapter;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_fav_list);
        mServerSyncManager.setOnStringErrorReceived(this);
        mServerSyncManager.setOnStringResultReceived(this);
        mListView = (ListView) findViewById(R.id.item_list);
        itemListAdapter = new ItemListAdapter(this.itemListDetailsDTOArrayList, getApplicationContext(), mSessionManager);
        itemListAdapter.setCustomButtonListner(this);
        mListView.setAdapter(itemListAdapter);
        onRequestGpsPermission();
    }

    @Override
    public void onVolleyErrorReceived(@NonNull VolleyError error, int requestToken) {
        progressDialog.dismiss();
    }

    @Override
    public void onDataErrorReceived(int errorCode, String errorMessage, int requestToken) {
        progressDialog.dismiss();
    }

    @Override
    public void onResultReceived(@NonNull String data, int requestToken) {
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestConstants.REQUEST_USER_FAV:
                userFavResDTO = UserFavResDTO.deserializeJson(data);
                itemInfoLists = userFavResDTO.getItemInfoList();
                producerLocationses = userFavResDTO.getProducerLocations();
                setData();
                break;
        }
    }

    //Get the user location
    /*for request permission*/
    private void onRequestGpsPermission() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    EDIT_LOCATION_PERMISSION_CODE);
        } else {
            buildGoogleApiClient();
        }
    }

    /*activity result fop request permission*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == EDIT_LOCATION_PERMISSION_CODE && grantResults[0] == 0) {
            buildGoogleApiClient();
        } else {
            customAlterDialog(getString(R.string.str_err_location_denied),
                    getString(R.string.user_denied_permission));
        }
    }

    /*This method initailize fused api*/
    synchronized private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        callToConnect();
    }

    private void callToConnect() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        } else
            Log.d("itemListFragment", "Connection with fused api is failed");
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(100); // Update location every second

        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                    mLocationRequest, this);
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        if (mLastLocation != null) {
            callToGetItemList();
        } else {
            final LocationManager manager = (LocationManager) getApplicationContext().
                    getSystemService(Context.LOCATION_SERVICE);
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                buildAlertMessageNoGps();
            } else {
                callToGetItemList();
            }
            //buildAlertMessageNoGps();
        }
    }

    private void callToGetItemList() {
        progressDialog.show();
        BaseRequestDTO baseRequestDTO = new BaseRequestDTO();
        mServerSyncManager.uploadDataToServer(ServerRequestConstants.REQUEST_USER_FAV,
                mSessionManager.getUserFavItem(), baseRequestDTO);
    }

    /*for fused api,to getting location*/
    @Override
    public void onConnectionSuspended(int i) {
        Log.d("TAG", "GPS CONNECTION FAILED");
    }

    /*for fused api,to getting location*/
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("TAG", "GPS CONNECTION FAILED");
    }

    @Override
    public void onLocationChanged(Location location) {
        this.mLastLocation = location;
    }

    private void buildAlertMessageNoGps() {
        Toast.makeText(getApplicationContext(),
                getString(R.string.location_problem), Toast.LENGTH_SHORT).show();
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
                            ItemListDetailsDTO itemListDetailsDTO = new ItemListDetailsDTO(itemInfoList.getItemId(),
                                    itemInfoList.getItemDesc(), itemInfoList.getItemName(),
                                    itemInfoList.getUnitPrice(), itemInfoList.getOfferText(), itemInfoList.getRating(),
                                    itemInfoList.getImageUrl(), itemInfoList.getProducerId(),
                                    producerLocationDetailsDTO.getBusinessName(), producerLocationDetailsDTO.getBusinessLat(),
                                    producerLocationDetailsDTO.getBusinessLong(), producerLocationDetailsDTO.getUserLat(),
                                    producerLocationDetailsDTO.getUserLon(), "",
                                    "", itemInfoList.getReviews());
                            itemListDetailsDTO.setQuantity(quantity);
                            this.itemListDetailsDTOArrayList.add(itemListDetailsDTO);

                        }
                    }
                }
            }
                /*Set Adapter*/
            if (NetworkUtils.isActiveNetworkAvailable(getApplicationContext())) {
                new ApiDirectionsAsyncTask().execute();
            } else {
                Log.d("TAG", "No internet connection");
            }
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


            } else if (mSessionManager.getProducerId() != 0) {
                if (mSessionManager.getProducerId() == itemListDetailsDTOs.getProducerId()) {
                    itemListDetailsDTOs.setQuantity(value + 1);
                    boolean result1 = qadmniHelper.insertOrUpdateCart(itemListDetailsDTOs);
                    int record = qadmniHelper.getCountCartTable();
                    itemListAdapter.notifyDataSetChanged();


                } else {
                    customAlterDialog(getApplicationContext().getResources().getString(R.string.app_name),
                            getApplicationContext().getResources().getString(R.string.str_cart_validation));
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


            } else {
                Toast.makeText(getApplicationContext(), "Quantity Should be greater than 0", Toast.LENGTH_LONG).show();
                mSessionManager.setProducerId(0);
                int record = qadmniHelper.getCountCartTable();
            }

        }
    }

    public class ApiDirectionsAsyncTask extends AsyncTask<Void, Integer, Void> {
        private static final String DIRECTIONS_API_BASE = "https://maps.googleapis.com/maps/api/distancematrix/json?units=metric";
        // API KEY of the project Google Map Api For work
        //   private static final String API_KEY = "AIzaSyBPyqI2_jmK7TOBS0x5uF35x7vSBvP6JX0";
        private final String API_KEY = getApplicationContext().getResources().getString(R.string.str_google_map_key);

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
}
