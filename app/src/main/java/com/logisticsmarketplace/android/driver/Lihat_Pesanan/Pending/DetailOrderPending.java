package com.logisticsmarketplace.android.driver.Lihat_Pesanan.Pending;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.logisticsmarketplace.android.driver.API.API;
import com.logisticsmarketplace.android.driver.Dashboard;
import com.logisticsmarketplace.android.driver.MainActivity;
import com.logisticsmarketplace.android.driver.Model.JobOrder.JobOrderData;
import com.logisticsmarketplace.android.driver.Model.JobOrder.JobOrderStatus;
import com.logisticsmarketplace.android.driver.Model.MyCookieJar;
import com.logisticsmarketplace.android.driver.R;
import com.logisticsmarketplace.android.driver.Utility;

import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailOrderPending extends AppCompatActivity {

    LinearLayout layout;
    JobOrderData jobOrder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utility.utility.getLanguage(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_order_pending);

        setTitle(R.string.track_order_list);

        layout=(LinearLayout)findViewById(R.id.layout);

        Intent intent = getIntent();
        int index = intent.getIntExtra("index", 0);
        String from = intent.getStringExtra("from");
        if (from.equals("View Order")) {
            if (OrderPending.jobOrders.get(index) != null) {
                jobOrder = OrderPending.jobOrders.get(index);

            }
        } else {
            if (Dashboard.jobOrders.get(index) != null) {
                jobOrder = Dashboard.jobOrders.get(index);
            }
        }
        if (jobOrder != null) {
            TextView joid = (TextView) findViewById(R.id.joid);
            TextView origin = (TextView) findViewById(R.id.origin);
            TextView destination = (TextView) findViewById(R.id.destination);
            TextView principle_cp_name = (TextView) findViewById(R.id.principle_cp_name);
            TextView principle_cp_phone = (TextView) findViewById(R.id.principle_cp_phone);
            TextView cargoInfo = (TextView) findViewById(R.id.cargo_info);

            joid.setText(jobOrder.joid);
            origin.setText(jobOrder.origin);
            destination.setText(jobOrder.destination);
            principle_cp_name.setText(jobOrder.principle_cp_name);
            principle_cp_phone.setText(jobOrder.principle_cp_phone);
            cargoInfo.setText(jobOrder.cargoInfo);
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_checklist, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_yes:
                dialog();
                break;
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    void updateStateUI() {
        updateJobOrderStatus(true);
    }

    public void dialog() {

        new AlertDialog.Builder(this)
                .setTitle(R.string.popup_announcement)
                .setMessage(R.string.popup_announcement2)
                .setPositiveButton(R.string.popup_yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        updateStateUI();
                    }
                })
                .setNegativeButton(R.string.popup_no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        updateJobOrderStatus(false);
                    }
                })
                .show();
    }

    void updateJobOrderStatus(final Boolean accept) {
        HashMap<String,String> statusJSON = new HashMap<>();
        String name  = Utility.utility.getLoggedName(this);
        statusJSON.put("driver",accept ? name : "none");

        MyCookieJar cookieJar = Utility.utility.getCookieFromPreference(this);
        API api = Utility.utility.getAPIWithCookie(cookieJar);
        Call<JSONObject> callUpdateJO = api.updateJobOrder(jobOrder.joid, statusJSON);
        callUpdateJO.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                if (Utility.utility.catchResponse(getApplicationContext(), response)) {
                    if (accept) {
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getApplicationContext(), TolakPesanan.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                Utility.utility.showConnectivityWithError(getApplicationContext());
            }
        });
    }

}
