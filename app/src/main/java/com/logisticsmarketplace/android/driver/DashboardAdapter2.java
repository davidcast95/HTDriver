package com.logisticsmarketplace.android.driver;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.logisticsmarketplace.android.driver.Model.JobOrder.JobOrderData;

import java.util.List;

/**
 * Created by Kristoforus Gumilang on 8/24/2017.
 */

public class DashboardAdapter2 extends ArrayAdapter<JobOrderData> {
    private Context context;
    private List<JobOrderData> list;

    public DashboardAdapter2(Context context, int layout, List<JobOrderData> list) {
        super(context, layout,list);
        this.context=context;
        this.list=list;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View view = convertView;
        if(convertView==null){
            LayoutInflater inflater= (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=inflater.inflate(R.layout.fragment_dashboard_list2,parent,false);
        }

        return view;
    }

}
