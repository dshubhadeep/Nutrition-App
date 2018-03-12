package com.example.smith.epsilonhealth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

public class LoginActivity extends AppCompatActivity {

    AnimationDrawable animationDrawable;
    APIRequestMethods mAPIRequestMethods;
    EditText user,pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.gradientLayout);
        animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(5000);
        animationDrawable.setExitFadeDuration(2000);
        mAPIRequestMethods = new APIRequestMethods();
    }

    public void login(View view){
        user= (EditText)findViewById(R.id.username);
        pass = (EditText)findViewById(R.id.password);
        mAPIRequestMethods.login(user.getText().toString(), pass.getText().toString(), new APIRequestMethods.OnCompleteListener() {
            @Override
            public void onLoginSucess(String message) {
                super.onLoginSucess(message);
                SharedPreferences sharedPreferences = getSharedPreferences("myshrd",0);
                sharedPreferences.edit().putString("username",user.getText().toString()).apply();
                Intent intent = new Intent(getApplicationContext(),FoodInput.class );
                startActivity(intent);
            }

            @Override
            public void onFailure(String msg) {
                super.onFailure(msg);
            }
        });
    }
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (!animationDrawable.isRunning()) {
            animationDrawable.start();
        }
    }

}
