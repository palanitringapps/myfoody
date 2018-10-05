package com.myfoody.home;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.myfoody.R;
import com.myfoody.base.BaseActivity;
import com.myfoody.utils.FoodyLocationServices;

import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class HomeActivity extends BaseActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationPermissionListener {

    private int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private LocationPermissionListener locationListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_locate_me);
        FoodyLocationServices.getInstance(getApplicationContext()).connectToGoogleClientApi();
        buildGoogleApiClient();
        checkLocation();
        checkPlayServices();
        findViewById(R.id.btn_locate_me).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocationPermission(HomeActivity.this);
            }
        });

    }


    public void setLocationPermission(LocationPermissionListener locationPermission) {
        this.locationListener = locationPermission;
        checkPermission(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION);
    }

    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }


    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (lastLocation != null) {
            Log.i("", lastLocation + " " + lastLocation.getLatitude() + " " + lastLocation.getLongitude());
            getCurrentZipcode(lastLocation.getLatitude(), lastLocation.getLongitude());
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        // Log.i(getResources().getString(R.string.location_connection_error) + result.getErrorCode());
    }


    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i("", "Connection suspended");
        googleApiClient.connect();
    }

    private void getCurrentZipcode(final double latitidue, final double longitude) {
        final Thread zipThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (Geocoder.isPresent()) {
                        Geocoder geocoder = new Geocoder(HomeActivity.this, Locale.ENGLISH);
                        List<Address> addresses = geocoder.getFromLocation(latitidue, longitude, 1);
                        Address address = addresses.get(0);
                        String returnAddress = address.getPostalCode();
                    } else {
                        Log.e("", "Geocode not available");
                    }
                } catch (Exception e) {
                    Log.e("", e.getMessage());
                }
            }
        });
        zipThread.start();
    }

    private void checkLocation() {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        boolean isNetworkEnable = false, isGpsEnable = false;

        try {
            isGpsEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            Log.e("", e.getMessage());
        }

        if (!isGpsEnable && !isNetworkEnable) {
            showLocationServiceAlert(this);
        } else {
        }
    }

    private void showLocationServiceAlert(final Activity context) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(context.getResources().getString(R.string.location_alert_title));
        dialog.setMessage(context.getResources().getString(R.string.location_alert_message));
        dialog.setPositiveButton(context.getResources().getString(R.string.location_setting), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivityForResult(myIntent, REQUEST_CODE);
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //restoreSession();
            }
        });

        dialog.setCancelable(false);

        dialog.create().show();
    }

    @Override
    public void onPermissionGrant(String[] permissions, int PERMISSION_REQUEST_CODE) {
        super.onPermissionGrant(permissions, PERMISSION_REQUEST_CODE);

        if (locationListener != null) {
            locationListener.locationPermission();
        }
    }

    @Override
    public void onPermissionDenied(String[] permissions, int PERMISSION_REQUEST_CODE) {
        super.onPermissionDenied(permissions, PERMISSION_REQUEST_CODE);
    }


    @Override
    public void locationPermission() {
        googleApiClient.connect();
    }

    private static final int REQUEST_CODE = 1;
    protected GoogleApiClient googleApiClient;
    protected Location lastLocation;
    public static final int LOCATION_PERMISSION = 5;
}
