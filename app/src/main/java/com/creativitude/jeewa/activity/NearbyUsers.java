package com.creativitude.jeewa.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.creativitude.jeewa.R;
import com.creativitude.jeewa.helpers.Dialer;
import com.creativitude.jeewa.helpers.Transitions;
import com.creativitude.jeewa.models.Topic;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class NearbyUsers extends AppCompatActivity implements OnMapReadyCallback, AdapterView.OnItemSelectedListener, View.OnClickListener {

    private static final String TAG = "NearbyUsers";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 12f;

    private GoogleMap googleMap;


    private boolean mLocationPermissionGranted = false;
    private boolean statusCheck = false;

    private DatabaseReference topicRef;
    private DatabaseReference userRef;

    private ArrayList<Topic> markers;
    private Topic userDetails;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map is ready!", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: map is ready");
        this.googleMap = googleMap;

        if (mLocationPermissionGranted) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        }

        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

                String snip = marker.getSnippet();
                String[] snipSplit = new String[0];

                try{
                    snipSplit = snip.split("Phone Number : ");

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }

                Dialer dialer = new Dialer(getApplicationContext());
                dialer.dial(snipSplit[1]);
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_users);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Transitions.init(NearbyUsers.this);
        }

        Spinner mapSpinner = findViewById(R.id.map_spinner);
        CircleImageView gpsIcon = findViewById(R.id.map_gps);

        gpsIcon.setOnClickListener(this);

        mapSpinner.setOnItemSelectedListener(this);
        topicRef = FirebaseDatabase.getInstance().getReference("Topics");
        userRef = FirebaseDatabase.getInstance().getReference("Users");

        getLocationPermission();

    }

    private void initMap() {

        Log.d(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(NearbyUsers.this);
    }


    private void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: get the current location of the device");

        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {

            if(mLocationPermissionGranted) {
                Task location = fusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()) {

                                statusCheck();
                                Log.d(TAG, "onComplete: found location");

                                if (statusCheck) {

                                    Log.d(TAG, "onComplete: after status check inside if");
                                    Location currentLocation = (Location) task.getResult();

                                    Log.d(TAG, "onComplete: location: " + currentLocation );

                                    try {
                                        moveCamera(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()),
                                                DEFAULT_ZOOM);
                                    } catch (NullPointerException e) {
                                        Log.e(TAG, "onComplete: NullPointerException: " + e.getMessage() );
                                        Toast.makeText(getApplicationContext(),"Cannot find your current location. Please try again.",Toast.LENGTH_SHORT).show();
                                    }


                                } else {
                                    Toast.makeText(getApplicationContext(), R.string.turn_gps_on,Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Log.d(TAG, "onComplete: location not found");
                                Toast.makeText(getApplicationContext(), R.string.location_not_found,Toast.LENGTH_SHORT).show();
                            }
                    }
                });
            }

        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: location provide client initiate error" + e.getMessage() );

        }


    }

    private void moveCamera (LatLng latLng, float zoom) {

        Log.d(TAG, "moveCamera: moving the camera to : lat: " + latLng.latitude + ", long: " + latLng.longitude);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));

    }

    private void getLocationPermission() {

        Log.d(TAG, "getLocationPermission: getting location permission");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};


        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                mLocationPermissionGranted = true;
                initMap();

            } else {
                ActivityCompat.requestPermissions(NearbyUsers.this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE );
            }
        }

        else {
            ActivityCompat.requestPermissions(NearbyUsers.this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE );
        }
    }

    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        } else {
            statusCheck = true;
        }
    }


    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.get_gps)
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        statusCheck = false;
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        Log.d(TAG, "onRequestPermissionsResult: called");
        mLocationPermissionGranted = false;

        switch (requestCode) {

            case LOCATION_PERMISSION_REQUEST_CODE: {

                if (grantResults.length > 0) {

                    for (int grantResult : grantResults) {
                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionGranted = true;
                    initMap();
                }
            }
        }
    }


    @Override
    public void onItemSelected(final AdapterView<?> adapterView, View view, int pos, long l) {

        String selectedBg = adapterView.getItemAtPosition(pos).toString();

        Log.d(TAG, "onItemSelected: Selected Blood Group: " + selectedBg);

        if(pos !=0 ) {

            Log.d(TAG, "onItemSelected: inside if statement");

            DatabaseReference bgRef = topicRef.child(selectedBg);

            bgRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        markers = new ArrayList<>();
                        for (DataSnapshot user : dataSnapshot.getChildren()) {


                            userDetails = user.getValue(Topic.class);
                            Log.d(TAG, "onDataChange: user: " + userDetails);


                            if (userDetails.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                continue;
                            }

                            markers.add(userDetails);

                        }
                        addMarker(markers);
                    } else {
                        googleMap.clear();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private void addMarker(ArrayList<Topic> user) {

        googleMap.clear();

        for (int i = 0; i < user.size(); i++) {

            Topic marker = user.get(i);

            Log.d(TAG, "addMarker: init lat: " + marker.getHt_lat());
            Log.d(TAG, "addMarker: init long: " + marker.getHt_long());

            LatLng latLng1 = new LatLng(marker.getHt_lat(), marker.getHt_long());

            String snippet = "Phone Number : " + marker.getNumber();

//            googleMap.setInfoWindowAdapter(new CustomInfoWIndowAdapter(NearbyUsers.this));

            MarkerOptions options = new MarkerOptions()
                    .position(latLng1)
                    .title(marker.getName())
                    .snippet(snippet)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_person_pin));

            googleMap.addMarker(options);

        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {
        Log.d(TAG, "onClick: clicked gps icon");
        getDeviceLocation();
    }
}
