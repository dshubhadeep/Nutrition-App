package com.example.smith.epsilonhealth;

import android.content.SharedPreferences;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    APIRequestMethods mAPIRequestMethods;
    BottomSheetBehavior mBottomSheetBehavior;
    TextView foodnameTv,calsTv,carbs,proteins,fats,fiber;
    EditText quantityEd;
    int qua;
    String cals;
    ArrayList mNutr= new ArrayList();
    private static final String TAG = "HomeActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //View bottomsheet = findViewById(R.id.bottom_sheet1);
        //mBottomSheetBehavior = BottomSheetBehavior.from(bottomsheet);
       // mBottomSheetBehavior.setPeekHeight(64);

        String value = getIntent().getExtras().getString("uri");
        quantityEd = (EditText)findViewById(R.id.quantity);
        Log.d(TAG, "onCreate: "+value);
        foodnameTv = (TextView) findViewById(R.id.foodNametv);
        calsTv = (TextView)findViewById(R.id.cals);
        carbs = (TextView)findViewById(R.id.carbs);
        proteins = (TextView)findViewById(R.id.proteins);
        fats = (TextView)findViewById(R.id.fats);
        fiber = (TextView)findViewById(R.id.fiber);
        mAPIRequestMethods = new APIRequestMethods();
        mAPIRequestMethods.getFoodDetails(value, new APIRequestMethods.OnCompleteListener() {
            @Override
            public void onSuccessgGetFoodDetails(String foodname, ArrayList nutrients, String cal) {
                super.onSuccessgGetFoodDetails(foodname, nutrients, cal);
                mNutr=nutrients;
                Log.d(TAG, "onSuccessgGetFoodDetails: "+foodname);
                foodnameTv.setText(foodname);
                calsTv.setText(cal+" cals");
                cals=cal;
                carbs.setText(nutrients.get(1).toString()+" per 100gm");
                proteins.setText(nutrients.get(3).toString()+" per 100gm");
                fats.setText(nutrients.get(0).toString()+" per 100gm");
                fiber.setText(nutrients.get(2).toString()+" per 100gm");
            }

            @Override
            public void onFailure(String msg) {
                super.onFailure(msg);
            }
        });
    }
    public void eatThis(View view){
        double cal = Double.parseDouble(cals);
        double uq = Double.parseDouble(quantityEd.getText().toString());
        double nc = Double.parseDouble(mNutr.get(1).toString())*uq*3;
        double nfa= Double.parseDouble(mNutr.get(0).toString())*uq*3;
        double nfi = Double.parseDouble(mNutr.get(2).toString())*uq*3;
        double np = Double.parseDouble(mNutr.get(3).toString())*uq*3;
        SharedPreferences sharedPreferences = getSharedPreferences("myshrd",0);
        String s = sharedPreferences.getString("username","fuckoff");
        Log.d(TAG, "eatThis: "+s);
        String js= "{\"usr\": " + s +",\"cals\": "  +cal+  ",\"carbs\": "   +nc+   ",\"fats\": "    +nfa+    ",\"fiber\": "   +nfi+   ",\"proteins\": "   +np+"}";
        Log.d(TAG, "eatThis: "+js);
        try {
            JSONObject object = new JSONObject(js);
            Log.d(TAG, "eatThis: "+object.toString());
            mAPIRequestMethods.addMeals(object);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }




}
