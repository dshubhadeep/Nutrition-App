package com.example.smith.epsilonhealth;

import android.os.Bundle;
import android.view.View.OnKeyListener;
import android.view.View;
import android.view.KeyEvent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.smith.epsilonhealth.DataModels.SearchItems;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchActivity extends AppCompatActivity {
    private static final String TAG = "SearchActivity";
    private APIRequestMethods mAPIRequestMethods;
    public static final String BASE_URL = "https://api.learn2crack.com";
    private RecyclerView mRecyclerView;
    private ArrayList<SearchItems> mArrayList;
    private DataAdapter mAdapter;
    private EditText foodname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        foodname = (EditText) findViewById(R.id.foodNameEd);
        mAPIRequestMethods = new APIRequestMethods();
        initViews();
    }
    private void initViews() {
        mRecyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);
        mRecyclerView.setAdapter(mAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
    }
    public void search(View view){
        String e = foodname.getText().toString();
        mAPIRequestMethods.sendText(e, new APIRequestMethods.OnCompleteListener() {
            @Override
            public void onSuccess(ArrayList labelArray,ArrayList uriArray) {
                super.onSuccess(labelArray,uriArray);
                mAdapter = new DataAdapter(getApplicationContext(),labelArray,uriArray);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                mRecyclerView.setLayoutManager(layoutManager);
                mAdapter.notifyDataSetChanged();
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(String msg) {
                super.onFailure(msg);
            }
        });
    }

}