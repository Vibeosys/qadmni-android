package com.qadmni.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.qadmni.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class PlaceOrderActivity extends BaseActivity implements
        OnMapReadyCallback, View.OnClickListener {
    public static final int LOCATION_RESULT = 1;
    private TextView txtAddL1, txtAddL2;
    private LinearLayout btnSearchAdd;
    private RadioGroup radGrpDeliveryType;
    private MapView mMapView;
    private GoogleMap mGoogleMap;
    private LatLng selectedLatLng;
    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);
        mMapView = (MapView) findViewById(R.id.mapView);
        txtAddL1 = (TextView) findViewById(R.id.txt_address_line1);
        txtAddL2 = (TextView) findViewById(R.id.txt_address_line2);
        radGrpDeliveryType = (RadioGroup) findViewById(R.id.radio_grp_delivery_type);
        btnSearchAdd = (LinearLayout) findViewById(R.id.btn_search_address);
        btnSearchAdd.setOnClickListener(this);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        try {
            MapsInitializer.initialize(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMapView.getMapAsync(this);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_search_address:
                Intent iRegister = new Intent(getApplicationContext(), SearchAddressActivity.class);
                startActivityForResult(iRegister, LOCATION_RESULT);
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        // For showing a move to my location button
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.setOnMapClickListener(new MapClick());
    }

    private class MapClick implements GoogleMap.OnMapClickListener {

        @Override
        public void onMapClick(LatLng latLng) {
            selectedLatLng = latLng;
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();
                txtAddL1.setText(knownName);
                txtAddL2.setText(address);
                mGoogleMap.addMarker(new MarkerOptions().position(latLng).title(knownName + "," + address));
                CameraUpdate center = CameraUpdateFactory.newLatLng(latLng);
                mGoogleMap.moveCamera(center);
                CameraUpdate zoom = CameraUpdateFactory.zoomTo(12);
                mGoogleMap.animateCamera(zoom);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOCATION_RESULT) {
            if (resultCode == Activity.RESULT_OK) {
                selectedLatLng = data.getExtras().getParcelable(SearchAddressActivity.SELECTED_PLACE_LAT_LNG);
                address = data.getStringExtra(SearchAddressActivity.SELECTED_PLACE_ADDRESS);
                String name = data.getStringExtra(SearchAddressActivity.SELECTED_PLACE_NAME);
                txtAddL1.setText(name);
                txtAddL2.setText(address);
                mGoogleMap.addMarker(new MarkerOptions().position(selectedLatLng).title(name + "," + address));
                CameraUpdate center = CameraUpdateFactory.newLatLng(selectedLatLng);
                mGoogleMap.moveCamera(center);
                CameraUpdate zoom = CameraUpdateFactory.zoomTo(12);
                mGoogleMap.animateCamera(zoom);
            } else {
                selectedLatLng = null;
                address = "";
            }

        }
    }
}
