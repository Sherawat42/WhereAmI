package com.example.sherawat42.whereami;

import android.Manifest;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.activeandroid.query.Select;
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

import java.util.List;

public class ShowAllMapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    GoogleMap mMap;
    Marker marker;
    GoogleApiClient mGoogleApiClient;
    SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
            ActivityCompat.requestPermissions(this, permissions, 1);
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    private Location mLastKnownLocation = null;


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

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
            ActivityCompat.requestPermissions(this, permissions, 1);
            return;
        }
        mLastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        Toast.makeText(this, String.valueOf(mLastKnownLocation), Toast.LENGTH_SHORT).show();
        startLocationUpdates();
        //code to initialize the marker with current location!
        if (mLastKnownLocation != null) {
            LatLng latLng = new LatLng(mLastKnownLocation.getLatitude(),mLastKnownLocation.getLongitude());
            CameraUpdate cam = CameraUpdateFactory.newLatLngZoom(latLng, 13);
            mMap.animateCamera(cam);
        }else{
            LatLng latLng = new LatLng(28.5921,77.0460);
            CameraUpdate cam = CameraUpdateFactory.newLatLngZoom(latLng, 13);
            mMap.animateCamera(cam);
        }
        setTheMarkers();
    }

    List<LocationReminderObject> dataset;
    List<MyLocation> my_locs;
    private void setTheMarkers(){
        dataset = new Select().from(LocationReminderObject.class).execute();
        my_locs = new Select().from(MyLocation.class).execute();
        for(int i=0;i<dataset.size();i++){
            LocationReminderObject myObj = dataset.get(i);
            String loc_type = myObj.getLoc_type();
            for(int j=0;j<my_locs.size();j++){
                String temp = my_locs.get(j).getType();
                if(loc_type.equals(temp)){
                    LatLng latLng = new LatLng(my_locs.get(j).getLatitude(), my_locs.get(j).getLongitude());
                    marker = mMap.addMarker(new MarkerOptions().position(latLng).title(myObj.getDescription()));
                }
            }
        }
    }

    boolean statusOfGPS;
    boolean fromGpsSetting = false;

    @Override
    protected void onResume() {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE );
        statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        super.onResume();
    }


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
        final PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                int statusCode = locationSettingsResult.getStatus().getStatusCode();
                if(statusCode== LocationSettingsStatusCodes.RESOLUTION_REQUIRED)
                {
                    fromGpsSetting = true;
                    try {
                        locationSettingsResult.getStatus().startResolutionForResult(ShowAllMapsActivity.this,1);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Toast.makeText(ShowAllMapsActivity.this, String.valueOf(location), Toast.LENGTH_SHORT).show();
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

}
