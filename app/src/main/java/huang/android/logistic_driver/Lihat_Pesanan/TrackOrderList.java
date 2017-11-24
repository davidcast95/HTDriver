package huang.android.logistic_driver.Lihat_Pesanan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import huang.android.logistic_driver.GPSActivity.GPSActivity;
import huang.android.logistic_driver.Maps.TrackOrderMaps;
import huang.android.logistic_driver.R;
import huang.android.logistic_driver.Utility;

public class TrackOrderList extends GPSActivity {

    ProgressBar loading;
    SwipeRefreshLayout mSwipeRefreshLayout;
    LinearLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utility.utility.getLanguage(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_order_list);

        setTitle(R.string.track_order_list);

        loading=(ProgressBar)findViewById(R.id.loading);
        layout=(LinearLayout)findViewById(R.id.layout);
        layout.setVisibility(View.INVISIBLE);
        getDetailJO();

        ImageButton button = (ImageButton)findViewById(R.id.maps);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (latitude!= null || longitude!=null){
                    Intent maps = new Intent(getApplicationContext(), TrackOrderMaps.class);
                    maps.putExtra("longitude", longitude );
                    maps.putExtra("latitude", latitude );
                    startActivity(maps);
                }
                else {
                    Context c = getApplicationContext();
                    Toast.makeText(c,R.string.nla, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.trackorderlist_titlebar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_track_order:
                Intent intent = new Intent(this, TrackHistory.class);
                intent.putExtra("JOID",JOID);
                intent.putExtra("tanggal4",tanggal4);
                intent.putExtra("to",to);
                intent.putExtra("from",from);
                this.startActivity(intent);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    String latitude;
    String longitude;
    String JOID,tanggal4,to,from;

    String type ="dr";
    String typeid ="Tonon";
    //API
    public void getDetailJO() {

    }
}
