package com.tasknobu.retrofit;

import com.tasknobu.model.SuccessResAddAnswer;
import com.tasknobu.model.SuccessResCheckboxChecked;
import com.tasknobu.model.SuccessResGetDates;
import com.tasknobu.model.SuccessResGetProfile;
import com.tasknobu.model.SuccessResGetTask;
import com.tasknobu.model.SuccessResGetTaskByWorkplaceId;
import com.tasknobu.model.SuccessResGetWorkPlace;
import com.tasknobu.model.SuccessResSignIn;
import com.tasknobu.model.SuccessResUpdateLoca;
import com.tasknobu.model.SuccessResUpdateLocation;
import com.tasknobu.model.SuccessResWorkplaceData;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface TaskNobuInterface {

    @FormUrlEncoded
    @POST("login")
    Call<SuccessResSignIn> login(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("update_lat_lon")
    Call<SuccessResUpdateLoca> updateLocation(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_task")
    Call<SuccessResGetTask> getTaskDetail(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_profile")
    Call<SuccessResGetProfile> getProfile(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_task_date")
    Call<SuccessResGetDates> getDates(@FieldMap Map<String, String> paramHashMap);

    @Multipart
    @POST("questionnaires_answer")
    Call<SuccessResAddAnswer> updateProfile (@Part("worker_id") RequestBody userId,
                                             @Part("task_id") RequestBody mobile,
                                             @Part("questionnaires_id") RequestBody fullname,
                                             @Part("answer") RequestBody address,
                                             @Part List<MultipartBody.Part> file);

    @FormUrlEncoded
    @POST("check_by_worker")
    Call<SuccessResCheckboxChecked> checkboxUpdate(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("delete_task_images")
    Call<SuccessResCheckboxChecked> removeImage(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("save_task_arrival_time")
    Call<SuccessResUpdateLoca> addArrivalTime(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("save_task_end_time")
    Call<SuccessResUpdateLoca> addEndTime(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("user_location")
    Call<SuccessResUpdateLocation> addUserLocation(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_work_places")
    Call<SuccessResGetWorkPlace> getWorkPlace(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_work_places_data")
    Call<SuccessResWorkplaceData> getWorkPlaceDataById(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST("get_task_new")
    Call<SuccessResGetTaskByWorkplaceId> getTaskByWorkplacesId(@FieldMap Map<String, String> paramHashMap);


    @Multipart
    @POST("questionnaires_answer")
    Call<ResponseBody> updateQA (@Part("worker_id") RequestBody userId,
                                             @Part("task_id") RequestBody fullname,
                                             @Part("answer") RequestBody address,
                                             @Part List<MultipartBody.Part> file);

}
