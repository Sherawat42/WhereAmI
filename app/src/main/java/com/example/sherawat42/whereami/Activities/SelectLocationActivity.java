package com.example.sherawat42.whereami;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.activeandroid.query.Select;
import com.example.sherawat42.whereami.Adapters.SelectLocationAdapter;

import java.util.LinkedHashSet;
import java.util.List;

public class SelectLocationActivity extends AppCompatActivity implements SelectLocationAdapter.ReturnToPreviousActivityHelper{


    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        List<MyLocation> myLocationList = new Select().from(MyLocation.class).execute();

        LinkedHashSet<String> noDupSet = new LinkedHashSet();
        for(int i=0;i<myLocationList.size();i++){
            noDupSet.add(myLocationList.get(i).getType());
        }

        String stringList[] = new String[noDupSet.size()];
        int i=0;
        for (String s : noDupSet) {
            stringList[i] = s;
            i++;
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_for_choose_location);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
//        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new SelectLocationAdapter(this, stringList,this);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }


    public static final String LOCATION_DATA = "location_type";
    // this is for returning to previous activity with the result
    @Override
    public void locationChosen(String locationType) {
        Intent i = new Intent();
        i.setClass(this, AddReminderActivity.class);
        i.putExtra(LOCATION_DATA, locationType);
        startActivity(i);
        finish();
    }

}
