package com.tasknobu.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.gson.Gson;
import com.tasknobu.BuildConfig;
import com.tasknobu.R;
import com.tasknobu.adapters.MultipleImagesAdapter;
import com.tasknobu.adapters.QuestionsAdapter;
import com.tasknobu.databinding.ActivityQuestinariesBinding;
import com.tasknobu.model.SuccessResAddAnswer;
import com.tasknobu.model.SuccessResCheckboxChecked;
import com.tasknobu.model.SuccessResGetTask;
import com.tasknobu.model.SuccessResUpdateLoca;
import com.tasknobu.retrofit.ApiClient;
import com.tasknobu.retrofit.Constant;
import com.tasknobu.retrofit.TaskNobuInterface;
import com.tasknobu.utils.DataManager;
import com.tasknobu.utils.ImageCancelClick;
import com.tasknobu.utils.RealPathUtil;
import com.tasknobu.utils.SharedPreferenceUtility;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static com.tasknobu.retrofit.Constant.USER_ID;
import static com.tasknobu.retrofit.Constant.showToast;

public class QuestinariesAct extends AppCompatActivity {

    private String str_image_path="";
    private Uri uriSavedImage;
    ActivityQuestinariesBinding binding;
    private static final int SELECT_FILE = 2;
    private SuccessResGetTask successResGetTask;
    private static final int MY_PERMISSION_CONSTANT = 5;
    private static final int REQUEST_CAMERA = 1;

    private String ansCommaSeperated="";

    private String idsCommaSeperated="";

    private TaskNobuInterface apiInterface;

    private HashMap<String,String> map = new HashMap<>();

    private String taskId= "";

    private ArrayList<String> imagesList = new ArrayList<>();

    private MultipleImagesAdapter multipleImagesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding = DataBindingUtil.setContentView(this ,R.layout.activity_questinaries);
       binding.header.tvHeader.setText(getString(R.string.questionaries));
       binding.header.imgHeader.setOnClickListener(v ->
               {
                   finish();
               }
               );

        apiInterface = ApiClient.getClient().create(TaskNobuInterface.class);

        Bundle bundle = getIntent().getExtras();

        String result =  bundle.getString("result");

        successResGetTask = new Gson().fromJson(result, SuccessResGetTask.class);

        Log.e("sdffsdfds","REsult = " +successResGetTask.getResult());

        binding.rvQuestions.setHasFixedSize(true);
        binding.rvQuestions.setLayoutManager(new LinearLayoutManager(QuestinariesAct.this));
        binding.rvQuestions.setAdapter(new QuestionsAdapter(QuestinariesAct.this,successResGetTask.getResult().get(0).getTaskQuestionnaires()));

        taskId = successResGetTask.getResult().get(0).getId();

        setImagesAdapter();

        binding.btnLogin.setOnClickListener(v ->
                {
                    boolean goForward = true;
                    for (int i = 0; i < successResGetTask.getResult().get(0).getTaskQuestionnaires().size(); i++) {
                        View view = binding.rvQuestions.getChildAt(i);
                        EditText nameEditText = (EditText) view.findViewById(R.id.etQuestion);
                        String name = nameEditText.getText().toString();
                        map.put(successResGetTask.getResult().get(0).getTaskQuestionnaires().get(i).getId(),name);
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

                        if(imagesList.size()>0)
                        {
                            uploadanswers(idsCommaSeperated,ansCommaSeperated,true);
                        }
                        else
                        {
                            Toast.makeText(QuestinariesAct.this, getString(R.string.please_upload_image),Toast.LENGTH_SHORT).show();
                        }

                    }
                    else
                    {
                        Toast.makeText(QuestinariesAct.this,"Please upload all answers.",Toast.LENGTH_SHORT).show();
                    }
                }
                );

