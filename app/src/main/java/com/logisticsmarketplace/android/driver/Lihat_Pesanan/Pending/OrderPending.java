package com.logisticsmarketplace.android.driver.Lihat_Pesanan.Pending;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.logisticsmarketplace.android.driver.API.API;
import com.logisticsmarketplace.android.driver.Model.JobOrder.JobOrderData;
import com.logisticsmarketplace.android.driver.Model.JobOrder.JobOrderResponse;
import com.logisticsmarketplace.android.driver.Model.JobOrder.JobOrderStatus;
import com.logisticsmarketplace.android.driver.Model.MyCookieJar;
import com.logisticsmarketplace.android.driver.R;
import com.logisticsmarketplace.android.driver.Utility;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderPending extends Fragment {
    View v;
    OrderPendingAdapter onOrderPendingAdapter;
    ListView lv;
    ProgressBar loading;
    SwipeRefreshLayout mSwipeRefreshLayout;
    TextView noData;

    public static List<JobOrderData> jobOrders;

    public OrderPending() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_order_pending, container, false);

        lv=(ListView)v.findViewById(R.id.layout);
        loading=(ProgressBar)v.findViewById(R.id.loading);
        noData=(TextView) v.findViewById(R.id.nodata);
        noData.setVisibility(View.GONE);
        getPendingOrder();

        mSwipeRefreshLayout=(SwipeRefreshLayout)v.findViewById(R.id.swipeRefreshLayout);

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
            Intent goDetail = new Intent(getActivity().getApplicationContext(),DetailOrderPending.class);
            goDetail.putExtra("index", position);
            goDetail.putExtra("from","View Order");
            startActivity(goDetail);
        }
    };

    void refreshItems() {
        getPendingOrder();
    }

    void onItemsLoadComplete() {

        mSwipeRefreshLayout.setRefreshing(false);
    }

    void getPendingOrder() {
        MyCookieJar cookieJar = Utility.utility.getCookieFromPreference(this.getActivity());
        API api = Utility.utility.getAPIWithCookie(cookieJar);
        String driverName = Utility.utility.getLoggedName(getActivity());
        Call<JobOrderResponse> callJO = api.getJobOrder("[[\"Job Order\",\"status\",\"=\",\"" + JobOrderStatus.DONE +"\"], [\"Job Order\",\"driver\",\"=\",\"DR_0009\"]]");
        callJO.enqueue(new Callback<JobOrderResponse>() {
            @Override
            public void onResponse(Call<JobOrderResponse> call, Response<JobOrderResponse> response) {
                Log.e("asd", response.message());
                if (Utility.utility.catchResponse(getActivity().getApplicationContext(),response)) {
                    JobOrderResponse jobOrderResponse = response.body();
                    jobOrders = jobOrderResponse.jobOrders;
                    if (jobOrders.size() == 0) {
                        noData.setVisibility(View.VISIBLE);
                    } else {
                        noData.setVisibility(View.GONE);
                        OrderPendingAdapter onProgressOrderAdapter = new OrderPendingAdapter(v.getContext(), R.layout.fragment_order_pending_list, jobOrders);
                        lv.setOnItemClickListener(onListClick);
                        lv.setAdapter(onProgressOrderAdapter);
                        lv.setVisibility(View.VISIBLE);
                    }
                    loading.setVisibility(View.GONE);
                    onItemsLoadComplete();
                } else {
                    Utility.utility.showConnectivityUnstable(getActivity().getApplicationContext());
                }

            }

            @Override
            public void onFailure(Call<JobOrderResponse> call, Throwable t) {
                loading.setVisibility(View.GONE);
            }
        });
    }
}
