package com.logisticsmarketplace.android.driver.Lihat_Pesanan;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;

import com.logisticsmarketplace.android.driver.API.API;
import com.logisticsmarketplace.android.driver.Lihat_Pesanan.Active.OrderActive;
import com.logisticsmarketplace.android.driver.Lihat_Pesanan.Done.OrderDone;
import com.logisticsmarketplace.android.driver.Model.JobOrder.JobOrderResponse;
import com.logisticsmarketplace.android.driver.Model.JobOrder.JobOrderStatus;
import com.logisticsmarketplace.android.driver.Model.MyCookieJar;
import com.logisticsmarketplace.android.driver.R;
import com.logisticsmarketplace.android.driver.Utility;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewJobOrder extends Fragment implements ViewPager.OnPageChangeListener, TabHost.OnTabChangeListener{
    ViewPager viewPager;
    TabHost tabHost;
    int onprogress = 0,done = 0;
    View v;
    public ViewJobOrder() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_view_order, container, false);

        initViewPager();
        initTabHost();
        getCount();

        return v;
    }

    private void initTabHost() {
        tabHost=(TabHost)v.findViewById(R.id.tabhost);
        tabHost.setup();

        SharedPreferences prefs = this.getActivity().getSharedPreferences("LanguageSwitch", Context.MODE_PRIVATE);
        String language = prefs.getString("language","English");
        String[] tabs = new String[2];
        if(language.contentEquals("English")){
            tabs[0]="Active";
            tabs[1]="Done";
        }
        else {
            tabs[0]="Aktif";
            tabs[1]="Selesai";
        }

        for(int i=0;i<2;i++){
            TabHost.TabSpec tabSpec;
            tabSpec = tabHost.newTabSpec(tabs[i]);
            tabSpec.setIndicator(tabs[i]);
            tabSpec.setContent(new FakeContent(getActivity().getApplicationContext()));
            tabHost.addTab(tabSpec);
        }

        tabHost.setOnTabChangedListener(this);
    }

    public class FakeContent implements TabHost.TabContentFactory{
        Context context;
        public FakeContent(Context mcontext){
            context=mcontext;
        }
        @Override
        public View createTabContent(String s) {
            View fakeView = new View(context);
            fakeView.setMinimumHeight(0);
            fakeView.setMinimumHeight(0);
            return fakeView;
        }
    }

    private void initViewPager() {
        viewPager = (ViewPager)v.findViewById(R.id.view_pager);
        List<Fragment> listFragments =  new ArrayList<Fragment>();
        listFragments.add(new OrderActive());
        listFragments.add(new OrderDone());

        ViewJobOrderPagerAdapter viewJobOrderAdapter = new ViewJobOrderPagerAdapter(getChildFragmentManager(), listFragments);
        viewPager.setAdapter(viewJobOrderAdapter);
        viewPager.setOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int selectedItem) {
        tabHost.setCurrentTab(selectedItem);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onTabChanged(String s) {
        int selectedItem = tabHost.getCurrentTab();
        Log.e("asd",viewPager.toString());
        viewPager.setCurrentItem(selectedItem);
    }

    void getActiveOrder() {
        MyCookieJar cookieJar = Utility.utility.getCookieFromPreference(this.getActivity());
        API api = Utility.utility.getAPIWithCookie(cookieJar);
        String driver = Utility.utility.getLoggedName(this.getActivity());
        Call<JobOrderResponse> callJO = api.getJobOrder("[[\"Job Order\",\"status\",\"=\",\""+ JobOrderStatus.ON_PROGRESS+"\"],[\"Job Order\",\"driver\",\"=\",\"" + driver + "\"]]");
        callJO.enqueue(new Callback<JobOrderResponse>() {
            @Override
            public void onResponse(Call<JobOrderResponse> call, Response<JobOrderResponse> response) {
                if (Utility.utility.catchResponse(getActivity().getApplicationContext(), response)) {
                    onprogress = response.body().jobOrders.size();
                    SharedPreferences prefs = getActivity().getSharedPreferences("LanguageSwitch", Context.MODE_PRIVATE);
                    String language = prefs.getString("language","Bahasa Indonesia");
                    TextView label = (TextView)tabHost.getTabWidget().getChildTabViewAt(0).findViewById(android.R.id.title);
                    if(language.contentEquals("English")) {
                        label.setText("On Progress (" + onprogress + ")");
                    } else {
                        label.setText("Dalam Proses (" + onprogress + ")");
                    }
                }

            }

            @Override
            public void onFailure(Call<JobOrderResponse> call, Throwable t) {
            }
        });
    }
    void getDoneOrder() {
        MyCookieJar cookieJar = Utility.utility.getCookieFromPreference(this.getActivity());
        API api = Utility.utility.getAPIWithCookie(cookieJar);
        String driver = Utility.utility.getLoggedName(this.getActivity());
        Call<JobOrderResponse> callJO = api.getJobOrder("[[\"Job Order\",\"status\",\"=\",\""+ JobOrderStatus.DONE+"\"],[\"Job Order\",\"driver\",\"=\",\"" + driver + "\"]]");
        callJO.enqueue(new Callback<JobOrderResponse>() {
            @Override
            public void onResponse(Call<JobOrderResponse> call, Response<JobOrderResponse> response) {
                if (Utility.utility.catchResponse(getActivity().getApplicationContext(), response)) {
                    done = response.body().jobOrders.size();
                    SharedPreferences prefs = getActivity().getSharedPreferences("LanguageSwitch", Context.MODE_PRIVATE);
                    String language = prefs.getString("language","Bahasa Indonesia");
                    TextView label = (TextView)tabHost.getTabWidget().getChildTabViewAt(1).findViewById(android.R.id.title);
                    if(language.contentEquals("English")) {
                        label.setText("Complete (" + done + ")");
                    } else {
                        label.setText("Selesai (" + done + ")");
                    }
                }

            }

            @Override
            public void onFailure(Call<JobOrderResponse> call, Throwable t) {
            }
        });
    }

    void getCount() {
        getActiveOrder();
        getDoneOrder();
    }



}
