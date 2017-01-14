package com.qadmni.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.qadmni.R;

public class SearchAddressActivity extends BaseActivity implements
        OnMapReadyCallback, View.OnClickListener {
    public static final String SELECTED_PLACE_LAT_LNG = "selectedPlaceLatLng";
    public static final String SELECTED_PLACE_NAME = "selectedPlaceName";
    public static final String SELECTED_PLACE_ADDRESS = "selectedAddress";
    private MapView mMapView;
    private GoogleMap mGoogleMap;
    PlaceAutocompleteFragment autocompleteFragment;
    private Button btnSelect;
    private Place selectedPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_address);
        mMapView = (MapView) findViewById(R.id.mapView);
        btnSelect = (Button) findViewById(R.id.btn_select);
        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        try {
            MapsInitializer.initialize(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        btnSelect.setOnClickListener(this);
        mMapView.getMapAsync(this);
        autocompleteFragment.setOnPlaceSelectedListener(new MyPlaceSelected());
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
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_select:
                if (selectedPlace != null) {
                    Intent intent = new Intent();
                    intent.putExtra(SELECTED_PLACE_LAT_LNG, selectedPlace.getLatLng());
                    intent.putExtra(SELECTED_PLACE_NAME, selectedPlace.getName());
                    intent.putExtra(SELECTED_PLACE_ADDRESS, selectedPlace.getAddress());
                    setResult(Activity.RESULT_OK, intent);
                    finish();//finishing activity
                }
                break;
        }
    }

    private class MyPlaceSelected implements PlaceSelectionListener {
        @Override
        public void onPlaceSelected(Place place) {
           /* double latitude = place.getLatLng().latitude;
            double longitude = place.getLatLng().longitude;*/
            LatLng latLng = place.getLatLng();
            mGoogleMap.addMarker(new MarkerOptions().position(latLng).title(place.getName() + "," + place.getAddress()));
            CameraUpdate center = CameraUpdateFactory.newLatLng(latLng);
            mGoogleMap.moveCamera(center);
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(12);
            mGoogleMap.animateCamera(zoom);
            selectedPlace = place;
        }

        @Override
        public void onError(Status status) {
            Toast.makeText(getApplicationContext(), getString(R.string.str_location_not_found),
                    Toast.LENGTH_SHORT).show();
            selectedPlace = null;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(Activity.RESULT_CANCELED, intent);
        finish();//finishing activity
        //super.onBackPressed();

    }
}
