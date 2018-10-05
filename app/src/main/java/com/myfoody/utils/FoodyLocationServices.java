package com.myfoody.utils;


import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

public class FoodyLocationServices implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    public static final String TAG = FoodyLocationServices.class.getSimpleName();
    private static FoodyLocationServices locationServices;

    private GoogleApiClient googleApiClient;
    private boolean isConnectedToGoogleApiClient;

    private FoodyLocationServices() {
    }

    public static FoodyLocationServices getInstance(Context context) {
        if (locationServices == null) {
            synchronized (FoodyLocationServices.class) {
                if (locationServices == null) {
                    locationServices = new FoodyLocationServices();
                    locationServices.googleApiClient = new GoogleApiClient.Builder(context)
                            .addApi(LocationServices.API)
                            .addConnectionCallbacks(locationServices)
                            .addOnConnectionFailedListener(locationServices)
                            .build();
                }
            }
        }

        return locationServices;
    }

    public void connectToGoogleClientApi() {
        if (!locationServices.googleApiClient.isConnected()
                && !locationServices.googleApiClient.isConnecting()) {
            locationServices.googleApiClient.connect();
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        isConnectedToGoogleApiClient = true;
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        isConnectedToGoogleApiClient = false;
    }

    @Override
    public void onConnectionSuspended(int cause) {
        isConnectedToGoogleApiClient = false;
        Log.e("", "Callback onConnectionSuspended : " + cause);
    }

    public Location getCurrentLocation() {
        while (!isConnectedToGoogleApiClient) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        if (location != null) {
            Log.e("", "Lat:" + location.getLatitude() + " Long: " + location.getLongitude());
        } else {
            Log.e("", "getCurrentLocation() is returning null");
        }

        return location;
    }
}
