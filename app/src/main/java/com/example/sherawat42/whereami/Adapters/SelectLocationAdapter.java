package com.example.sherawat42.whereami.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sherawat42.whereami.R;

import cyd.awesome.material.AwesomeButton;

/**
 * Created by sherawat42 on 5/5/17.
 */

public class SelectLocationAdapter extends RecyclerView.Adapter<SelectLocationAdapter.ViewHolder> {

    public interface ReturnToPreviousActivityHelper{
        public void locationChosen(String str);
    }

    ReturnToPreviousActivityHelper returnToPreviousActivityHelper;

    private String[] locationTypes;
    Context ctx;
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView locationTypeTV;
        public AwesomeButton chooseButton;
        public ViewHolder(View itemView) {
            super(itemView);
            locationTypeTV = (TextView)itemView.findViewById(R.id.location_type);
            chooseButton = (AwesomeButton)itemView.findViewById(R.id.chooseLocationButton);
        }
    }

    public SelectLocationAdapter(Context ctx, String[] locationTypes,ReturnToPreviousActivityHelper returnToPreviousActivityHelper){
        this.locationTypes = locationTypes;
        this.ctx = ctx;
        this.returnToPreviousActivityHelper = returnToPreviousActivityHelper;
    }


    @Override
    public SelectLocationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.choose_location_view, parent, false);
        SelectLocationAdapter.ViewHolder vh = new SelectLocationAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(SelectLocationAdapter.ViewHolder holder, final int position) {
        holder.locationTypeTV.setText(locationTypes[position]);
        holder.chooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToPreviousActivityHelper.locationChosen(locationTypes[position]);
            }
        });
    }

    @Override
    public int getItemCount() {
        return locationTypes.length;
    }

}
