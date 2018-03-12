package com.example.smith.epsilonhealth;

import android.util.Log;
import android.widget.Toast;

import com.example.smith.epsilonhealth.Interface.EpsilonApiInterface;
import com.example.smith.epsilonhealth.Retrofit.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by SMITH on 25-Jan-18.
 */

public class APIRequestMethods {
    private EpsilonApiInterface apiInterface;
    private static final String TAG = "APIRequestMethods";

    public APIRequestMethods() {
        Log.d(TAG, "APIRequestMethods: called");
        apiInterface = Utils.getInterface();
    }

    public void login(String username, String password, final OnCompleteListener listener) {

        Log.d(TAG, "login: called");
        Call call = apiInterface.attemptLogin(username,password);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                ResponseBody responseBody = (ResponseBody)response.body();
                try {
                    Log.d(TAG, "onResponse: "+responseBody.toString());
                    String s = responseBody.string();
                    Log.d(TAG, "onResponse: s"+s);
                    JSONObject ob = new JSONObject(s);
                    Log.d(TAG, "onResponse: jsonOb "+ob.toString());
                    Log.d(TAG, "onResponse: "+ob.getString("success"));
                    if(ob.getString("success").equals("true")){
                        listener.onLoginSucess("Success");
                    }
                    else{
                        listener.onFailure("failure");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });

    }

    public void sendImage(MultipartBody.Part file, RequestBody body, final OnCompleteListener listener) {
        Call call = apiInterface.uploadImage(file, body);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Log.d(TAG, "onResponse: " + response.body());
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                listener.onFailure(t.getMessage());
            }
        });
    }

    public void sendText(String foodname, final OnCompleteListener listener) {
        Call call = apiInterface.foodSearch(foodname);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                ResponseBody responseBody = (ResponseBody) response.body();
                try {
                    Log.d(TAG, "onResponse: " + response.body().toString());
                    JSONArray arr = new JSONArray(responseBody.string());

                    ArrayList arrayLabel = new ArrayList();
                    ArrayList arrayUri= new ArrayList();

                    for(int i=0;i<arr.length();i++){
                        String label = arr.getJSONObject(i).getString("label");
                        arrayLabel.add(label);
                        String uri = arr.getJSONObject(i).getString("uri");
                        arrayUri.add(uri);
                        Log.d(TAG, "onResponse: "+arrayUri.get(i));
                    }
                    listener.onSuccess(arrayLabel,arrayUri);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }

    public void getFoodDetails(String uri, final OnCompleteListener listener) {
        Call call = apiInterface.getFoodDetails(uri);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject data = new JSONObject(response.body().string());
                    JSONObject js = data.getJSONObject("data");
                    Log.d(TAG, "onResponse: " + js.getString("food"));
                    String foodname = js.getString("food");
                    Log.d(TAG, "onResponse: " + foodname);
                    String cal = js.getString("cal");
                    JSONObject object = js.getJSONObject("nutrients");
                    String messagefound = object.getString("message");
                    ArrayList a = new ArrayList();
                    if (messagefound.equals("Nutrients found")) {
                        a.add(object.getString("FAT"));
                        a.add(object.getString("CARBS"));
                        a.add(object.getString("FIBER"));
                        a.add(object.getString("PROTEIN"));
                        listener.onSuccessgGetFoodDetails(foodname, a, cal);
                    } else {
                        listener.onFailure("Nutrients not found");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }

    public void addMeals(JSONObject json){
        Call call = apiInterface.addMeal(json);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }

    public abstract static class OnCompleteListener {
        public void onLoginSucess(String message){}

        public void onSuccess(ArrayList labelArray,ArrayList uriArray) {
        }

        public void onSuccessgGetFoodDetails(String foodname, ArrayList nutrients, String cal) {
        }

        public void onFailure(String msg) {
        }

    }
}

