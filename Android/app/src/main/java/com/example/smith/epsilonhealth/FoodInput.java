package com.example.smith.epsilonhealth;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.robinhood.spark.SparkAdapter;
import com.robinhood.spark.SparkView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class FoodInput extends AppCompatActivity {
    private static final int TAKE_PICTURE = 1;
    private Uri imageUri;
    APIRequestMethods mAPIRequestMethods;
    String uri;
    private static final String TAG = "FoodInput";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_input);
        mAPIRequestMethods = new APIRequestMethods();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        SparkView sparkView = (SparkView) findViewById(R.id.sparkview);
        float arrf[] = {0.1f,0.5f,0.2f,1.2f,1.3f,0.89f,1.3f};
        sparkView.setAdapter(new MyAdapter(arrf));
    }

    public void takePhoto(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photo = new File(Environment.getExternalStorageDirectory(), "Pic.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(photo));
        imageUri = Uri.fromFile(photo);
        startActivityForResult(intent, TAKE_PICTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PICTURE:
                if (resultCode == Activity.RESULT_OK) {
                    Uri selectedImage = imageUri;
                    getContentResolver().notifyChange(selectedImage, null);
                    ContentResolver cr = getContentResolver();
                    Bitmap bitmap;
                    try {
                        bitmap = android.provider.MediaStore.Images.Media
                                .getBitmap(cr, selectedImage);
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byte[] byteArray = stream.toByteArray();
                        final String encodeToString = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        String path =  selectedImage.toString().substring(5);
                        Log.d(TAG, "onActivityResult: "+selectedImage.toString());
                        File file = new File(path);

                        if (file != null) {
                            Toast.makeText(this, "File not null", Toast.LENGTH_SHORT).show();
                        }
                        RequestBody requestFile =
                                RequestBody.create(MediaType.parse("multipart/form-data"), file);
                        MultipartBody.Part body =
                                MultipartBody.Part.createFormData("Image", file.getName(), requestFile);
                        mAPIRequestMethods.sendImage(body, requestFile, new APIRequestMethods.OnCompleteListener() {
                            @Override
                            public void onFailure(String msg) {
                                super.onFailure(msg);
                                Toast.makeText(FoodInput.this, "file not uploaded", Toast.LENGTH_SHORT).show();
                            }
                        });
                        Toast.makeText(this, selectedImage.toString(),
                                Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(this, "Failed to load", Toast.LENGTH_SHORT)
                                .show();
                        Log.e("Camera", e.toString());
                    }
                }
        }
    }

    public void searchText(View view) {
       Intent intent = new Intent(this,SearchActivity.class);
       startActivity(intent);
    }

    public class MyAdapter extends SparkAdapter {
        private float[] yData;

        public MyAdapter(float[] yData) {
            this.yData = yData;
        }

        @Override
        public int getCount() {
            return yData.length;
        }

        @Override
        public Object getItem(int index) {
            return yData[index];
        }

        @Override
        public float getY(int index) {
            return yData[index];
        }
    }
}
