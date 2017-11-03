package com.logisticsmarketplace.android.driver;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.logisticsmarketplace.android.driver.API.API;
import com.logisticsmarketplace.android.driver.Lihat_Pesanan.Active.DetailOrderActive;
import com.logisticsmarketplace.android.driver.Model.JobOrder.JobOrderData;
import com.logisticsmarketplace.android.driver.Model.JobOrder.JobOrderResponse;
import com.logisticsmarketplace.android.driver.Model.JobOrder.JobOrderStatus;
import com.logisticsmarketplace.android.driver.Model.MyCookieJar;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Dashboard extends Fragment {
    View v;
    ListView lv;
    TextView noData;
    PendingOrderAdapter pendingOrderAdapter;
    DashboardAdapter dashboardAdapter;
    ProgressBar loading;
    SwipeRefreshLayout mSwipeRefreshLayout;
    public static List<JobOrderData> jobOrders;

    public Dashboard() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Utility.utility.getLanguage(this.getActivity());
        v = inflater.inflate(R.layout.fragment_dashboard, container, false);

        noData = (TextView) v.findViewById(R.id.no_data);
        lv= (ListView) v.findViewById(R.id.pendingListView);
        lv.setVisibility(View.INVISIBLE);
        loading=(ProgressBar)v.findViewById(R.id.loading);
        mSwipeRefreshLayout=(SwipeRefreshLayout)v.findViewById(R.id.swipeRefreshLayout);
        getOnProgressOrder();
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItems();
            }
        });

        return v;
    }


    private AdapterView.OnItemClickListener onListClick = new AdapterView.OnItemClickListener(){
        public void onItemClick(AdapterView<?> parent,
                                View view,
                                int position, long id){
            Intent goDetail = new Intent(getActivity().getApplicationContext(),DetailOrderActive.class);
            goDetail.putExtra("index",position);
            goDetail.putExtra("from","Dashboard");
            startActivityForResult(goDetail, 200);
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        getOnProgressOrder();
    }

    //API
    void getOnProgressOrder() {
        noData.setVisibility(View.GONE);
        lv.setVisibility(View.GONE);
        MyCookieJar cookieJar = Utility.utility.getCookieFromPreference(this.getActivity());
        API api = Utility.utility.getAPIWithCookie(cookieJar);
        String driverName = Utility.utility.getLoggedName(getActivity());
        Call<JobOrderResponse> callJO = api.getJobOrder("[[\"Job Order\",\"status\",\"=\",\""+ JobOrderStatus.ON_PROGRESS+"\"], [\"Job Order\",\"driver\",\"=\",\""+driverName+"\"]]");
        callJO.enqueue(new Callback<JobOrderResponse>() {
            @Override
            public void onResponse(Call<JobOrderResponse> call, Response<JobOrderResponse> response) {
                if (Utility.utility.catchResponse(getActivity().getApplicationContext(), response)) {
                    JobOrderResponse jobOrderResponse = response.body();
                    jobOrders = jobOrderResponse.jobOrders;
                    noData.setVisibility(View.GONE);
                    if (jobOrders.size() == 0) noData.setVisibility(View.VISIBLE);
                    else {
                        PendingOrderAdapter pendingOrderAdapter = new PendingOrderAdapter(v.getContext(), R.layout.default_order_list, jobOrders);
                        lv.setOnItemClickListener(onListClick);
                        lv.setAdapter(pendingOrderAdapter);
                        lv.setVisibility(View.VISIBLE);
                    }
                    loading.setVisibility(View.GONE);
                    onItemsLoadComplete();
                }

            }

            @Override
            public void onFailure(Call<JobOrderResponse> call, Throwable t) {
                Utility.utility.showConnectivityUnstable(getActivity().getApplicationContext());
                loading.setVisibility(View.GONE);
            }
        });
    }



    void refreshItems() {
        getOnProgressOrder();
    }

    void onItemsLoadComplete() {
        mSwipeRefreshLayout.setRefreshing(false);
    }


}
