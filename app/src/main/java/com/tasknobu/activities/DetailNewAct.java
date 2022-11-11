package com.tasknobu.activities;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.tasknobu.R;
import com.tasknobu.adapters.OfferAdapter;
import com.tasknobu.adapters.WorkPlacesAdapter;
import com.tasknobu.databinding.ActivityDetailBinding;
import com.tasknobu.databinding.ActivityNewDetailBinding;
import com.tasknobu.model.SuccessResCheckboxChecked;
import com.tasknobu.model.SuccessResGetDates;
import com.tasknobu.model.SuccessResGetWorkPlace;
import com.tasknobu.model.SuccessResGetWorkPlace;
import com.tasknobu.model.SuccessResUpdateLoca;
import com.tasknobu.retrofit.ApiClient;
import com.tasknobu.retrofit.Constant;
import com.tasknobu.retrofit.TaskNobuInterface;
import com.tasknobu.utils.BackgroundService;
import com.tasknobu.utils.CheckboxCheckedListener;
import com.tasknobu.utils.DataManager;
import com.tasknobu.utils.GPSTracker;
import com.tasknobu.utils.SharedPreferenceUtility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tasknobu.retrofit.Constant.USER_ID;
import static com.tasknobu.retrofit.Constant.showToast;

public class DetailNewAct extends AppCompatActivity {

    ActivityNewDetailBinding binding;
    ArrayList<String> datesList = new ArrayList<>();
    private TaskNobuInterface apiInterface;
    private ArrayList<SuccessResGetWorkPlace.Result> workPlaceList = new ArrayList<>();
    private String date="";
    private WorkPlacesAdapter workPlacesAdapter;
    private SuccessResGetWorkPlace SuccessResGetWorkPlace;
    private boolean firstTime = false;
    private SuccessResGetWorkPlace.Result taskResult;
    Intent serviceIntent;
    private GPSTracker gpsTracker;
    private String strLat="";
    private String strLng="";
    private String strDesLat="",strDesLng="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this,R.layout.activity_new_detail);

        gpsTracker = new GPSTracker(DetailNewAct.this);

        apiInterface = ApiClient.getClient().create(TaskNobuInterface.class);

        binding.header.tvHeader.setText(getString(R.string.details));

        binding.header.imgHeader.setOnClickListener(v ->
                {
                    finish();
                }
                );
        getTask();
        getLocation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("public", "onResume: Called ");
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
//            if (requestCode == Constant.GPS_REQUEST) {
//                isGPS = true; // flag maintain before get location
//            }
        }
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(DetailNewAct.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(DetailNewAct.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(DetailNewAct.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    Constant.LOCATION_REQUEST);
        } else {
            Log.e("Latittude====",gpsTracker.getLatitude()+"");
            strLat = Double.toString(gpsTracker.getLatitude()) ;
            strLng = Double.toString(gpsTracker.getLongitude()) ;

            if(!isMyServiceRunning(BackgroundService.class))
            {
                serviceIntent = new Intent(this, BackgroundService.class);
                startService(serviceIntent);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case 1000: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("Latittude====", gpsTracker.getLatitude() + "");
                    strLat = Double.toString(gpsTracker.getLatitude());
                    strLng = Double.toString(gpsTracker.getLongitude());
                    if(!isMyServiceRunning(BackgroundService.class))
                    {
                        serviceIntent = new Intent(this, BackgroundService.class);
                        startService(serviceIntent);
                    }

//                    if (isContinue) {
//                        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(DetailAct.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                            // TODO: Consider calling
//                            //    ActivityCompat#requestPermissions
//                            // here to request the missing permissions, and then overriding
//                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                            //                                          int[] grantResults)
//                            // to handle the case where the user grants the permission. See the documentation
//                            // for ActivityCompat#requestPermissions for more details.
//                            return;
//                        }
//                        mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
//                    } else {
//                        Log.e("Latittude====", gpsTracker.getLatitude() + "");
//
//                        strLat = Double.toString(gpsTracker.getLatitude()) ;
//                        strLng = Double.toString(gpsTracker.getLongitude()) ;
//                    }
                } else {
                    Toast.makeText(DetailNewAct.this, DetailNewAct.this.getResources().getString(R.string.permisson_denied), Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }
    
    private void getTask() {

        String userId = SharedPreferenceUtility.getInstance(DetailNewAct.this).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(DetailNewAct.this, getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();

        Call<SuccessResGetWorkPlace> call = apiInterface.getWorkPlace(map);
        call.enqueue(new Callback<SuccessResGetWorkPlace>() {
            @Override
            public void onResponse(Call<SuccessResGetWorkPlace> call, Response<SuccessResGetWorkPlace> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetWorkPlace data = response.body();
                    Log.e("data",data.status+"");
                    if (data.status.equalsIgnoreCase("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                         SuccessResGetWorkPlace = data;

                         workPlaceList.clear();
                         workPlaceList.addAll(data.getResult());
                         workPlacesAdapter = new WorkPlacesAdapter(DetailNewAct.this,workPlaceList);
                         binding.rvWorkDetail.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                         binding.rvWorkDetail.setHasFixedSize(true);
                         binding.rvWorkDetail.setAdapter(workPlacesAdapter);

                    } else if (data.status.equalsIgnoreCase("0")) {
                        showToast(DetailNewAct.this, data.getMessage());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetWorkPlace> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        stopService(serviceIntent);
    }


    private void updateArrivalTime(String taskId) {

        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

        String userId = SharedPreferenceUtility.getInstance(DetailNewAct.this).getString(USER_ID);

        DataManager.getInstance().showProgressMessage(DetailNewAct.this, getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("task_id",taskId);
        map.put("worker_id",userId);
        map.put("arrival_date",currentDate);
        map.put("arrival_time",currentTime);

        Call<SuccessResUpdateLoca> call = apiInterface.addArrivalTime(map);
        call.enqueue(new Callback<SuccessResUpdateLoca>() {
            @Override
            public void onResponse(Call<SuccessResUpdateLoca> call, Response<SuccessResUpdateLoca> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResUpdateLoca data = response.body();
                    Log.e("data",data.status+"");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResUpdateLoca> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

}