package com.logisticsmarketplace.android.driver.Lihat_Pesanan.Done;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.logisticsmarketplace.android.driver.Lihat_Pesanan.Base.JobOrderAdapter;
import com.logisticsmarketplace.android.driver.Model.JobOrder.JobOrderData;
import com.logisticsmarketplace.android.driver.R;

import java.util.List;

/**
 * Created by Kristoforus Gumilang on 8/24/2017.
 */

public class OrderPendingAdapter extends JobOrderAdapter {

    public OrderPendingAdapter(Context context, int layout, List<JobOrderData> list) {
        super(context, layout, list);
    }
}
