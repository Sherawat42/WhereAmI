package com.example.sherawat42.whereami.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sherawat42.whereami.MyDataTypes.LocationReminderObject;
import com.example.sherawat42.whereami.R;

import cyd.awesome.material.AwesomeText;

//import static com.example.sherawat42.whereami.Activities.AddLocationActivity.SelectLocationActivity.LOCATION_DATA;

public class AddReminderActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                descriptionET = (EditText)findViewById(R.id.description);
                if(!locationChosen){
                Snackbar.make(view, "Please Choose a Location!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                    return;
                }else if(descriptionET.getText().toString().isEmpty()){
                    Snackbar.make(view, "Please enter some description!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    return;
                }
                LocationReminderObject locationReminderObject = new LocationReminderObject();
                locationReminderObject.description = descriptionET.getText().toString();
                locationReminderObject.loc_type = loc_type;
                locationReminderObject.save();
                Toast.makeText(getApplication(),"Location reminder successfully set", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }


    public void openSelectLocationActivity(View view) {
        Intent i = new Intent();
        i.setClass(this,SelectLocationActivity.class);
        startActivity(i);
        finish();
    }

    String loc_type = null;

    private boolean locationChosen = false;
    @Override
    protected void onResume() {
        super.onResume();
        Intent i = getIntent();
        loc_type = i.getStringExtra(SelectLocationActivity.LOCATION_DATA);

        if(loc_type != null){
            AwesomeText at = (AwesomeText)findViewById(R.id.location_type_text);
            at.setVisibility(View.VISIBLE);
            locationChosen = true;
            at.setText("Type of location chosen is "+loc_type);
        }
    }
    EditText descriptionET;

}
