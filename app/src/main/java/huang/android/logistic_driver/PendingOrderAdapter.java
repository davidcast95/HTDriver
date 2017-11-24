package huang.android.logistic_driver;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import huang.android.logistic_driver.Lihat_Pesanan.Base.JobOrderAdapter;
import huang.android.logistic_driver.Model.JobOrder.JobOrderData;

import java.util.List;


public class PendingOrderAdapter extends JobOrderAdapter {

    public PendingOrderAdapter(Context context, int layout, List<JobOrderData> list) {
        super(context, layout, list);
    }
}
