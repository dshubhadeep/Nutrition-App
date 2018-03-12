package com.example.smith.epsilonhealth;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.smith.epsilonhealth.DataModels.SearchItems;

import java.util.ArrayList;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList labelArr;
    private ArrayList uriArr;

    public DataAdapter(Context context, ArrayList arrayLabel, ArrayList arrayUri) {
        labelArr = arrayLabel;
        uriArr = arrayUri;
        mContext = context;
    }

    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataAdapter.ViewHolder viewHolder, final int i) {
        viewHolder.tv_name.setText(labelArr.get(i).toString());
        viewHolder.tv_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,HomeActivity.class);
                intent.putExtra("uri",uriArr.get(i).toString());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return uriArr.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_name, tv_version, tv_api_level;

        public ViewHolder(View view) {
            super(view);

            tv_name = (TextView) view.findViewById(R.id.tv_name);
        }
    }

}