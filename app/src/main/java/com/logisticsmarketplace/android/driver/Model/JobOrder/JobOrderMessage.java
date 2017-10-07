package com.logisticsmarketplace.android.driver.Model.JobOrder;

import com.logisticsmarketplace.android.driver.Model.Default.DataMessage;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Kristoforus Gumilang on 8/24/2017.
 */

public class JobOrderMessage extends DataMessage {
    @SerializedName("data")
    public List<JobOrderData> data;
}