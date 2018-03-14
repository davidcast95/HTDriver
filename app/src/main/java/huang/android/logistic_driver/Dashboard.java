package huang.android.logistic_driver;

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

import com.paging.listview.PagingListView;

import huang.android.logistic_driver.API.API;
import huang.android.logistic_driver.Lihat_Pesanan.Active.DetailOrderActive;
import huang.android.logistic_driver.Model.JobOrder.GetJobOrderResponse;
import huang.android.logistic_driver.Model.JobOrder.JobOrderData;
import huang.android.logistic_driver.Model.JobOrder.JobOrderResponse;
import huang.android.logistic_driver.Model.JobOrder.JobOrderStatus;
import huang.android.logistic_driver.Model.JobOrderRoute.JobOrderRouteResponse;
import huang.android.logistic_driver.Model.MyCookieJar;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Dashboard extends Fragment implements PagingListView.Pagingable {
    View v;
    PagingListView lv;
    TextView noData;
    ProgressBar loading;
    SwipeRefreshLayout mSwipeRefreshLayout;
    public static List<JobOrderData> jobOrders = new ArrayList<>();
    PendingOrderAdapter pendingOrderAdapter;
    int pager = 0,limit=20;

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
        lv= (PagingListView) v.findViewById(R.id.pendingListView);
        lv.setVisibility(View.INVISIBLE);
        loading=(ProgressBar)v.findViewById(R.id.loading);
        mSwipeRefreshLayout=(SwipeRefreshLayout)v.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItems();
            }
        });

        pendingOrderAdapter = new PendingOrderAdapter(v.getContext(), R.layout.default_order_list, jobOrders);
        lv.setOnItemClickListener(onListClick);
        lv.setAdapter(pendingOrderAdapter);
        lv.setHasMoreItems(false);
        lv.setPagingableListener(this);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        getOnProgressOrder();
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
        MyCookieJar cookieJar = Utility.utility.getCookieFromPreference(this.getActivity());
        API api = Utility.utility.getAPIWithCookie(cookieJar);
        String driverName = Utility.utility.getLoggedName(getActivity());
        Call<GetJobOrderResponse> callJO = api.getJobOrder(JobOrderStatus.ON_PROGRESS, driverName,"%",""+(pager++ * limit));
        callJO.enqueue(new Callback<GetJobOrderResponse>() {
            @Override
            public void onResponse(Call<GetJobOrderResponse> call, Response<GetJobOrderResponse> response) {
                if (Utility.utility.catchResponse(getActivity().getApplicationContext(), response,"")) {
                    GetJobOrderResponse jobOrderResponse = response.body();
                    if (jobOrderResponse.jobOrders != null) {
                        pendingOrderAdapter.addAll(jobOrderResponse.jobOrders);
                        lv.setVisibility(View.VISIBLE);
                        lv.onFinishLoading(true,jobOrderResponse.jobOrders);
                    } else {
                        lv.onFinishLoading(false,null);
                    }
                    if (jobOrders.size() == 0) {
                        noData.setVisibility(View.VISIBLE);
                    }
                    else {
                        noData.setVisibility(View.GONE);
                    }
                    loading.setVisibility(View.GONE);
                    onItemsLoadComplete();
                }

            }

            @Override
            public void onFailure(Call<GetJobOrderResponse> call, Throwable t) {
                Utility.utility.showConnectivityUnstable(getActivity().getApplicationContext());
                loading.setVisibility(View.GONE);
            }
        });
    }




    void refreshItems() {
        pager = 0;
        pendingOrderAdapter.clear();
        getOnProgressOrder();
    }

    void onItemsLoadComplete() {
        mSwipeRefreshLayout.setRefreshing(false);
    }


    @Override
    public void onLoadMoreItems() {
        getOnProgressOrder();
    }
}
