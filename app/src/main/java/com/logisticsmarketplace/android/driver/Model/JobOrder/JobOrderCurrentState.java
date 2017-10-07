package com.logisticsmarketplace.android.driver.Model.JobOrder;

import com.google.gson.annotations.SerializedName;


public class JobOrderCurrentState {
    @SerializedName("longitude")
    public String longitude;

    @SerializedName("latitude")
    public String latitude;

    @SerializedName("state_name")
    public String state_name;

    @SerializedName("posting_date")
    public String posting_date;

    @SerializedName("name")
    public String name;
}
