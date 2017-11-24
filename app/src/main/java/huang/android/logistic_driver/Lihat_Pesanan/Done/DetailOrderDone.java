package huang.android.logistic_driver.Lihat_Pesanan.Done;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import huang.android.logistic_driver.API.API;
import huang.android.logistic_driver.Dashboard;
import huang.android.logistic_driver.Lihat_Pesanan.Base.DetailOrder;
import huang.android.logistic_driver.Lihat_Pesanan.TrackHistory;
import huang.android.logistic_driver.Maps.TrackOrderMaps;
import huang.android.logistic_driver.Model.JobOrder.JobOrderData;
import huang.android.logistic_driver.Model.JobOrderUpdate.JobOrderUpdateData;
import huang.android.logistic_driver.Model.JobOrderUpdate.JobOrderUpdateResponse;
import huang.android.logistic_driver.Model.MyCookieJar;
import huang.android.logistic_driver.R;
import huang.android.logistic_driver.Utility;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailOrderDone extends DetailOrder {

    @Override
    protected String getTitleString(String title) {
        return getString(R.string.dpd);
    }

    @Override
    protected Boolean hasOptionMenu(Boolean has) {
        return true;
    }

    @Override
    protected int getMenuType() {
        return R.menu.done_track_titlebar;
    }
}
