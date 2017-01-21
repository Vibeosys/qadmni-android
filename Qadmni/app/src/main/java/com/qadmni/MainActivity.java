package com.qadmni;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.gms.location.LocationRequest;
import com.qadmni.activity.BaseActivity;
import com.qadmni.activity.FilterUserListActivity;
import com.qadmni.activity.SelectLanguageActivity;
import com.qadmni.activity.UserMyCartActivity;
import com.qadmni.adapters.CategoryFragmentAdapter;
import com.qadmni.adapters.ItemListAdapter;
import com.qadmni.data.ItemListDetailsDTO;
import com.qadmni.data.requestDataDTO.BaseRequestDTO;
import com.qadmni.data.responseDataDTO.CategoryListResponseDTO;
import com.qadmni.utils.ServerRequestConstants;
import com.qadmni.utils.ServerSyncManager;

import java.util.ArrayList;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, ServerSyncManager.OnSuccessResultReceived,
        ServerSyncManager.OnErrorResultReceived {
    private CategoryFragmentAdapter categoryFragmentAdapter;
    private ViewPager mViewPager;
    private ArrayList<CategoryListResponseDTO> categoryListResponseDTOs;
    private TextView mNavigationUserEmailId, mNavigationUserName;
    private int EDIT_LOCATION_PERMISSION_CODE = 566;
    private LocationRequest mLocationRequest;
    private Location mLastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        String name = mSessionManager.getCustomerName();
        String email = mSessionManager.getUserEmail();
        /*if (name != null || !name.isEmpty()) {
            mNavigationUserEmailId.setText("" + email);
            mNavigationUserName.setText("" + name);
        }*/


        callToGetCategoryWebService();
        mServerSyncManager.setOnStringErrorReceived(this);
        mServerSyncManager.setOnStringResultReceived(this);


    }

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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
            Intent i = new Intent(getApplicationContext(), FilterUserListActivity.class);
            startActivity(i);
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
        } else if (id == R.id.nav_myOrders) {

        } else if (id == R.id.nav_favourite) {

        } else if (id == R.id.nav_profile) {

        } else if (id == R.id.nav_language) {
            Intent intent = new Intent(getApplicationContext(), SelectLanguageActivity.class);
            startActivity(intent);
            finish();

        } else if (id == R.id.nav_partner_login) {

        } else if (id == R.id.nav_logout) {

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


}
