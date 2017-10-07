package com.logisticsmarketplace.android.driver.Lihat_Pesanan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.logisticsmarketplace.android.driver.API.API;
import com.logisticsmarketplace.android.driver.Lihat_Pesanan.Active.DetailOrderActive;
import com.logisticsmarketplace.android.driver.Model.History.HistoryHistory;
import com.logisticsmarketplace.android.driver.Model.History.HistoryResponse;
import com.logisticsmarketplace.android.driver.Model.MyCookieJar;
import com.logisticsmarketplace.android.driver.R;
import com.logisticsmarketplace.android.driver.Utility;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrackHistory extends AppCompatActivity {

    String joid, date,to,from;
    ListView lv;
    TrackHistoryAdapter trackHistoryAdapter;
    ProgressBar loading;
    LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utility.utility.getLanguage(this);
        super.onCreate(savedInstanceState);
        setTitle(R.string.tracking_history);
        setContentView(R.layout.activity_track_history);

        Intent intent = getIntent();
        joid = intent.getStringExtra("joid");
        from = intent.getStringExtra("origin");
        to = intent.getStringExtra("destination");

        TextView joid = (TextView)findViewById(R.id.joid);
        joid.setText(this.joid);
        TextView tanggal = (TextView)findViewById(R.id.tanggal1);
        tanggal.setText(date);
        TextView locationfrom = (TextView)findViewById(R.id.locationfrom);
        locationfrom.setText(from);
        TextView locationto = (TextView)findViewById(R.id.locationto);
        locationto.setText(to);

        loading=(ProgressBar)findViewById(R.id.loading);
        lv=(ListView) findViewById(R.id.historylist);
        layout=(LinearLayout)findViewById(R.id.layout);
        loading.setVisibility(View.GONE);

        trackHistoryAdapter = new TrackHistoryAdapter(getApplicationContext(),R.layout.activity_track_history_list, DetailOrderActive.jobOrderUpdates);
        lv.setAdapter(trackHistoryAdapter);
        layout.setVisibility(View.VISIBLE);
        loading.setVisibility(View.INVISIBLE);

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
