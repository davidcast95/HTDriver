package com.logisticsmarketplace.android.driver.GPSActivity;

import android.location.Location;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;

/**
 * Created by davidwibisono on 23/10/17.
 */

public class GPSServices {

    public static GoogleApiClient mGoogleApiClient;
    public static LocationRequest mLocationRequest;
    public static Location mLastLocation;
}
