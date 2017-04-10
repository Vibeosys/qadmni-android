package com.qadmni.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.qadmni.R;
import com.qadmni.helpers.LocaleHelper;

public class SplashActivity extends BaseActivity {
    private static int SPLASH_TIME_OUT = 3000;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        imageView = (ImageView) findViewById(R.id.logo_image);
        LocaleHelper.setLocale(getApplicationContext(), mSessionManager.getUserSelectedLang().toLowerCase());
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_effect);
        animation.setFillEnabled(true);
        animation.setFillAfter(true);
        imageView.startAnimation(animation);
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent mainRun = new Intent(getApplicationContext(), MainLoginActivity.class);
                startActivity(mainRun);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
