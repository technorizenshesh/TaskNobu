package com.tasknobu.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tasknobu.R;
import com.tasknobu.databinding.ActivityLoginBinding;
import com.tasknobu.model.SuccessResSignIn;
import com.tasknobu.retrofit.ApiClient;
import com.tasknobu.retrofit.Constant;
import com.tasknobu.retrofit.NetworkAvailablity;
import com.tasknobu.retrofit.TaskNobuInterface;
import com.tasknobu.utils.BackgroundService;
import com.tasknobu.utils.DataManager;
import com.tasknobu.utils.GPSTracker;
import com.tasknobu.utils.SharedPreferenceUtility;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tasknobu.retrofit.Constant.showToast;

public class LoginAct extends AppCompatActivity {

    ActivityLoginBinding binding;
    private TaskNobuInterface apiInterface;
    private String strEmail ="",strPassword= "",deviceToken = "";
    private GPSTracker gpsTracker;
    private String strLat="";
    private String strLng="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login);
        gpsTracker = new GPSTracker(LoginAct.this);
        binding.btnLogin.setOnClickListener(v ->
                {
                    validation();
                }
                );

        binding.loggedInRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!binding.loggedInRadio.isSelected()) {
                    binding.loggedInRadio.setChecked(true);
                    binding.loggedInRadio.setSelected(true);
                } else {
                    binding.loggedInRadio.setChecked(false);
                    binding.loggedInRadio.setSelected(false);
                }
            }
        });

        getLocation();
        apiInterface = ApiClient.getClient().create(TaskNobuInterface.class);
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(LoginAct.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(LoginAct.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LoginAct.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    Constant.LOCATION_REQUEST);
        } else {
            Log.e("Latittude====",gpsTracker.getLatitude()+"");
            strLat = Double.toString(gpsTracker.getLatitude()) ;
            strLng = Double.toString(gpsTracker.getLongitude()) ;
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

                } else {
                    Toast.makeText(LoginAct.this, LoginAct.this.getResources().getString(R.string.permisson_denied), Toast.LENGTH_SHORT).show();
                }
                break;
            }

        }
    }

    private void validation() {
        strEmail=binding.etEmail.getText().toString();
        strPassword=binding.etPass.getText().toString();
        if(strEmail.equalsIgnoreCase(""))
        {
            Toast.makeText(this, ""+getString(R.string.enter_your_email), Toast.LENGTH_SHORT).show();
        }else  if(strPassword.equalsIgnoreCase(""))
        {
            Toast.makeText(this, ""+getString(R.string.enter_your_password), Toast.LENGTH_SHORT).show();
        }else
        {
            if (NetworkAvailablity.checkNetworkStatus(this)) {
                login();
            } else {
                Toast.makeText(this, getResources().getString(R.string.msg_noInternet), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void login() {
        DataManager.getInstance().showProgressMessage(LoginAct.this, getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("email",strEmail);
        map.put("password",strPassword);
        map.put("register_id","");

        Call<SuccessResSignIn> call = apiInterface.login(map);
        call.enqueue(new Callback<SuccessResSignIn>() {
            @Override
            public void onResponse(Call<SuccessResSignIn> call, Response<SuccessResSignIn> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResSignIn data = response.body();
                    Log.e("data",data.status+"");
                    if (data.status.equalsIgnoreCase("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);
                        if(binding.loggedInRadio.isChecked())
                        {
                            SharedPreferenceUtility.getInstance(getApplication()).putBoolean(Constant.IS_USER_LOGGED_IN, true);
                        }

                        SharedPreferenceUtility.getInstance(LoginAct.this).putString(Constant.USER_ID,data.getResult().getId());
                        Toast.makeText(LoginAct.this, getString(R.string.logged_in_successful),Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginAct.this,HomeAct.class));
                        finish();
                    } else if (data.status.equalsIgnoreCase("0")) {
                        showToast(LoginAct.this, data.getMessage());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResSignIn> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

}