package com.example.smith.epsilonhealth.Retrofit;

import com.example.smith.epsilonhealth.Interface.EpsilonApiInterface;

/**
 * Created by SMITH on 28-Jan-18.
 */

public class Utils {
    private Utils(){}
    public static String BASE_URL = "https://epsilon-c2c.herokuapp.com/";
    public static EpsilonApiInterface getInterface(){
        return RetrofitClient.getRetrofitClient(BASE_URL).create(EpsilonApiInterface.class);
    }
}
