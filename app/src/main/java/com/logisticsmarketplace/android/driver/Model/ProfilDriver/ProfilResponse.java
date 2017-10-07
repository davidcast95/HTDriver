package com.logisticsmarketplace.android.driver.Model.ProfilDriver;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by davidwibisono on 8/24/17.
 */

public class ProfilResponse {
    @SerializedName("data")
    public List<Profil> data;
}
