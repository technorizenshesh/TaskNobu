package com.tasknobu.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.tasknobu.R;
import com.tasknobu.databinding.ActivityHomeBinding;
import com.tasknobu.model.SuccessResGetProfile;
import com.tasknobu.model.SuccessResGetProfile;
import com.tasknobu.retrofit.ApiClient;
import com.tasknobu.retrofit.Constant;
import com.tasknobu.retrofit.TaskNobuInterface;
import com.tasknobu.utils.BackgroundService;
import com.tasknobu.utils.DataManager;
import com.tasknobu.utils.SharedPreferenceUtility;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tasknobu.retrofit.Constant.USER_ID;
import static com.tasknobu.retrofit.Constant.showToast;

public class HomeAct extends AppCompatActivity {

    ActivityHomeBinding binding;

    private TaskNobuInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_home);

        apiInterface = ApiClient.getClient().create(TaskNobuInterface.class);

        binding.btnDownload.setOnClickListener(v ->
                {
                    startActivity(new Intent(HomeAct.this,DetailNewAct.class));
                }
                );

        binding.ivLogout.setOnClickListener(v ->
                {
                    stopService(new Intent(HomeAct.this, BackgroundService.class));
                    Intent i = new Intent(HomeAct.this, LoginAct.class);
// set the new task and clear flags
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }
                );

        getProfile();

//        binding.ivSetting.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (binding.drawer.isDrawerOpen(GravityCompat.END))
//                    binding.drawer.closeDrawer(GravityCompat.END);
//                else binding.drawer.openDrawer(GravityCompat.END);
//            }
//        });

//        setNavigationItemClick();

    }

    private void getProfile()
    {

        String userId = SharedPreferenceUtility.getInstance(HomeAct.this).getString(USER_ID);

        DataManager.getInstance().showProgressMessage(HomeAct.this, getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("user_id",userId);

        Call<SuccessResGetProfile> call = apiInterface.getProfile(map);
        call.enqueue(new Callback<SuccessResGetProfile>() {
            @Override
            public void onResponse(Call<SuccessResGetProfile> call, Response<SuccessResGetProfile> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetProfile data = response.body();
                    Log.e("data",data.status+"");
                    if (data.status.equalsIgnoreCase("1")) {

                       binding.tvHeader.setText(getString(R.string.welcome) +" "+data.getResult().get(0).getFirstName());

                    } else if (data.status.equalsIgnoreCase("0")) {
                        showToast(HomeAct.this, data.getMessage());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SuccessResGetProfile> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });

    }

   /* private void setNavigationItemClick()
    {
        binding.drawerLayout.tvProfile.setOnClickListener(v ->
                {
                    binding.drawer.closeDrawer(GravityCompat.END);
                    startActivity(new Intent(HomeAct.this,ProfileAct.class));
                }
        );

        binding.drawerLayout.tvDownloadedTask.setOnClickListener(v ->
                {
                    binding.drawer.closeDrawer(GravityCompat.END);
                    startActivity(new Intent(HomeAct.this,DetailAct.class));
                }
        );

        binding.drawerLayout.tvMyOrders.setOnClickListener(v ->
                {
                    binding.drawer.closeDrawer(GravityCompat.END);
                    startActivity(new Intent(HomeAct.this,HelpAct.class));
                }
        );

        binding.drawerLayout.btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(HomeAct.this,
                    LoginAct.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }*/

}