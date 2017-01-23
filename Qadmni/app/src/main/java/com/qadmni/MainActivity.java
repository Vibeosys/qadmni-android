package com.qadmni;

import android.*;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.LayerDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.qadmni.activity.BaseActivity;
import com.qadmni.activity.CustomerLoginActivity;
import com.qadmni.activity.FilterUserListActivity;
import com.qadmni.activity.MainLoginActivity;
import com.qadmni.activity.SelectLanguageActivity;
import com.qadmni.activity.UserMyCartActivity;
import com.qadmni.activity.UserOrderHistoryActivity;
import com.qadmni.activity.VendorLoginActivity;
import com.qadmni.adapters.CategoryFragmentAdapter;
import com.qadmni.adapters.ItemListAdapter;
import com.qadmni.data.ItemListDetailsDTO;
import com.qadmni.data.requestDataDTO.BaseRequestDTO;
import com.qadmni.data.responseDataDTO.CategoryListResponseDTO;
import com.qadmni.utils.Constants;
import com.qadmni.utils.ServerRequestConstants;
import com.qadmni.utils.ServerSyncManager;
import com.qadmni.utils.UserAuth;
import com.qadmni.utils.UserType;
import com.qadmni.utils.Utils2;

import java.util.ArrayList;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, ServerSyncManager.OnSuccessResultReceived,
        ServerSyncManager.OnErrorResultReceived, LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final int FILTER_LIST = 101;
    private CategoryFragmentAdapter categoryFragmentAdapter;
    private ViewPager mViewPager;
    private ArrayList<CategoryListResponseDTO> categoryListResponseDTOs;
    private TextView mNavigationUserEmailId, mNavigationUserName;
    private int EDIT_LOCATION_PERMISSION_CODE = 566;
    private LocationRequest mLocationRequest;
    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private OnFilterApply onFilterApply;
    public static Utils2 notificationUtil;
    BroadcastReceiver broadcastReceiver;
    IntentFilter filter;
    public static SearchClickListener searchClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        filter = new IntentFilter();
        filter.addAction(Constants.SEND_BROADCAST_SIGNAL);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        mNavigationUserEmailId = (TextView) headerView.findViewById(R.id.txt_user_email);
        mNavigationUserName = (TextView) headerView.findViewById(R.id.txt_user_name);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        int userType = mSessionManager.getUserType();
        if (userType == UserType.USER_OTHER) {
            mNavigationUserEmailId.setText("info@qadmni.com");
            mNavigationUserName.setText(getResources().getString(R.string.app_name));

        } else if (userType == UserType.USER_CUSTOMER) {
            String name = mSessionManager.getCustomerName();
            String email = mSessionManager.getUserEmail();
            if (name != null || !name.isEmpty()) {
                mNavigationUserEmailId.setText("" + email);
                mNavigationUserName.setText("" + name);
            }
        }

        onRequestGpsPermission();
        mServerSyncManager.setOnStringErrorReceived(this);
        mServerSyncManager.setOnStringResultReceived(this);
        /*int record = qadmniHelper.getCountCartTable();
        if (record != 0) {
            updateUI(record);
        }*/

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    int result = bundle.getInt(Constants.ENDED_DATA_SIGNAL);
                    updateUI(result);

                }
            }
        };
    }

    public void updateUI(final int result) {

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {

                notificationUtil.setBadgeCount(result);
            }
        });
    }

    /*public void updateUIToMainActivity(final int result) {

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                notificationUtil.setBadgeCount(0);
                notificationUtil.setBadgeCount(result);
            }
        });
    }
*/

    private void callToGetCategoryWebService() {
        BaseRequestDTO baseRequestDTO = new BaseRequestDTO();
        mServerSyncManager.uploadDataToServer(ServerRequestConstants.REQUEST_GET_CATEGORY,
                mSessionManager.getCategoryListUrl(), baseRequestDTO);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        MenuItem item = menu.findItem(R.id.shoppingcart);
        LayerDrawable icon = (LayerDrawable) item.getIcon();
        notificationUtil = new Utils2(this, icon);
        notificationUtil.setBadgeCount(0);

        SearchView searchView = (SearchView) menu.findItem(R.id.detail_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if (searchClickListener != null)
                    searchClickListener.OnSearchClickListener(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //searchText = newText;
                return true;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                if (searchClickListener != null)
                    searchClickListener.OnSearchClickListener("");
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        //noinspection SimplifiableIfStatement
        if (id == R.id.shoppingcart) {
            Intent i = new Intent(getApplicationContext(), UserMyCartActivity.class);
            startActivity(i);
        }
        if (id == R.id.filter) {
            Intent iFilter = new Intent(getApplicationContext(), FilterUserListActivity.class);
            startActivityForResult(iFilter, FILTER_LIST);
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_myCart) {
            // Handle the camera action
            Intent intent = new Intent(getApplicationContext(), UserMyCartActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_myOrders) {
            Intent intent = new Intent(getApplicationContext(), UserOrderHistoryActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_favourite) {

        } else if (id == R.id.nav_profile) {

        } else if (id == R.id.nav_language) {
            Intent intent = new Intent(getApplicationContext(), SelectLanguageActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_partner_login) {
            UserAuth.CleanCustomerAuthenticationInfo();
            Intent intent = new Intent(getApplicationContext(), MainLoginActivity.class);
            startActivity(intent);
            finish();

        } else if (id == R.id.nav_logout) {
            UserAuth.CleanCustomerAuthenticationInfo();
            Intent intent = new Intent(getApplicationContext(), MainLoginActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onVolleyErrorReceived(@NonNull VolleyError error, int requestToken) {
        progressDialog.dismiss();
        customAlterDialog(getResources().getString(R.string.str_err_server_err), error.getMessage());

    }

    @Override
    public void onDataErrorReceived(int errorCode, String errorMessage, int requestToken) {
        progressDialog.dismiss();
        customAlterDialog(getResources().getString(R.string.str_err_server_err), errorMessage);
    }

    @Override
    public void onResultReceived(@NonNull String data, int requestToken) {
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestConstants.REQUEST_GET_CATEGORY:
                ArrayList<CategoryListResponseDTO> categoryListResponseDTOs = CategoryListResponseDTO.deSerializedToJson(data);
                categoryFragmentAdapter = new CategoryFragmentAdapter(
                        getSupportFragmentManager(), categoryListResponseDTOs);
                mViewPager.setOffscreenPageLimit(1);
                mViewPager.setAdapter(categoryFragmentAdapter);
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
            callToGetCategoryWebService();
        } else {
            final LocationManager manager = (LocationManager) getApplicationContext().
                    getSystemService(Context.LOCATION_SERVICE);
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                buildAlertMessageNoGps();
            } else {
                callToGetCategoryWebService();
            }
            //buildAlertMessageNoGps();
        }
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

    public Location getLastLocation() {
        return this.mLastLocation;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILTER_LIST) {
            if (resultCode == Activity.RESULT_OK) {
                int sortBy = data.getExtras().getInt(FilterUserListActivity.SORT_BY);
                int selectedDistance = data.getExtras().getInt(FilterUserListActivity.SELECTED_DISTANCE);
                int selectedPrice = data.getExtras().getInt(FilterUserListActivity.SELECTED_PRICE);
                mSessionManager.setListSortBy(sortBy);
                mSessionManager.setSelectedDistance(selectedDistance);
                mSessionManager.setSelectedPrice(selectedPrice);
                //onFilterApply.applyFilterClick();
            }
        }
    }

    public interface OnFilterApply {
        void applyFilterClick();
    }

    public void setOnFilterApply(OnFilterApply onFilterApply) {
        this.onFilterApply = onFilterApply;
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, filter);
        int record = qadmniHelper.getCountCartTable();
        if (record != 0) {
            //updateUIToMainActivity(record);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    public static void setOnSearchClickListener(SearchClickListener listener) {
        searchClickListener = listener;
    }

    public interface SearchClickListener {
        public void OnSearchClickListener(String query);
    }
}
