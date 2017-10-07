package com.logisticsmarketplace.android.driver.Model.Driver;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kgumi on 24-Jul-17.
 */

public class Driver {
    @SerializedName("name")
    public String name;
    @SerializedName("nama")
    public String nama;
    @SerializedName("phone")
    public String phone;
    @SerializedName("address")
    public String address;
    @SerializedName("email")
    public String email;
    @SerializedName("vendor")
    public String vendor;
    @SerializedName("docstatus")
    public int docstatus;
}
