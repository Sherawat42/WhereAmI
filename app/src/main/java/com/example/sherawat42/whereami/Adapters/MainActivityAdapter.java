package com.example.sherawat42.whereami.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sherawat42.whereami.MyDataTypes.LocationReminderObject;
import com.example.sherawat42.whereami.R;

import java.util.List;

import cyd.awesome.material.AwesomeText;

/**
 * Created by sherawat42 on 3/5/17.
 */

public class MainActivityAdapter extends RecyclerView.Adapter<MainActivityAdapter.ViewHolder> {
    public List<LocationReminderObject> myDataset;
    private int mExpandedPosition = -1;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public AwesomeText location_typeTV;
        public AwesomeText descriptionTV;
        public AwesomeText distanceTV;
        public ViewHolder(View v) {
            super(v);
            location_typeTV = (AwesomeText) v.findViewById(R.id.location_type_text_main);
            descriptionTV = (AwesomeText) v.findViewById(R.id.description);
            distanceTV = (AwesomeText) v.findViewById(R.id.distance);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MainActivityAdapter(List<LocationReminderObject> myDataset) {
        this.myDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MainActivityAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        // create a new view
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_list, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.descriptionTV.setText(myDataset.get(position).getDescription());
        holder.location_typeTV.setText(myDataset.get(position).getLoc_type());
        final boolean isExpanded = position==mExpandedPosition;
        holder.location_typeTV.setVisibility(isExpanded?View.VISIBLE:View.GONE);
//        holder.descriptionTV.setVisibility(isExpanded?View.VISIBLE:View.GONE);
        holder.itemView.setActivated(isExpanded);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExpandedPosition = isExpanded ? -1:position;
//                TransitionManager.beginDelayedTransition((RecyclerView)holder.itemView.getParent());
                notifyDataSetChanged();
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return myDataset.size();
    }
}
