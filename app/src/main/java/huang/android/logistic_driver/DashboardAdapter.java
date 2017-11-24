package huang.android.logistic_driver;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import huang.android.logistic_driver.Model.JobOrder.JobOrderData;

import java.util.List;


public class DashboardAdapter extends ArrayAdapter<JobOrderData> {
    private Context context;
    private List<JobOrderData> list;

    public DashboardAdapter(Context context, int layout, List<JobOrderData> list) {
        super(context, layout,list);
        this.context=context;
        this.list=list;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View view = convertView;
        if(convertView==null){
            LayoutInflater inflater= (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=inflater.inflate(R.layout.fragment_dashboard_list,parent,false);
        }

        return view;
    }

}
