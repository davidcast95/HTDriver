package huang.android.logistic_driver.Lihat_Pesanan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import huang.android.logistic_driver.API.API;
import huang.android.logistic_driver.Dashboard;
import huang.android.logistic_driver.GPSActivity.GPSActivity;
import huang.android.logistic_driver.Lihat_Pesanan.Active.DetailOrderActive;
import huang.android.logistic_driver.Lihat_Pesanan.Done.DetailOrderDone;
import huang.android.logistic_driver.Model.History.HistoryHistory;
import huang.android.logistic_driver.Model.History.HistoryResponse;
import huang.android.logistic_driver.Model.JobOrderUpdate.JobOrderUpdateData;
import huang.android.logistic_driver.Model.MyCookieJar;
import huang.android.logistic_driver.R;
import huang.android.logistic_driver.Utility;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrackHistory extends GPSActivity {

    String joid, date,to,from;
    ListView lv;
    TrackHistoryAdapter trackHistoryAdapter;
    ProgressBar loading;
    LinearLayout layout;
    TextView nodata;

    List<JobOrderUpdateData> jobOrderUpdateDataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utility.utility.getLanguage(this);
        super.onCreate(savedInstanceState);
        setTitle(R.string.tracking_history);
        setContentView(R.layout.activity_track_history);

        nodata = (TextView) findViewById(R.id.no_data);
        nodata.setVisibility(View.GONE);
        loading=(ProgressBar)findViewById(R.id.loading);
        lv=(ListView) findViewById(R.id.historylist);
        layout=(LinearLayout)findViewById(R.id.layout);

        Intent intent = getIntent();
        joid = intent.getStringExtra("joid");
        from = intent.getStringExtra("origin");
        to = intent.getStringExtra("destination");

        TextView joid = (TextView)findViewById(R.id.joid);
        joid.setText(this.joid);
        TextView tanggal = (TextView)findViewById(R.id.tanggal1);
        tanggal.setText(date);
        TextView locationfrom = (TextView)findViewById(R.id.locationfrom);
        locationfrom.setText(Html.fromHtml(from));
        TextView locationto = (TextView)findViewById(R.id.locationto);
        locationto.setText(Html.fromHtml(to));

        from = getIntent().getStringExtra("from");

        if (from.equals("OrderActive") || from.equals("Dashboard")) {
            jobOrderUpdateDataList = DetailOrderActive.jobOrderUpdates;
        } else if (from.equals("OrderDone")) {
            jobOrderUpdateDataList = DetailOrderDone.jobOrderUpdates;
        }

        if (jobOrderUpdateDataList.size() > 0) {

            trackHistoryAdapter = new TrackHistoryAdapter(getApplicationContext(), R.layout.activity_track_history_list, jobOrderUpdateDataList);
            lv.setAdapter(trackHistoryAdapter);
        } else {
            nodata.setVisibility(View.VISIBLE);
            lv.setVisibility(View.GONE);
        }
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
