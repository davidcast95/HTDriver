package com.logisticsmarketplace.android.driver.Model.BackgroundData;

import com.google.gson.annotations.SerializedName;

/**
 * Created by davidwibisono on 23/10/17.
 */

public class BackgroundData {
    @SerializedName("lo")
    public String lo;
    @SerializedName("lat")
    public String lat;
    @SerializedName("batery")
    public String battery;
    @SerializedName("signal")
    public String signal;
    @SerializedName("last_update")
    public String last_update;
    @SerializedName("driver")
    public String driver;
    @SerializedName("status_gps")
    public String status_gps;
}
