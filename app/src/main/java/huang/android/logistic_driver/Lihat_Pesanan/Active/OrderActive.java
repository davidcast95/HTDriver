package huang.android.logistic_driver.Lihat_Pesanan.Active;

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

import com.paging.listview.PagingListView;

import huang.android.logistic_driver.API.API;
import huang.android.logistic_driver.Lihat_Pesanan.ViewJobOrder;
import huang.android.logistic_driver.Model.JobOrder.GetJobOrderResponse;
import huang.android.logistic_driver.Model.JobOrder.JobOrderData;
import huang.android.logistic_driver.Model.JobOrder.JobOrderResponse;
import huang.android.logistic_driver.Model.JobOrder.JobOrderStatus;
import huang.android.logistic_driver.Model.JobOrderRoute.JobOrderRouteResponse;
import huang.android.logistic_driver.Model.MyCookieJar;
import huang.android.logistic_driver.R;
import huang.android.logistic_driver.Utility;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OrderActive extends Fragment implements PagingListView.Pagingable {
    View v;

    PagingListView lv;
    ProgressBar loading;
    SwipeRefreshLayout mSwipeRefreshLayout;
    TextView noData;

    public static List<JobOrderData> jobOrders = new ArrayList<>();
    OrderActiveAdapter onProgressOrderAdapter;
    int pager = 0, limit = 20;
    String lastQuery = "";

    public OrderActive() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_order_aktif, container, false);

        lv=(PagingListView) v.findViewById(R.id.layout);
        loading=(ProgressBar)v.findViewById(R.id.loading);
        noData = (TextView)v.findViewById(R.id.nodata);
        noData.setVisibility(View.GONE);

        mSwipeRefreshLayout=(SwipeRefreshLayout)v.findViewById(R.id.swipeRefreshLayout);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItems();
            }
        });
        onProgressOrderAdapter = new OrderActiveAdapter(v.getContext(), R.layout.fragment_order_aktif_list, jobOrders);
        lv.setOnItemClickListener(onListClick);
        lv.setAdapter(onProgressOrderAdapter);
        lv.setHasMoreItems(false);
        lv.setPagingableListener(this);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        getActiveOrder();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            refreshItems();
        }
    }

    private AdapterView.OnItemClickListener onListClick = new AdapterView.OnItemClickListener(){
        public void onItemClick(AdapterView<?> parent,
                                View view,
                                int position, long id){
            Intent goDetail = new Intent(getActivity().getApplicationContext(),DetailOrderActive.class);
            goDetail.putExtra("index",position);
            goDetail.putExtra("from","OrderActive");
            startActivityForResult(goDetail,100);
        }
    };

    void refreshItems() {
        pager = 0;
        onProgressOrderAdapter.clear();
        getActiveOrder();
        ViewJobOrder viewJobOrder = (ViewJobOrder)getParentFragment();
        viewJobOrder.getCount();
    }

    void onItemsLoadComplete() {

        mSwipeRefreshLayout.setRefreshing(false);
    }

    void getActiveOrder() {
        loading.setVisibility(View.VISIBLE);
        MyCookieJar cookieJar = Utility.utility.getCookieFromPreference(this.getActivity());
        API api = Utility.utility.getAPIWithCookie(cookieJar);
        String driverName = Utility.utility.getLoggedName(getActivity());
        Call<GetJobOrderResponse> callJO = api.getJobOrder(JobOrderStatus.ON_PROGRESS,driverName,lastQuery + "%","" + (pager++ * limit));
        callJO.enqueue(new Callback<GetJobOrderResponse>() {
            @Override
            public void onResponse(Call<GetJobOrderResponse> call, Response<GetJobOrderResponse> response) {
                loading.setVisibility(View.GONE);
                if (Utility.utility.catchResponse(getActivity().getApplicationContext(), response, "")) {
                    GetJobOrderResponse jobOrderResponse = response.body();
                    if (jobOrderResponse.jobOrders != null) {
                        onProgressOrderAdapter.addAll(jobOrderResponse.jobOrders);
                        lv.onFinishLoading(true,jobOrderResponse.jobOrders);
                    } else {
                        lv.onFinishLoading(false,null);
                    }
                    if (jobOrders.size() == 0) {
                        noData.setVisibility(View.VISIBLE);
                    } else {
                        noData.setVisibility(View.GONE);
                        lv.setVisibility(View.VISIBLE);
                    }
                    onItemsLoadComplete();
                } else {
                    Utility.utility.showConnectivityUnstable(getActivity().getApplicationContext());
                }

            }

            @Override
            public void onFailure(Call<GetJobOrderResponse> call, Throwable t) {

                loading.setVisibility(View.GONE);
            }
        });
    }


    @Override
    public void onLoadMoreItems() {
        getActiveOrder();
    }

    public void searchJobOrder(String query) {
        loading.setVisibility(View.VISIBLE);
        lastQuery = query;
        pager = 0;
        onProgressOrderAdapter.clear();
        getActiveOrder();
    }
}
