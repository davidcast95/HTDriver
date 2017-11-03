package com.logisticsmarketplace.android.driver.Lihat_Pesanan.Done;

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

import com.logisticsmarketplace.android.driver.API.API;
import com.logisticsmarketplace.android.driver.Dashboard;
import com.logisticsmarketplace.android.driver.Lihat_Pesanan.Base.DetailOrder;
import com.logisticsmarketplace.android.driver.Lihat_Pesanan.TrackHistory;
import com.logisticsmarketplace.android.driver.Maps.TrackOrderMaps;
import com.logisticsmarketplace.android.driver.Model.JobOrder.JobOrderData;
import com.logisticsmarketplace.android.driver.Model.JobOrderUpdate.JobOrderUpdateData;
import com.logisticsmarketplace.android.driver.Model.JobOrderUpdate.JobOrderUpdateResponse;
import com.logisticsmarketplace.android.driver.Model.MyCookieJar;
import com.logisticsmarketplace.android.driver.R;
import com.logisticsmarketplace.android.driver.Utility;

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
        return false;
    }
}
