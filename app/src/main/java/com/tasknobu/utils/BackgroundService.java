package com.tasknobu.utils;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.tasknobu.R;
import com.tasknobu.activities.AddLocationAct;
import com.tasknobu.activities.HomeAct;
import com.tasknobu.model.SuccessResUpdateLoca;
import com.tasknobu.model.SuccessResUpdateLocation;
import com.tasknobu.retrofit.ApiClient;
import com.tasknobu.retrofit.Constant;
import com.tasknobu.retrofit.TaskNobuInterface;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tasknobu.retrofit.Constant.USER_ID;
import static com.tasknobu.retrofit.Constant.showToast;


/**
 * Created by Ravindra Birla on 09,June,2022
 */
public class BackgroundService extends Service {

    public Context context = this;
    public Handler handler = null;
    public static Runnable runnable = null;

    private GPSTracker gpsTracker;

    private TaskNobuInterface apiInterface;

    private String strAddress="",strLat="",strLng="";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
//        Toast.makeText(this, "Service created!", Toast.LENGTH_LONG).show();
        apiInterface = ApiClient.getClient().create(TaskNobuInterface.class);
        gpsTracker = new GPSTracker(context.getApplicationContext());
        handler = new Handler();
        runnable = new Runnable() {
            public void run() {

                strLat = Double.toString(gpsTracker.getLatitude()) ;
                strLng = Double.toString(gpsTracker.getLongitude()) ;
                login();
//                Toast.makeText(context, "Service is still running", Toast.LENGTH_LONG).show();
                handler.postDelayed(runnable, 10000);
            }
        };
        handler.postDelayed(runnable, 15000);
    }

    @Override
    public void onDestroy() {
        /* IF YOU WANT THIS SERVICE KILLED WITH THE APP THEN UNCOMMENT THE FOLLOWING LINE */
        handler.removeCallbacks(runnable);
//        Toast.makeText(this, "Service stopped", Toast.LENGTH_LONG).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String userID = intent.getStringExtra("UserID");
        return START_STICKY;
    }

    private void login() {

        Date date1 = Calendar.getInstance().getTime();
        DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String strDate1 = dateFormat1.format(date1);

        String userId = SharedPreferenceUtility.getInstance(context).getString(USER_ID);

        Date date = Calendar.getInstance().getTime();

        DateFormat dateFormat =  new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        String strDate = dateFormat.format(date);

        String newFormatedDate = parseDateToddMMyyyy(strDate);

        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("lat",strLat);
        map.put("lon",strLng);
        map.put("location_date",newFormatedDate);

        Call<SuccessResUpdateLocation> call = apiInterface.addUserLocation(map);
        call.enqueue(new Callback<SuccessResUpdateLocation>() {
            @Override
            public void onResponse(Call<SuccessResUpdateLocation> call, Response<SuccessResUpdateLocation> response) {

                try {
                    SuccessResUpdateLocation data = response.body();
                    Log.e("data",data.status+"");
                    if (data.status.equalsIgnoreCase("1")) {

                    } else if (data.status.equalsIgnoreCase("0")) {
//                        showToast(AddLocationAct.this, data.getMessage());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResUpdateLocation> call, Throwable t) {
                call.cancel();
            }
        });
    }

    public String parseDateToddMMyyyy(String time) {
        String inputPattern = "yyyy-MM-dd hh:mm:ss";
        String outputPattern = "yyyy-MM-dd";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    //    @Override
//    public void onStart(Intent intent, int startid) {
//        Toast.makeText(this, "Service started by user.", Toast.LENGTH_LONG).show();
//
//
//    }


}