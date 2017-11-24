package huang.android.logistic_driver.Lihat_Pesanan.Active;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import huang.android.logistic_driver.Lihat_Pesanan.Base.JobOrderAdapter;
import huang.android.logistic_driver.Model.JobOrder.JobOrderData;
import huang.android.logistic_driver.R;

import java.util.List;

/**
 * Created by Kristoforus Gumilang on 8/24/2017.
 */

public class OrderActiveAdapter extends JobOrderAdapter {

    public OrderActiveAdapter(Context context, int layout, List<JobOrderData> list) {
        super(context, layout, list);
    }
}