        binding.tvUploadPhoto.setOnClickListener(v ->
                {
                    if(checkPermisssionForReadStorage())
                        showImageSelection();
                }
        );
    }

    private void setImagesAdapter() {

        imagesList.clear();

        if(successResGetTask.getResult().get(0).getTaskImageByWorker()!=null)
        {
            if(successResGetTask.getResult().get(0).getTaskImageByWorker().size()>0)
            {
                for(SuccessResGetTask.TaskImageByWorker result: successResGetTask.getResult().get(0).getTaskImageByWorker())
                {
                    imagesList.add(result.getImage());
                }
            }
        }

        multipleImagesAdapter = new MultipleImagesAdapter(QuestinariesAct.this, imagesList, new ImageCancelClick() {
            @Override
            public void imageCancel(int position) {

                if(imagesList.get(position).contains("https://nobu.es/tasknobu/uploads"))
                {

                    removeImage(imagesList.get(position));
                    imagesList.remove(position);
                    multipleImagesAdapter.notifyDataSetChanged();
                } else
                {
                    imagesList.remove(position);
                    multipleImagesAdapter.notifyDataSetChanged();
                }
            }
        });
        binding.rvImages.setHasFixedSize(true);
        binding.rvImages.setLayoutManager(new GridLayoutManager(this, 3));
        binding.rvImages.setAdapter(multipleImagesAdapter);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case 1000: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(QuestinariesAct.this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case MY_PERMISSION_CONSTANT: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0) {
                    boolean camera = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean read_external_storage = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean write_external_storage = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    if (camera && read_external_storage && write_external_storage) {
                        showImageSelection();
                    } else {
                        Toast.makeText(QuestinariesAct.this, getResources().getString(R.string.permission_denied_boo), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(QuestinariesAct.this, getResources().getString(R.string.permission_denied_boo), Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }
    private void uploadanswers(String ids,String answer,boolean from)
    {
        String strUserId = SharedPreferenceUtility.getInstance(QuestinariesAct.this).getString(USER_ID);
        DataManager.getInstance().showProgressMessage(QuestinariesAct.this, getString(R.string.please_wait));

        List<MultipartBody.Part> filePartList = new LinkedList<>();

        for (int i = 0; i < imagesList.size(); i++) {

            String image = imagesList.get(i);

            if(!imagesList.get(i).contains("https://nobu.es/tasknobu/uploads"))
            {
                File file = DataManager.getInstance().saveBitmapToFile(new File(imagesList.get(i)));
                filePartList.add(MultipartBody.Part.createFormData("image[]", file.getName(), RequestBody.create(MediaType.parse("image[]/*"), file)));
            }

           }

        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), strUserId);
        RequestBody taskIds = RequestBody.create(MediaType.parse("text/plain"), taskId);
        RequestBody questionIds = RequestBody.create(MediaType.parse("text/plain"), ids);
        RequestBody answers = RequestBody.create(MediaType.parse("text/plain"),answer);

        Call<SuccessResAddAnswer> loginCall = apiInterface.updateProfile(userId,taskIds,questionIds,answers,filePartList);
        loginCall.enqueue(new Callback<SuccessResAddAnswer>() {
            @Override
            public void onResponse(Call<SuccessResAddAnswer> call, Response<SuccessResAddAnswer> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    SuccessResAddAnswer data = response.body();
                    String responseString = new Gson().toJson(response.body());
                    Log.e(TAG,"Test Response :"+responseString);

                    if(from)
                    {
                       updateArrivalTime(successResGetTask.getResult().get(0).getId());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG,"Test Response :"+response.body());
                }
            }

            @Override
            public void onFailure(Call<SuccessResAddAnswer> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void showImageSelection() {

        final Dialog dialog = new Dialog(QuestinariesAct.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Widget_Material_ListPopupWindow;
        dialog.setContentView(R.layout.dialog_show_image_selection);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());

        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        LinearLayout layoutCamera = (LinearLayout) dialog.findViewById(R.id.layoutCemera);
        LinearLayout layoutGallary = (LinearLayout) dialog.findViewById(R.id.layoutGallary);
        layoutCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                dialog.cancel();
                openCamera();
            }
        });

        layoutGallary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                dialog.cancel();
                getPhotoFromGallary();

//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_MULTIPLE);

            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void removeImage(String image)
    {

        String imageId="";

        if(successResGetTask.getResult().get(0).getTaskImageByWorker().size()>0)
        {

            for(SuccessResGetTask.TaskImageByWorker result: successResGetTask.getResult().get(0).getTaskImageByWorker())
            {
                if(result.getImage().equalsIgnoreCase(image))
                {
                    imageId = result.getId();
                }
            }
        }

        Map<String,String> map = new HashMap<>();
        map.put("id",imageId);

        Call<SuccessResCheckboxChecked> call = apiInterface.removeImage(map);
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
                        showToast(QuestinariesAct.this, data.getMessage());
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

    private void getPhotoFromGallary() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Image"), SELECT_FILE);
    }

    private void openCamera() {

//        File dirtostoreFile = new File(Environment.getExternalStorageDirectory() + "/Starnz/Images/");
//
//        if (!dirtostoreFile.exists()) {
//            dirtostoreFile.mkdirs();
//        }
//
//        String timestr = DataManager.getInstance().convertDateToString(Calendar.getInstance().getTimeInMillis());
//
//        File tostoreFile = new File(Environment.getExternalStorageDirectory() + "/Stanrz/Images/" + "IMG_" + timestr + ".jpg");
//
//        str_image_path = tostoreFile.getPath();
//
//        uriSavedImage = FileProvider.getUriForFile(QuestinariesAct.this,
//                BuildConfig.APPLICATION_ID + ".provider",
//                tostoreFile);
//
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
//
//        startActivityForResult(intent, REQUEST_CAMERA);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(QuestinariesAct.this.getPackageManager()) != null)
            startActivityForResult(cameraIntent, REQUEST_CAMERA);

    }

    int PICK_IMAGE_MULTIPLE = 1;
    String imageEncoded;
    List<String> imagesEncodedList;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Log.e("Result_code", requestCode + "");
            if (requestCode == SELECT_FILE) {

                try {
                    Uri selectedImage = data.getData();
                    Bitmap bitmapNew = MediaStore.Images.Media.getBitmap(QuestinariesAct.this.getContentResolver(), selectedImage);
                    Bitmap bitmap = BITMAP_RE_SIZER(bitmapNew, bitmapNew.getWidth(), bitmapNew.getHeight());
                    Uri tempUri = getImageUri(QuestinariesAct.this, bitmap);
                    String image = RealPathUtil.getRealPath(QuestinariesAct.this, tempUri);
                    str_image_path = image;

                    imagesList.add(str_image_path);

                    multipleImagesAdapter.notifyDataSetChanged();

                } catch (IOException e) {
                    Log.i("TAG", "Some exception " + e);
                }

            } else if (requestCode == REQUEST_CAMERA) {

                try {
                    if (data != null) {
                        Bundle extras = data.getExtras();
                        Bitmap bitmapNew = (Bitmap) extras.get("data");
                        Bitmap imageBitmap = BITMAP_RE_SIZER(bitmapNew, bitmapNew.getWidth(), bitmapNew.getHeight());
                        Uri tempUri = getImageUri(QuestinariesAct.this, imageBitmap);
                        String image = RealPathUtil.getRealPath(QuestinariesAct.this, tempUri);
                        str_image_path = image;
                        imagesList.add(str_image_path);
                        multipleImagesAdapter.notifyDataSetChanged();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        //Multiple IMage picker 1

//        try {
//            // When an Image is picked
//            if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK
//                    && null != data) {
//                // Get the Image from data
//
//                String[] filePathColumn = { MediaStore.Images.Media.DATA };
//                imagesEncodedList = new ArrayList<String>();
//                if(data.getData()!=null){
//
//                    Uri mImageUri=data.getData();
//
//                    // Get the cursor
//                    Cursor cursor = getContentResolver().query(mImageUri,
//                            filePathColumn, null, null, null);
//                    // Move to first row
//                    cursor.moveToFirst();
//
//                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                    imageEncoded  = cursor.getString(columnIndex);
//                    cursor.close();
//                    imagesList.add(imageEncoded);
//                    multipleImagesAdapter.notifyDataSetChanged();
//
//                } else {
//                    if (data.getClipData() != null) {
//                        ClipData mClipData = data.getClipData();
//                        ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
//                        for (int i = 0; i < mClipData.getItemCount(); i++) {
//
////                            ClipData.Item item = mClipData.getItemAt(i);
////                            Uri uri = item.getUri();
////                            mArrayUri.add(uri);
////                            // Get the cursor
////                            Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
////                            // Move to first row
////                            cursor.moveToFirst();
////
////                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
////                            imageEncoded  = cursor.getString(columnIndex);
////                            imagesEncodedList.add(imageEncoded);
////                            cursor.close();
//
//
//                            ClipData.Item item = mClipData.getItemAt(i);
//                            Uri uri = item.getUri();
//
//                            Uri selectedImage = Uri.fromFile(new File(String.valueOf(uri)));
//                            Bitmap bitmapNew = null;
//                            try {
//                                bitmapNew = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                            Bitmap bitmap = BITMAP_RE_SIZER(bitmapNew, bitmapNew.getWidth(), bitmapNew.getHeight());
//                            Uri tempUri = getImageUri(QuestinariesAct.this, bitmap);
//                            String image = RealPathUtil.getRealPath(QuestinariesAct.this, tempUri);
//                            imagesList.add(image);
//                        }
//
//                        for (String selected: imagesEncodedList)
//                        {
//                            Uri selectedImage = Uri.fromFile(new File(selected));
//                            Bitmap bitmapNew = null;
//                            try {
//                                bitmapNew = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                            Bitmap bitmap = BITMAP_RE_SIZER(bitmapNew, bitmapNew.getWidth(), bitmapNew.getHeight());
//                            Uri tempUri = getImageUri(QuestinariesAct.this, bitmap);
//                            String image = RealPathUtil.getRealPath(QuestinariesAct.this, tempUri);
//                            imagesList.add(image);
//                        }
//
//                        multipleImagesAdapter.notifyDataSetChanged();
//                        Log.v("LOG_TAG", "Selected Images" + mArrayUri.size());
//                    }
//                }
//            } else {
//                Toast.makeText(this, "You haven't picked Image",
//                        Toast.LENGTH_LONG).show();
//            }
//        } catch (Exception e) {
//            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
//                    .show();
//        }


    }

    public Bitmap BITMAP_RE_SIZER(Bitmap bitmap, int newWidth, int newHeight) {
        Bitmap scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);

        float ratioX = newWidth / (float) bitmap.getWidth();
        float ratioY = newHeight / (float) bitmap.getHeight();
        float middleX = newWidth / 2.0f;
        float middleY = newHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bitmap, middleX - bitmap.getWidth() / 2, middleY - bitmap.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

        return scaledBitmap;

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title_" + System.currentTimeMillis(), null);
        return Uri.parse(path);
    }

    //CHECKING FOR Camera STATUS
    public boolean checkPermisssionForReadStorage() {
        if (ContextCompat.checkSelfPermission(QuestinariesAct.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED

                ||

                ContextCompat.checkSelfPermission(QuestinariesAct.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
                ||

                ContextCompat.checkSelfPermission(QuestinariesAct.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
        ) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(QuestinariesAct.this,
                    Manifest.permission.CAMERA)

                    ||

                    ActivityCompat.shouldShowRequestPermissionRationale(QuestinariesAct.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                    ||
                    ActivityCompat.shouldShowRequestPermissionRationale(QuestinariesAct.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
            ) {
                requestPermissions(
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSION_CONSTANT);
            } else {
                //explain("Please Allow Location Permission");
                // No explanation needed, we can request the permission.
                requestPermissions(
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSION_CONSTANT);
            }
            return false;
        } else {
            //  explain("Please Allow Location Permission");
            return true;
        }
    }

    private void updateArrivalTime(String taskId) {

        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

        String userId = SharedPreferenceUtility.getInstance(QuestinariesAct.this).getString(USER_ID);

        DataManager.getInstance().showProgressMessage(QuestinariesAct.this, getString(R.string.please_wait));
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
                        Intent i = new Intent(QuestinariesAct.this, HomeAct.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    } else if (data.status.equalsIgnoreCase("0")) {
                        showToast(QuestinariesAct.this, data.getMessage());
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