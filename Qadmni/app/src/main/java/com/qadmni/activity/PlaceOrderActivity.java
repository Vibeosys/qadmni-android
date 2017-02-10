package com.qadmni.activity;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.qadmni.R;
import com.qadmni.data.requestDataDTO.BaseRequestDTO;
import com.qadmni.data.requestDataDTO.InitOrderReqDTO;
import com.qadmni.data.requestDataDTO.OrderItemDTO;
import com.qadmni.data.responseDataDTO.InitOrderResDTO;
import com.qadmni.utils.DateUtils;
import com.qadmni.utils.DeliveryMethods;
import com.qadmni.utils.NetworkUtils;
import com.qadmni.utils.PaymentMethods;
import com.qadmni.utils.ServerRequestConstants;
import com.qadmni.utils.ServerSyncManager;

import org.androidannotations.annotations.TextChange;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PlaceOrderActivity extends BaseActivity implements
        OnMapReadyCallback, View.OnClickListener, ServerSyncManager.OnErrorResultReceived,
        ServerSyncManager.OnSuccessResultReceived, CompoundButton.OnCheckedChangeListener {
    public static final int LOCATION_RESULT = 1;
    private int MAX_GIFT_CHARACHTERS = 180;
    private static final String TAG = PlaceOrderActivity.class.getSimpleName();
    private TextView txtAddL1, txtAddL2, txtScheduleDate, txtRemainCharacters;
    private LinearLayout btnSearchAdd, btnPlaceOrder;
    private RadioGroup radGrpDeliveryType;
    private MapView mMapView;
    private GoogleMap mGoogleMap;
    private LatLng selectedLatLng;
    private String address;
    private CheckBox chkAsapDelivery, chkScheduleDelivery, chkPayPal, chkCash;
    private String deliveryMethod = "", paymentMethod = "";
    private Calendar mFilterCalender = Calendar.getInstance();
    DateUtils dateUtils = new DateUtils();
    long deliverySchedule;
    private EditText edtGiftMsg;
    private CheckBox chkGift;
    LinearLayout layGift;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);
        setTitle(getResources().getString(R.string.place_order));
        mFilterCalender.set(Calendar.DAY_OF_MONTH, mFilterCalender.get(Calendar.DAY_OF_MONTH));
        mMapView = (MapView) findViewById(R.id.mapView);
        txtAddL1 = (TextView) findViewById(R.id.txt_address_line1);
        txtAddL2 = (TextView) findViewById(R.id.txt_address_line2);
        txtRemainCharacters = (TextView) findViewById(R.id.txt_remain_characters);
        edtGiftMsg = (EditText) findViewById(R.id.edt_message);
        radGrpDeliveryType = (RadioGroup) findViewById(R.id.radio_grp_delivery_type);
        btnSearchAdd = (LinearLayout) findViewById(R.id.btn_search_address);
        btnPlaceOrder = (LinearLayout) findViewById(R.id.btn_place_order);
        chkAsapDelivery = (CheckBox) findViewById(R.id.chk_asap_delivery);
        chkScheduleDelivery = (CheckBox) findViewById(R.id.chk_schedule_delivery);
        chkPayPal = (CheckBox) findViewById(R.id.chk_pay_pal);
        chkCash = (CheckBox) findViewById(R.id.chk_cash);
        chkGift = (CheckBox) findViewById(R.id.chk_gift_Wrap_delivery);
        txtScheduleDate = (TextView) findViewById(R.id.txt_pick_date);
        layGift = (LinearLayout) findViewById(R.id.isGiftMsg);

        txtRemainCharacters.setText(MAX_GIFT_CHARACHTERS + " " + getString(R.string.str_180_charachters));
        btnSearchAdd.setOnClickListener(this);
        btnPlaceOrder.setOnClickListener(this);
        txtScheduleDate.setOnClickListener(this);

        chkAsapDelivery.setOnCheckedChangeListener(this);
        chkScheduleDelivery.setOnCheckedChangeListener(this);
        chkPayPal.setOnCheckedChangeListener(this);
        chkCash.setOnCheckedChangeListener(this);
        chkGift.setOnCheckedChangeListener(this);

        mMapView.onCreate(savedInstanceState);
        mServerSyncManager.setOnStringErrorReceived(this);
        mServerSyncManager.setOnStringResultReceived(this);
        mMapView.onResume();
        try {
            MapsInitializer.initialize(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMapView.getMapAsync(this);
        edtGiftMsg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int remainChar = MAX_GIFT_CHARACHTERS - charSequence.length();
                txtRemainCharacters.setText(remainChar + " " + getString(R.string.str_180_charachters));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_search_address:
                Intent iRegister = new Intent(getApplicationContext(), SearchAddressActivity.class);
                startActivityForResult(iRegister, LOCATION_RESULT);
                break;
            case R.id.btn_place_order:
                placeOrder();
                break;
            case R.id.txt_pick_date:
                showDate();
                break;
        }
    }

    private void placeOrder() {
        int deliveryType = 0;
        String address1 = txtAddL1.getText().toString();
        String address2 = txtAddL2.getText().toString();
        deliveryType = radGrpDeliveryType.getCheckedRadioButtonId();
        String msg = "";
        boolean check = false;
        if (address1.isEmpty() || address1.equals(getString(R.string.str_address_main))) {
            Toast.makeText(getApplicationContext(), getString(R.string.str_add_not_selected),
                    Toast.LENGTH_SHORT).show();
            check = true;
        } else if (selectedLatLng == null) {
            Toast.makeText(getApplicationContext(), getString(R.string.str_err_location_map),
                    Toast.LENGTH_SHORT).show();
            check = true;
        } else if (deliveryType == 0) {
            Toast.makeText(getApplicationContext(), getString(R.string.str_delivery_type_not_selected),
                    Toast.LENGTH_SHORT).show();
            check = true;
        } else if (paymentMethod.isEmpty()) {
            Toast.makeText(getApplicationContext(), getString(R.string.str_payment_mode_not_slected),
                    Toast.LENGTH_SHORT).show();
            check = true;
        } else if (chkGift.isChecked()) {
            msg = edtGiftMsg.getText().toString();
            if (TextUtils.isEmpty(msg)) {
                check = true;
                edtGiftMsg.setError(getString(R.string.str_err_msg_empty));
            }
        }
        if (check) {

        } else {
            int deliveryId = radGrpDeliveryType.getCheckedRadioButtonId();
            switch (deliveryId) {
                case R.id.radio_delivery:
                    deliveryMethod = DeliveryMethods.HOME_DELIVERY;
                    break;
                case R.id.radio_pick_up:
                    deliveryMethod = DeliveryMethods.PICK_UP;
                    break;
            }
            if (NetworkUtils.isActiveNetworkAvailable(getApplicationContext())) {
                ArrayList<OrderItemDTO> itemList = qadmniHelper.getItemsList();
                InitOrderReqDTO initOrderReqDTO = new InitOrderReqDTO(itemList, address1 +
                        "," + address2, selectedLatLng.latitude, selectedLatLng.longitude,
                        deliveryMethod, deliverySchedule, paymentMethod);
                initOrderReqDTO.setGift(chkGift.isChecked());
                initOrderReqDTO.setGiftMessage(msg);
                Gson gson = new Gson();
                String serializedJsonString = gson.toJson(initOrderReqDTO);
                BaseRequestDTO baseRequestDTO = new BaseRequestDTO();
                baseRequestDTO.setData(serializedJsonString);
                mServerSyncManager.uploadDataToServer(ServerRequestConstants.REQUEST_INIT_ORDER,
                        mSessionManager.initOrderUrl(), baseRequestDTO);
            } else {
                createNetworkAlertDialog(getResources().getString(R.string.str_net_err),
                        getResources().getString(R.string.str_err_net_msg));
            }
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
        mGoogleMap.clear();
        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.setOnMapClickListener(new MapClick());
    }

    @Override
    public void onVolleyErrorReceived(@NonNull VolleyError error, int requestToken) {
        switch (requestToken) {
            case ServerRequestConstants.REQUEST_INIT_ORDER:
                customAlterDialog(getString(R.string.str_server_err_title), getString(R.string.str_server_err_desc));
                break;
        }
    }

    @Override
    public void onDataErrorReceived(int errorCode, String errorMessage, int requestToken) {
        switch (requestToken) {
            case ServerRequestConstants.REQUEST_INIT_ORDER:
                customAlterDialog(getString(R.string.str_place_order_title), errorMessage);
                break;
        }
    }

    @Override
    public void onResultReceived(@NonNull String data, int requestToken) {
        switch (requestToken) {
            case ServerRequestConstants.REQUEST_INIT_ORDER:
                InitOrderResDTO orderResDTO = InitOrderResDTO.deserializeJson(data);
                Intent iConfirmOrder = new Intent(getApplicationContext(), ConfirmAndPayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(ConfirmAndPayActivity.ORDER_INIT_DETAILS, data);
                iConfirmOrder.putExtras(bundle);
                startActivity(iConfirmOrder);
                finish();
                Log.d(TAG, "##" + orderResDTO.toString());
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean value) {
        switch (compoundButton.getId()) {
            case R.id.chk_asap_delivery:
                if (value) {
                    deliverySchedule = 0;
                    chkScheduleDelivery.setChecked(false);
                }
                break;
            case R.id.chk_schedule_delivery:
                if (value) {
                    chkAsapDelivery.setChecked(false);
                    showDate();
                }
                break;
            case R.id.chk_pay_pal:
                if (value) {
                    chkCash.setChecked(false);
                    paymentMethod = PaymentMethods.PAY_PAL;
                }
                break;
            case R.id.chk_cash:
                if (value) {
                    chkPayPal.setChecked(false);
                    paymentMethod = PaymentMethods.CASH;
                }
                break;
            case R.id.chk_gift_Wrap_delivery:
                if (value) {
                    layGift.setVisibility(View.VISIBLE);
                } else {
                    layGift.setVisibility(View.GONE);
                }
                break;
        }
    }

    private void showDate() {
        DatePickerDialog dialog = new DatePickerDialog(this, date, mFilterCalender
                .get(Calendar.YEAR), mFilterCalender.get(Calendar.MONTH),
                mFilterCalender.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMinDate(mFilterCalender.getTimeInMillis());
        dialog.show();
    }

    private class MapClick implements GoogleMap.OnMapClickListener {

        @Override
        public void onMapClick(LatLng latLng) {
            selectedLatLng = latLng;
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            mGoogleMap.clear();
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
                mGoogleMap.clear();
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

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            mFilterCalender.set(Calendar.YEAR, year);
            mFilterCalender.set(Calendar.MONTH, monthOfYear);
            mFilterCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            showTime();
        }
    };

    private void showTime() {
        TimePickerDialog dialog = new TimePickerDialog(this, time,
                mFilterCalender.get(Calendar.HOUR_OF_DAY), mFilterCalender.get(Calendar.MINUTE), false);
        dialog.show();
    }

    private void updateLabel() {
        String formattedDate = dateUtils.getReadableDateNTime(mFilterCalender.getTime());
        deliverySchedule = mFilterCalender.getTime().getTime() / 1000;
        txtScheduleDate.setText(formattedDate);
        chkScheduleDelivery.setChecked(true);
        chkAsapDelivery.setChecked(false);
    }

    TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int minutes) {
            mFilterCalender.set(Calendar.HOUR_OF_DAY, hour);
            mFilterCalender.set(Calendar.MINUTE, minutes);
            updateLabel();
        }
    };
}
