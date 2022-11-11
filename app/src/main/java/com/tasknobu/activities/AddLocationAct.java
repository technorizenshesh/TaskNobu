package com.tasknobu.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.gson.Gson;
import com.tasknobu.R;
import com.tasknobu.databinding.ActivityAddLocationBinding;
import com.tasknobu.model.SuccessResUpdateLoca;
import com.tasknobu.model.SuccessResUpdateLoca;
import com.tasknobu.retrofit.ApiClient;
import com.tasknobu.retrofit.Constant;
import com.tasknobu.retrofit.TaskNobuInterface;
import com.tasknobu.utils.DataManager;
import com.tasknobu.utils.SharedPreferenceUtility;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tasknobu.retrofit.Constant.USER_ID;
import static com.tasknobu.retrofit.Constant.showToast;

public class AddLocationAct extends AppCompatActivity {

    ActivityAddLocationBinding binding;

    private static int AUTOCOMPLETE_REQUEST_CODE = 1;

    private String strAddress= "",strLat="",strLong="";

    private TaskNobuInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_add_location);

        apiInterface = ApiClient.getClient().create(TaskNobuInterface.class);

        Places.initialize(getApplicationContext(), "AIzaSyA1zVQsDeyYQJbE64CmQVSfzNO-AwFoUNk");

        // Create a new PlacesClient instance
        PlacesClient placesClient = Places.createClient(this);

        binding.btnContinue.setOnClickListener(v ->
                {

                    if(!strAddress.equalsIgnoreCase(""))
                    {
                        login();
                    } else
                    {
                        showToast(AddLocationAct.this,"Please add Location.");
                    }
                }
        );

        binding.tvLocation.setOnClickListener(v ->
                {
                    // return after the user has made a selection.
                    List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.ADDRESS,Place.Field.LAT_LNG);
                    // Start the autocomplete intent.
                    Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                            .build(AddLocationAct.this);
                    startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
                }
                );

    }

    private void login() {

        String userId = SharedPreferenceUtility.getInstance(AddLocationAct.this).getString(USER_ID);

        DataManager.getInstance().showProgressMessage(AddLocationAct.this, getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("location",strAddress);
        map.put("lat",strLat);
        map.put("lon",strLong);

        Call<SuccessResUpdateLoca> call = apiInterface.updateLocation(map);
        call.enqueue(new Callback<SuccessResUpdateLoca>() {
            @Override
            public void onResponse(Call<SuccessResUpdateLoca> call, Response<SuccessResUpdateLoca> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResUpdateLoca data = response.body();
                    Log.e("data",data.status+"");
                    if (data.status.equalsIgnoreCase("1")) {

                        SharedPreferenceUtility.getInstance(getApplication()).putBoolean(Constant.IS_USER_LOGGED_IN, true);
                        startActivity(new Intent(AddLocationAct.this,HomeAct.class));
                        finish();
                    } else if (data.status.equalsIgnoreCase("0")) {
                        showToast(AddLocationAct.this, data.getMessage());
                    }

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



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i("", "Place: " + place.getName() + ", " + place.getId());

                strAddress = place.getAddress();
                LatLng latLng = place.getLatLng();

                Double latitude = latLng.latitude;
                Double longitude = latLng.longitude;

                strLat = Double.toString(latitude);
                strLong = Double.toString(longitude);

                String address = place.getAddress();

                strAddress = address;

                binding.tvLocation.setText(address);

                binding.tvLocation.post(new Runnable(){
                    @Override
                    public void run() {
                        binding.tvLocation.setText(address);
                    }
                });

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i("", status.getStatusMessage());


            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}