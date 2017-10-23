package com.logisticsmarketplace.android.driver.Lihat_Pesanan.Base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.logisticsmarketplace.android.driver.Model.JobOrder.JobOrderData;
import com.logisticsmarketplace.android.driver.R;

import java.util.List;

/**
 * Created by davidwibisono on 20/10/17.
 */

public class JobOrderAdapter extends ArrayAdapter<JobOrderData> {
    private Context context;
    private List<JobOrderData> list;

    public JobOrderAdapter(Context context, int layout, List<JobOrderData> list) {
        super(context, layout,list);
        this.context=context;
        this.list=list;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View view = convertView;
        if(convertView==null){
            LayoutInflater inflater= (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=inflater.inflate(R.layout.default_order_list,parent,false);
        }

        TextView joid = (TextView)view.findViewById(R.id.joid);
        TextView origin = (TextView) view.findViewById(R.id.origin);
        TextView destination = (TextView)view.findViewById(R.id.destination);
        TextView ref = (TextView)view.findViewById(R.id.ref_id);

        ref.setText("Ref No : " + list.get(position).ref);
        joid.setText(list.get(position).joid);
        origin.setText(list.get(position).origin);
        destination.setText(list.get(position).destination);


        return view;
    }
}
