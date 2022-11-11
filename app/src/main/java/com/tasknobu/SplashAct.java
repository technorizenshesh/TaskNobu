package com.tasknobu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.tasknobu.activities.HomeAct;
import com.tasknobu.activities.LoginAct;
import com.tasknobu.retrofit.Constant;
import com.tasknobu.utils.SharedPreferenceUtility;

import java.util.Locale;

public class SplashAct extends AppCompatActivity {

    private Boolean isUserLoggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        updateResources(SplashAct.this,"es");
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        isUserLoggedIn = SharedPreferenceUtility.getInstance(SplashAct.this).getBoolean(Constant.IS_USER_LOGGED_IN);
        finds();
    }

    private static void updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources resources = context.getResources();

        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }

    private void finds() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isUserLoggedIn) {
                    startActivity(new Intent(SplashAct.this, HomeAct.class));
                    finish();
                } else {
                    startActivity(new Intent(SplashAct.this, LoginAct.class));
                    finish();
                }
            }
        },5000);
    }
}