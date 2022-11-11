package com.tasknobu.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tasknobu.R;
import com.tasknobu.adapters.QuestionsAdapterNew;
import com.tasknobu.adapters.WorkPlacesDataAdapter;
import com.tasknobu.databinding.ActivityNewQuestionariesBinding;
import com.tasknobu.model.SuccessResAddAnswer;
import com.tasknobu.model.SuccessResGetTaskByWorkplaceId;
import com.tasknobu.model.SuccessResGetTaskByWorkplaceId;
import com.tasknobu.model.SuccessResUpdateLoca;
import com.tasknobu.retrofit.ApiClient;
import com.tasknobu.retrofit.TaskNobuInterface;
import com.tasknobu.utils.DataManager;
import com.tasknobu.utils.SharedPreferenceUtility;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static com.tasknobu.retrofit.Constant.USER_ID;
import static com.tasknobu.retrofit.Constant.showToast;

public class NewQuestionariesAct extends AppCompatActivity {

    ActivityNewQuestionariesBinding binding;
    private ArrayList<SuccessResGetTaskByWorkplaceId.Result> newQuesnairesList = new ArrayList<>();
    private TaskNobuInterface apiInterface;
    private String id;

    private HashMap<String,String> map = new HashMap<>();

    private String ansCommaSeperated="";

    private String idsCommaSeperated="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_new_questionaries);

        apiInterface = ApiClient.getClient().create(TaskNobuInterface.class);

        binding.header.tvHeader.setText(getString(R.string.questionaries));
        binding.header.imgHeader.setOnClickListener(v ->
                {
                    finish();
                }
        );

        id = getIntent().getExtras().getString("id");

        getTask();

