package com.example.sherawat42.whereami.Activities;

import android.Manifest;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.example.sherawat42.whereami.MyDataTypes.LocationReminderObject;
import com.example.sherawat42.whereami.MyDataTypes.MyLocation;
import com.example.sherawat42.whereami.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class AddLocationActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    GoogleMap mMap;
    Marker marker;
    EditText locationNameET;
    EditText locationTypeET;
    GoogleApiClient mGoogleApiClient;
    SupportMapFragment mapFragment;
    LatLng selectedLocation;
    List<LocationReminderObject> dataset;
    List<MyLocation> my_locs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
        mapFragment.getMapAsync(this);
        locationNameET = (EditText) findViewById(R.id.loc_name);
        locationTypeET = (EditText) findViewById(R.id.loc_type);
        selectedLocation = new LatLng(-1.0,-1.0);
        dataset = new Select().from(LocationReminderObject.class).execute();
        my_locs = new Select().from(MyLocation.class).execute();
    }

//    @Override
//    protected void onPause() {
//        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, (LocationListener) this);
//        mGoogleApiClient.disconnect();
//        super.onPause();
//    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
            ActivityCompat.requestPermissions(this, permissions, 1);
            return;
        }
        mMap.setMyLocationEnabled(true);

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                Toast.makeText(mapFragment.getActivity() , "Please Drag the red marker to the location to be chosen!!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker mMarker) {
                marker = mMarker;
                LatLng position = mMarker.getPosition();
                selectedLocation = position;
                double longitude = position.longitude;
                double latitude = position.latitude;
                Geocoder gc = new Geocoder(getApplicationContext());
                if (gc.isPresent()) {

                    List<Address> fromLocation = null;
                    try {
                        fromLocation = gc.getFromLocation(latitude, longitude, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (fromLocation != null) {
                        if (!fromLocation.isEmpty()) {
                            Address ad = fromLocation.get(0);
                            String locality = ad.getLocality();
                            mMarker.setSnippet(locality);
                        }
                    }
                }

            }
        });

    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }


    private Location mLastKnownLocation = null;

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
            ActivityCompat.requestPermissions(this, permissions, 1);
            return;
        }
        mLastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        //to display the last known location
        Toast.makeText(this, String.valueOf(mLastKnownLocation), Toast.LENGTH_SHORT).show();
        startLocationUpdates();
        //code to initialize the marker with current location!
        if (mLastKnownLocation != null) {
            LatLng latLng = new LatLng(mLastKnownLocation.getLatitude(),mLastKnownLocation.getLongitude());
            marker = mMap.addMarker(new MarkerOptions().position(latLng).title("Drag the marker to location"));
            marker.setDraggable(true);
            CameraUpdate cam = CameraUpdateFactory.newLatLngZoom(latLng, 15);
            mMap.animateCamera(cam);
            selectedLocation = latLng;
        }else{
            LatLng latLng = new LatLng(28.5921,77.0460);
            marker = mMap.addMarker(new MarkerOptions().position(latLng).title("Drag the marker to location"));
            marker.setDraggable(true);
            CameraUpdate cam = CameraUpdateFactory.newLatLngZoom(latLng, 15);
            mMap.animateCamera(cam);
            selectedLocation = latLng;
        }
    }

    boolean fromGpsSetting = false;

    private void startLocationUpdates() {
        final LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10000)
                .setFastestInterval(5000);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
            ActivityCompat.requestPermissions(this, permissions, 1);
            return;
        }

        LocationSettingsRequest.Builder builder=new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                int statusCode = locationSettingsResult.getStatus().getStatusCode();
                if(statusCode== LocationSettingsStatusCodes.RESOLUTION_REQUIRED)
                {
                    fromGpsSetting = true;
                    try {
                        locationSettingsResult.getStatus().startResolutionForResult(AddLocationActivity.this,1);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Toast.makeText(AddLocationActivity.this, String.valueOf(location), Toast.LENGTH_SHORT).show();
                LatLng mylatlng = new LatLng(location.getLatitude(),location.getLongitude());
                    for(int j=0;j<my_locs.size();j++){
                        LatLng latLng = new LatLng(my_locs.get(j).getLatitude(), my_locs.get(j).getLongitude());
                        float[] results = new float[10];
                        Location.distanceBetween(latLng.latitude,latLng.longitude, mylatlng.latitude, mylatlng.longitude, results);
                        if(results[0] < 1000 ){
                            Toast.makeText(getApplicationContext(), "You are near" + my_locs.get(j).getName(), Toast.LENGTH_SHORT);
                        }
                    }
            }
        });
    }


    boolean statusOfGPS;

    @Override
    protected void onResume() {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE );
        statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        super.onResume();
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastKnownLocation = location;
    }

    //called on clicking submit button
    public void submitLocation(View view) {
        String loc_name = locationNameET.getText().toString();
        String loc_type = locationTypeET.getText().toString();

        if(loc_name.isEmpty()){
            Toast.makeText(this,"Please add Location Type",Toast.LENGTH_SHORT).show();
            return;
        }else if(loc_type.isEmpty()) {
            Toast.makeText(this, "Please add Location Type", Toast.LENGTH_SHORT).show();
            return;
        }
        Double mLattitude = selectedLocation.latitude;
        Double mLongitude = selectedLocation.longitude;
//        MyLocation myLoc = new MyLocation(loc_name, loc_type, mLattitude, mLongitude);
        MyLocation myLoc = new MyLocation();
        myLoc.longitude = mLongitude;
        myLoc.latitude = mLattitude;
        myLoc.name = loc_name;
        myLoc.type = loc_type;
        try{
            myLoc.save();
            Toast.makeText(this, "Location successfully saved!!", Toast.LENGTH_LONG).show();
            finish();
        }catch (Exception e){
            e.printStackTrace();
        }
//        System.out.println("test");
    }
}
