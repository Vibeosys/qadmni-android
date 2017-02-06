package com.qadmni.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.qadmni.R;

public class DisplayUserAddressMap extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private double userDeliveryLat, userDeliveryLang;
    private String userAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_user_address_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            userAddress = bundle.getString("userAddress");
            userDeliveryLat = bundle.getDouble("userLat");
            userDeliveryLang = bundle.getDouble("userLan");
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (userAddress != null) {
            if (userDeliveryLat != 0.0 && userDeliveryLang != 0.0) {
                LatLng userLatLong = new LatLng(userDeliveryLat, userDeliveryLang);
                CameraPosition cameraPosition = new CameraPosition.Builder().target(userLatLong).zoom(12).build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                mMap.addMarker(new MarkerOptions().position(userLatLong).title("" + userAddress));
            }
        }
    }
}
