package com.tasknobu.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.tasknobu.R;
import com.tasknobu.adapters.WorkPlacesAdapter;
import com.tasknobu.adapters.WorkPlacesDataAdapter;
import com.tasknobu.databinding.ActivityWorkPlaceDataBinding;
import com.tasknobu.model.SuccessResWorkplaceData;
import com.tasknobu.model.SuccessResWorkplaceData;
import com.tasknobu.retrofit.ApiClient;
import com.tasknobu.retrofit.TaskNobuInterface;
import com.tasknobu.utils.DataManager;
import com.tasknobu.utils.SharedPreferenceUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tasknobu.retrofit.Constant.USER_ID;
import static com.tasknobu.retrofit.Constant.showToast;

public class WorkPlaceDataAct extends AppCompatActivity {

    ActivityWorkPlaceDataBinding binding;

    private String id="";

    private ArrayList<SuccessResWorkplaceData.Result> workplaceDataList  = new ArrayList<>();

    private TaskNobuInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_work_place_data);

        apiInterface = ApiClient.getClient().create(TaskNobuInterface.class);

        binding.header.imgHeader.setOnClickListener(v ->
                {
                    finish();
                }
                );



        binding.header.tvHeader.setText(R.string.wokrplace_data);
        id = getIntent().getExtras().getString("id");
        getTask();

        binding.btnQuestions.setOnClickListener(v ->
                {
                    startActivity(new Intent(WorkPlaceDataAct.this,NewQuestionariesAct.class).putExtra("id",id));
                }
        );

    }

    private void getTask() {
        DataManager.getInstance().showProgressMessage(WorkPlaceDataAct.this, getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("work_place_id",id);
        Call<SuccessResWorkplaceData> call = apiInterface.getWorkPlaceDataById(map);
        call.enqueue(new Callback<SuccessResWorkplaceData>() {
            @Override
            public void onResponse(Call<SuccessResWorkplaceData> call, Response<SuccessResWorkplaceData> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResWorkplaceData data = response.body();
                    Log.e("data",data.status+"");
                    if (data.status.equalsIgnoreCase("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);

                        workplaceDataList.clear();
                        workplaceDataList.addAll(data.getResult());
                        binding.rvWorkPlaceData.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        binding.rvWorkPlaceData.setHasFixedSize(true);
                        binding.rvWorkPlaceData.setAdapter(new WorkPlacesDataAdapter(getApplicationContext(),workplaceDataList));

                    } else if (data.status.equalsIgnoreCase("0")) {
                        showToast(WorkPlaceDataAct.this, data.getMessage());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<SuccessResWorkplaceData> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

}