        binding.btnLogin.setOnClickListener(v ->
                {

                    map = new HashMap<>();
                    boolean goForward = true;
                    for (int i = 0; i < newQuesnairesList.size(); i++) {
                        View view = binding.rvQuestions.getChildAt(i);
                        EditText nameEditText = (EditText) view.findViewById(R.id.etQuestion);
                        String name = nameEditText.getText().toString();
                        map.put(newQuesnairesList.get(i).getTaskId(),name);
                    }
                    ansCommaSeperated="";
                    idsCommaSeperated="";
                    for (Map.Entry<String,String> entry : map.entrySet()) {
                        String key = entry.getKey();
                        String value = entry.getValue();

                        ansCommaSeperated = ansCommaSeperated+value+",";
                        idsCommaSeperated = idsCommaSeperated+key+",";

                        if(value.equalsIgnoreCase(""))
                        {
                            goForward  = false;
                        }
                        // do stuff
                        Log.d("TAG", "onCreate: Key :"+key+" value :" + value);
                    }

                    if(goForward)
                    {

                        ansCommaSeperated = ansCommaSeperated.substring(0, ansCommaSeperated.length() -1);
                        idsCommaSeperated = idsCommaSeperated.substring(0, idsCommaSeperated.length() -1);

                        uploadanswers(idsCommaSeperated,ansCommaSeperated,true);

//                        if(imagesList.size()>0)
//                        {
//                            uploadanswers(idsCommaSeperated,ansCommaSeperated,true);
//                        }
//                        else
//                        {
//                            Toast.makeText(NewQuestionariesAct.this, getString(R.string.please_upload_image),Toast.LENGTH_SHORT).show();
//                        }

                    }
                    else
                    {
                        Toast.makeText(NewQuestionariesAct.this,"Please upload all answers.",Toast.LENGTH_SHORT).show();
                    }
                }
        );

    }

    private void uploadanswers(String ids,String answer,boolean from)
    {
        String strUserId = SharedPreferenceUtility.getInstance(NewQuestionariesAct.this).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(NewQuestionariesAct.this, getString(R.string.please_wait));

        List<MultipartBody.Part> filePartList = new LinkedList<>();

//        for (int i = 0; i < imagesList.size(); i++) {
//
//            String image = imagesList.get(i);
//
//            if(!imagesList.get(i).contains("https://nobu.es/tasknobu/uploads"))
//            {
//                File file = DataManager.getInstance().saveBitmapToFile(new File(imagesList.get(i)));
//                filePartList.add(MultipartBody.Part.createFormData("image[]", file.getName(), RequestBody.create(MediaType.parse("image[]/*"), file)));
//            }
//        }

        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), strUserId);
        RequestBody questionIds = RequestBody.create(MediaType.parse("text/plain"), ids);
        RequestBody answers = RequestBody.create(MediaType.parse("text/plain"),answer);

        Call<ResponseBody> loginCall = apiInterface.updateQA(userId,questionIds,answers,filePartList);
        loginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();

                DataManager.getInstance().hideProgressMessage();
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    String data = jsonObject.getString("status");
                    String message = jsonObject.getString("message");
                    if (data.equals("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        showToast(NewQuestionariesAct.this,""+message);
                        if(from)
                    {
//                        updateArrivalTime(successResGetTask.getResult().get(0).getId());
                    }
                    } else if (data.equals("0")) {
                        showToast(NewQuestionariesAct.this, message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

//                try {
//                    ResponseBody data = response.body();
//                    String responseString = new Gson().toJson(response.body());
//                    Log.e(TAG,"Test Response :"+responseString);
//
//                    if(from)
//                    {
//                        updateArrivalTime(successResGetTask.getResult().get(0).getId());
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    Log.e(TAG,"Test Response :"+response.body());
//                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }


    private void getTask() {
        DataManager.getInstance().showProgressMessage(NewQuestionariesAct.this, getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("work_place_id",id);
        Call<SuccessResGetTaskByWorkplaceId> call = apiInterface.getTaskByWorkplacesId(map);
        call.enqueue(new Callback<SuccessResGetTaskByWorkplaceId>() {
            @Override
            public void onResponse(Call<SuccessResGetTaskByWorkplaceId> call, Response<SuccessResGetTaskByWorkplaceId> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResGetTaskByWorkplaceId data = response.body();
                    Log.e("data",data.status+"");
                    if (data.status.equalsIgnoreCase("1")) {
                        String dataResponse = new Gson().toJson(response.body());
                        Log.e("MapMap", "EDIT PROFILE RESPONSE" + dataResponse);

                        newQuesnairesList.clear();
                        newQuesnairesList.addAll(data.getResult());
                        binding.rvQuestions.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        binding.rvQuestions.setHasFixedSize(true);
                        binding.rvQuestions.setAdapter(new QuestionsAdapterNew(NewQuestionariesAct.this,newQuesnairesList));

                    } else if (data.status.equalsIgnoreCase("0")) {
                        showToast(NewQuestionariesAct.this, data.getMessage());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<SuccessResGetTaskByWorkplaceId> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    private void updateArrivalTime(String taskId) {

        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

        String userId = SharedPreferenceUtility.getInstance(NewQuestionariesAct.this).getString(USER_ID);

        DataManager.getInstance().showProgressMessage(NewQuestionariesAct.this, getString(R.string.please_wait));
        Map<String,String> map = new HashMap<>();
        map.put("task_id",taskId);
        map.put("worker_id",userId);
        map.put("end_date",currentDate);
        map.put("end_time",currentTime);

        Call<SuccessResUpdateLoca> call = apiInterface.addEndTime(map);
        call.enqueue(new Callback<SuccessResUpdateLoca>() {
            @Override
            public void onResponse(Call<SuccessResUpdateLoca> call, Response<SuccessResUpdateLoca> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResUpdateLoca data = response.body();
                    Log.e("data end",data.status+"");
                    if (data.status.equalsIgnoreCase("1")) {
                        Intent i = new Intent(NewQuestionariesAct.this, HomeAct.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    } else if (data.status.equalsIgnoreCase("0")) {
                        showToast(NewQuestionariesAct.this, data.getMessage());
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



}