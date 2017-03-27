package com.qadmni.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.qadmni.MainActivity;
import com.qadmni.R;
import com.qadmni.helpers.LocaleHelper;

import java.util.ArrayList;
import java.util.List;

public class SelectLanguageActivity extends BaseActivity {
    private Spinner mSpinner;
    private TextView btnSave;
    private String lang = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_langauge);
        setTitle(getResources().getString(R.string.action_settings));
        mSpinner = (Spinner) findViewById(R.id.spinner);
        btnSave = (TextView) findViewById(R.id.btn_save);
        List<String> spineerData = new ArrayList<>();
        spineerData.add(getResources().getString(R.string.str_english));
        spineerData.add(getResources().getString(R.string.str_arabic));
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (getBaseContext(), android.R.layout.simple_list_item_1, spineerData);

        dataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        mSpinner.setAdapter(dataAdapter);
        String strSelectedLang = mSessionManager.getUserSelectedLang();
        if (strSelectedLang.equals("En")) {
            mSpinner.setSelection(0);
        } else if (strSelectedLang.equals("Ar")) {
            mSpinner.setSelection(1);
        }
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    lang = "en";


                } else if (position == 1) {
                    lang = "ar";

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocaleHelper.setLocale(getApplicationContext(), lang);
                if (lang.equals("en")) {
                    mSessionManager.setUserSelectedLang("En");
                } else if (lang.equals("ar")) {
                    mSessionManager.setUserSelectedLang("Ar");
                }
                recreate();

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);

    }
}
