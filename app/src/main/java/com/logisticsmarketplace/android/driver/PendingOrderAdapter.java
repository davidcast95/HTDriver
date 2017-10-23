package com.logisticsmarketplace.android.driver;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.logisticsmarketplace.android.driver.Lihat_Pesanan.Base.JobOrderAdapter;
import com.logisticsmarketplace.android.driver.Model.JobOrder.JobOrderData;

import java.util.List;


public class PendingOrderAdapter extends JobOrderAdapter {

    public PendingOrderAdapter(Context context, int layout, List<JobOrderData> list) {
        super(context, layout, list);
    }
}
