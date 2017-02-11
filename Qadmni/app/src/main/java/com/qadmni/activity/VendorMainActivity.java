package com.qadmni.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.qadmni.R;
import com.qadmni.adapters.VendorItemAdapter;
import com.qadmni.data.requestDataDTO.BaseRequestDTO;
import com.qadmni.data.responseDataDTO.VendorItemResDTO;
import com.qadmni.utils.NetworkUtils;
import com.qadmni.utils.ServerRequestConstants;
import com.qadmni.utils.ServerSyncManager;
import com.qadmni.utils.UserAuth;

import java.util.ArrayList;

public class VendorMainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, ServerSyncManager.OnSuccessResultReceived,
        ServerSyncManager.OnErrorResultReceived, VendorItemAdapter.OnButtonClickListener {
    private RecyclerView reItemList;
    private VendorItemAdapter adapter;
    private TextView mNavigationUserEmailId, mNavigationUserName;
    private LinearLayout addProductTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getResources().getString(R.string.str_vendor_dashboard));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        addProductTitle = (LinearLayout) findViewById(R.id.addProductTitle);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        mServerSyncManager.setOnStringErrorReceived(this);
        mServerSyncManager.setOnStringResultReceived(this);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        mNavigationUserEmailId = (TextView) headerView.findViewById(R.id.txt_user_email);
        mNavigationUserName = (TextView) headerView.findViewById(R.id.txt_user_name);
        String name = mSessionManager.getVendorName();
        String email = mSessionManager.getVendorEmailId();
        if (name != null || !name.isEmpty()) {
            mNavigationUserEmailId.setText("" + email);
            mNavigationUserName.setText("" + name);
        }
        reItemList = (RecyclerView) findViewById(R.id.item_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        reItemList.setLayoutManager(layoutManager);
        if (!NetworkUtils.isActiveNetworkAvailable(getApplicationContext())) {
            createNetworkAlertDialog(getResources().getString(R.string.str_net_err),
                    getResources().getString(R.string.str_err_net_msg));
        } else {
            progressDialog.show();
            BaseRequestDTO baseRequestDTO = new BaseRequestDTO();
            mServerSyncManager.uploadDataToServer(ServerRequestConstants.REQUEST_VENDOR_ITEMS,
                    mSessionManager.getVendorItems(), baseRequestDTO);
        }
        addProductTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AddOrUpdateProductActivity.class));
            }
        });
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
        getMenuInflater().inflate(R.menu.vendor_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       /* if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.language) {
            // Handle the camera action
            Intent intent = new Intent(getApplicationContext(), SelectLanguageActivity.class);
            startActivity(intent);
        }
        if (id == R.id.nav_myOrders) {
            Intent intent = new Intent(getApplicationContext(), VendorOrderListActivity.class);
            startActivity(intent);
        }
        if (id == R.id.nav_logout) {
            callToLogOut();
        }
        if (id == R.id.nav_profile) {
            Intent intent = new Intent(getApplicationContext(), VendorProfileActivity.class);
            startActivity(intent);
        }
        /*if (id == R.id.nav_subscription) {
            *//*Intent intent = new Intent(getApplicationContext(), PatnerRenewActivity.class);
            startActivity(intent);*//*
        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void callToLogOut() {
        UserAuth.CleanVendorAuthenticationInfo();
        Intent intent = new Intent(getApplicationContext(), MainLoginActivity.class);
        startActivity(intent);
        finish();
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
            case ServerRequestConstants.REQUEST_VENDOR_ITEMS:
                ArrayList<VendorItemResDTO> vendorItemResDTOs = VendorItemResDTO.deSerializeToArray(data);
                adapter = new VendorItemAdapter(getApplicationContext(), vendorItemResDTOs);
                adapter.setOnButtonClickListener(this);
                reItemList.setAdapter(adapter);
                break;
        }
    }

    @Override
    public void onButtonClick(VendorItemResDTO vendorItemResDTO, int position) {
        Intent intent = new Intent(getApplicationContext(), AddOrUpdateProductActivity.class);
        Bundle bundle = new Bundle();
        bundle.putLong("productId", vendorItemResDTO.getItemId());
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
