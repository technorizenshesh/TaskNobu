package com.tasknobu.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

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

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.tasknobu.R;
import com.tasknobu.adapters.OfferAdapter;
import com.tasknobu.databinding.ActivityDetailBinding;
import com.tasknobu.model.SuccessResCheckboxChecked;
import com.tasknobu.model.SuccessResGetDates;
import com.tasknobu.model.SuccessResGetTask;
import com.tasknobu.model.SuccessResGetTask;
import com.tasknobu.model.SuccessResGetDates;
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

public class DetailAct extends AppCompatActivity {

    ActivityDetailBinding binding;

    ArrayList<String> datesList = new ArrayList<>();

    private TaskNobuInterface apiInterface;

    private ArrayList<SuccessResGetTask.TaskDetail> taskDetailArrayList = new ArrayList<>();

    private String date="";

    private SuccessResGetTask successResGetTask;
    private boolean firstTime = false;
    private SuccessResGetTask.Result taskResult;
    private OfferAdapter offerAdapter;
    Intent serviceIntent;
    private GPSTracker gpsTracker;
    private String strLat="";
    private String strLng="";
    private String strDesLat="",strDesLng="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this,R.layout.activity_detail);

        gpsTracker = new GPSTracker(DetailAct.this);

        apiInterface = ApiClient.getClient().create(TaskNobuInterface.class);

        binding.header.tvHeader.setText(getString(R.string.details));

        binding.header.imgHeader.setOnClickListener(v ->
                {
                    finish();
                }
                );

        offerAdapter = new OfferAdapter(DetailAct.this, taskDetailArrayList, new CheckboxCheckedListener() {
            @Override
            public void checkboxClick(int position, String state, String taskId) {
                updateCheckbox(taskId,state);
            }
        });
        binding.rvTaskDescription.setHasFixedSize(true);
        binding.rvTaskDescription.setLayoutManager(new LinearLayoutManager(getApplication()));
        binding.rvTaskDescription.setAdapter(offerAdapter);
//        getDates();


        getLocation();
        binding.btnQuestions.setOnClickListener(v ->
                {
                    Intent intent = new Intent(DetailAct.this,QuestinariesAct.class);
                     intent.putExtra("result",new Gson().toJson(successResGetTask));
                     startActivity(intent);
                }
                );
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("public", "onResume: Called ");
        if(!date.equalsIgnoreCase(""))
        {
            getTask(date);
        }
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
        if (ActivityCompat.checkSelfPermission(DetailAct.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(DetailAct.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(DetailAct.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
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
                    Toast.makeText(DetailAct.this, DetailAct.this.getResources().getString(R.string.permisson_denied), Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }
    
    private void getTask(String task) {

        String userId = SharedPreferenceUtility.getInstance(DetailAct.this).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(DetailAct.this, getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("worker_id",userId);
        map.put("task_date",task);

        Call<SuccessResGetTask> call = apiInterface.getTaskDetail(map);
        call.enqueue(new Callback<SuccessResGetTask>() {
            @Override
            public void onResponse(Call<SuccessResGetTask> call, Response<SuccessResGetTask> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetTask data = response.body();
                    Log.e("data",data.status+"");
                    if (data.status.equalsIgnoreCase("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                         successResGetTask = data;
                         taskResult = successResGetTask.getResult().get(0);
                         setData();

                    } else if (data.status.equalsIgnoreCase("0")) {
                        showToast(DetailAct.this, data.getMessage());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetTask> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    public void setData()
    {

        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

        Log.d("TAG", "setData: "+currentDate+" Time : "+currentTime);

        taskDetailArrayList.clear();
        taskDetailArrayList.addAll(taskResult.getTaskDetails());
        offerAdapter.notifyDataSetChanged();

        Glide
                .with(DetailAct.this)
                .load(taskResult.getImage())
                .into(binding.ivProfile);

        binding.tvDestination1.setText(taskResult.getWorkDestination());

        strDesLat = taskResult.getTaskLat();
        strDesLng = taskResult.getTaskLon();

        binding.tvDetail.setOnClickListener(v ->
                {
                    String lat = strDesLat;
                    String lon = strDesLng;

                    //                String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?q=loc:%f,%f", lat,lon);
                    String uri = "http://maps.google.com/maps?q=loc:"+lat+","+lon;
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    DetailAct.this.startActivity(intent);
                }
                );

        updateArrivalTime(taskResult.getId());

    }

    private void setSpinner()
    {
        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,datesList);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        binding.spinner.setAdapter(aa);

        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                date = selectedItem;
                getTask(selectedItem);
                Log.d("public", "SelectedItem ????");
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        stopService(serviceIntent);
    }
    
    private void getDates()
    {
        String userId = SharedPreferenceUtility.getInstance(DetailAct.this).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(DetailAct.this, getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("worker_id",userId);

        Call<SuccessResGetDates> call = apiInterface.getDates(map);
        call.enqueue(new Callback<SuccessResGetDates>() {
            @Override
            public void onResponse(Call<SuccessResGetDates> call, Response<SuccessResGetDates> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetDates data = response.body();
                    Log.e("data",data.status+"");
                    if (data.status.equalsIgnoreCase("1")) {
                        String dataResponse = new Gson().toJson(response.body());

                        Log.d("public", "GetData ????");

                        datesList.clear();

                        for(SuccessResGetDates.Result result:data.getResult())
                        {
                            datesList.add( result.getTaskDate());
                        }

                        setSpinner();

                    } else if (data.status.equalsIgnoreCase("0")) {
                        showToast(DetailAct.this, data.getMessage());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetDates> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    private void updateCheckbox(String taskId,String status)
    {

        Map<String,String> map = new HashMap<>();
        map.put("id",taskId);
        map.put("checked",status);

        Call<SuccessResCheckboxChecked> call = apiInterface.checkboxUpdate(map);
        call.enqueue(new Callback<SuccessResCheckboxChecked>() {
            @Override
            public void onResponse(Call<SuccessResCheckboxChecked> call, Response<SuccessResCheckboxChecked> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResCheckboxChecked data = response.body();
                    Log.e("data",data.status+"");
                    if (data.status.equalsIgnoreCase("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                    } else if (data.status.equalsIgnoreCase("0")) {
                        showToast(DetailAct.this, data.getMessage());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResCheckboxChecked> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    private void updateArrivalTime(String taskId) {

        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

        String userId = SharedPreferenceUtility.getInstance(DetailAct.this).getString(USER_ID);

        DataManager.getInstance().showProgressMessage(DetailAct.this, getString(R.string.please_wait));
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