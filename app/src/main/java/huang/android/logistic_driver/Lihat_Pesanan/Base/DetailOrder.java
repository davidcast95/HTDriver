package huang.android.logistic_driver.Lihat_Pesanan.Base;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import huang.android.logistic_driver.API.API;
import huang.android.logistic_driver.Chat.Chat;
import huang.android.logistic_driver.Dashboard;
import huang.android.logistic_driver.Lihat_Pesanan.Active.OrderActive;
import huang.android.logistic_driver.Lihat_Pesanan.CheckPoint;
import huang.android.logistic_driver.Lihat_Pesanan.Done.OrderDone;
import huang.android.logistic_driver.Lihat_Pesanan.TrackHistory;
import huang.android.logistic_driver.Maps.TrackOrderMaps;
import huang.android.logistic_driver.Model.JobOrder.GetJobOrderResponse;
import huang.android.logistic_driver.Model.JobOrder.JobOrderData;
import huang.android.logistic_driver.Model.JobOrderRoute.JobOrderRouteData;
import huang.android.logistic_driver.Model.JobOrderUpdate.JobOrderUpdateData;
import huang.android.logistic_driver.Model.JobOrderUpdate.JobOrderUpdateResponse;
import huang.android.logistic_driver.Model.Location.Location;
import huang.android.logistic_driver.Model.MyCookieJar;
import huang.android.logistic_driver.Model.PushNotificationAction.PushNotificationAction;
import huang.android.logistic_driver.R;
import huang.android.logistic_driver.Utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by davidwibisono on 20/10/17.
 */

public class DetailOrder extends AppCompatActivity {
    ListView stopLocationList;
    LinearLayout layout, last_status;
    TextView statusTimeTV,statusTV,notesTV;

    public static JobOrderData jobOrder;
    public static List<JobOrderUpdateData> jobOrderUpdates = new ArrayList<>();
    public int listHeight = 0;

    String from;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utility.utility.getLanguage(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_order);

        setTitle(getTitleString(getString(R.string.dp)));

        layout=(LinearLayout)findViewById(R.id.layout);
        last_status=(LinearLayout)findViewById(R.id.last_status);
        last_status.setVisibility(View.GONE);
        statusTimeTV=(TextView)findViewById(R.id.status_time);
        statusTV=(TextView)findViewById(R.id.status);
        notesTV = (TextView)findViewById(R.id.notes);
        stopLocationList = (ListView)findViewById(R.id.stop_location_list);



        Intent intent = getIntent();
        String job_order = intent.getStringExtra("joid");
        String notif = intent.getStringExtra("notif");
        if (notif != null) {
            if (notif.equals(PushNotificationAction.NEW_ASSIGNED_JO)) {
                Toast.makeText(getApplicationContext(),getString(R.string.msg_new_assigned_jo),Toast.LENGTH_LONG).show();
            }
        }
        if (job_order != null) {
            getJobOrderBy(job_order);
        } else {
            int index = intent.getIntExtra("index", 0);
            from = intent.getStringExtra("from");
            if (from.equals("OrderActive")) {
                if (OrderActive.jobOrders.get(index) != null) {
                    jobOrder = OrderActive.jobOrders.get(index);

                }
            } else if (from.equals("OrderDone")) {
                if (OrderDone.jobOrders.get(index) != null) {
                    jobOrder = OrderDone.jobOrders.get(index);

                }
            } else {
                if (Dashboard.jobOrders.get(index) != null) {
                    jobOrder = Dashboard.jobOrders.get(index);
                }
            }
            if (jobOrder != null) {
                TextView principle = (TextView) findViewById(R.id.principle);
                TextView ref = (TextView) findViewById(R.id.ref_id);
                TextView joid = (TextView) findViewById(R.id.joid);
                TextView origin = (TextView) findViewById(R.id.origin);
                TextView destination = (TextView) findViewById(R.id.destination);
                TextView vendor_name = (TextView) findViewById(R.id.vendor_name);
                TextView vendor_cp_name = (TextView) findViewById(R.id.vendor_cp_name);
                TextView vendor_cp_phone = (TextView) findViewById(R.id.vendor_cp_phone);
                TextView principle_name = (TextView) findViewById(R.id.principle_name);
                TextView principle_cp_name = (TextView) findViewById(R.id.principle_cp_name);
                TextView principle_cp_phone = (TextView) findViewById(R.id.principle_cp_phone);
                TextView cargoInfo = (TextView) findViewById(R.id.cargo_info);
                TextView epd = (TextView) findViewById(R.id.pick_date);
                TextView edd = (TextView) findViewById(R.id.delivered_date);
                TextView volume = (TextView) findViewById(R.id.volume);
                TextView truck = (TextView) findViewById(R.id.truck);
                TextView truck_type = (TextView) findViewById(R.id.truck_type);
                TextView truck_hull_no = (TextView) findViewById(R.id.truck_hull_no);
                TextView cargoNote = (TextView) findViewById(R.id.cargo_notes);
                TextView driver_name = (TextView) findViewById(R.id.driver_name);
                TextView driver_phone = (TextView) findViewById(R.id.driver_phone);

                Utility.utility.setTextView(principle, jobOrder.principle);

                final ImageView profileImage = (ImageView)findViewById(R.id.profile_image);

                String imageUrl = jobOrder.principle_image.get(0);
                if (imageUrl != null) {
                    MyCookieJar cookieJar = Utility.utility.getCookieFromPreference(getApplicationContext());
                    API api = Utility.utility.getAPIWithCookie(cookieJar);
                    Call<ResponseBody> callImage = api.getImage(imageUrl);
                    callImage.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                ResponseBody responseBody = response.body();
                                if (responseBody != null) {
                                    Bitmap bm = BitmapFactory.decodeStream(response.body().byteStream());
                                    profileImage.setImageBitmap(bm);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });
                }

                if (jobOrder.ref == null) jobOrder.ref = "";
                Utility.utility.setTextView(ref, "Ref No : " + jobOrder.ref.replace("\n", ""));

                StopLocationViewerAdapter stopLocationAdapter = new StopLocationViewerAdapter(getApplicationContext(), jobOrder.routes, this);
                stopLocationList.setAdapter(stopLocationAdapter);
                ViewTreeObserver observer = stopLocationList.getViewTreeObserver();

                observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                        listHeight = Utility.utility.setAndGetListViewHeightBasedOnChildren(stopLocationList);
                    }
                });

                stopLocationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        dialogStopLocation(jobOrder.routes.get(i));
                    }
                });

                Utility.utility.setTextView(principle,jobOrder.principle);
                Utility.utility.setTextView(joid, jobOrder.joid);
                Utility.utility.setTextView(vendor_name, jobOrder.vendor);
                Utility.utility.setTextView(vendor_cp_name, jobOrder.vendor_cp_name);
                Utility.utility.setTextView(vendor_cp_phone, jobOrder.vendor_cp_phone);
                Utility.utility.setDialContactPhone(vendor_cp_phone, jobOrder.vendor_cp_phone, this);
                Utility.utility.setTextView(principle_name, jobOrder.principle);
                Utility.utility.setTextView(principle_cp_name, jobOrder.principle_cp_name);
                Utility.utility.setTextView(principle_cp_phone, jobOrder.principle_cp_phone);
                Utility.utility.setDialContactPhone(principle_cp_phone, jobOrder.principle_cp_phone, this);
                Utility.utility.setTextView(cargoInfo, jobOrder.cargoInfo);
                Utility.utility.setTextView(epd, Utility.formatDateFromstring(Utility.dateDBShortFormat, Utility.dateLongFormat, jobOrder.etd));
                Utility.utility.setTextView(edd, Utility.formatDateFromstring(Utility.dateDBShortFormat, Utility.dateLongFormat, jobOrder.eta));

                Utility.utility.setTextView(volume, jobOrder.estimate_volume);
                Utility.utility.setTextView(truck, jobOrder.truck);
                Utility.utility.setTextView(truck_type, jobOrder.truck_type);
                Utility.utility.setTextView(truck_hull_no, jobOrder.truck_lambung);
                Utility.utility.setTextView(cargoNote, jobOrder.notes);
                Utility.utility.setTextView(driver_name, jobOrder.driver_name);
                Utility.utility.setTextView(driver_phone, jobOrder.driver_phone);
                Utility.utility.setDialContactPhone(driver_phone, jobOrder.driver_phone, this);
            }
        }
    }

    public void dialogStopLocation(JobOrderRouteData route) {
        new AlertDialog.Builder(this);
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.dialog_stop_location_detail, null);


        TextView typeTV = (TextView)promptsView.findViewById(R.id.type),
                stopLocationTV = (TextView)promptsView.findViewById(R.id.stop_location),
                nameTV = (TextView)promptsView.findViewById(R.id.name),
                cpTV = (TextView)promptsView.findViewById(R.id.cp),
                itemTV = (TextView)promptsView.findViewById(R.id.item),
                remarkTV = (TextView)promptsView.findViewById(R.id.remark);

        Utility.utility.setTextView(typeTV,route.type);
        Utility.utility.setTextView(stopLocationTV,Utility.utility.longFormatLocation(new Location(route.distributor_code,route.location,route.city,route.address,route.warehouse_name,"","")));
        Utility.utility.setTextView(nameTV,route.nama);
        Utility.utility.setTextView(cpTV,route.phone);
        Utility.utility.setDialContactPhone(cpTV, route.phone, this);
        Utility.utility.setTextView(itemTV,route.item_info);
        Utility.utility.setTextView(remarkTV,route.remark);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);
        alertDialogBuilder.setView(promptsView);
        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setNegativeButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();
        String job_order = intent.getStringExtra("joid");
        if (job_order == null) {
            getLastUpdate();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(getMenuType(), menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (hasOptionMenu(true)) {
            switch (item.getItemId()) {
                case R.id.action_history:
                    Intent intent = new Intent(this, TrackHistory.class);
                    intent.putExtra("joid", jobOrder.joid);
                    intent.putExtra("origin", Utility.utility.longFormatLocation(new Location(jobOrder.routes.get(0).distributor_code,jobOrder.routes.get(0).location,jobOrder.routes.get(0).city,jobOrder.routes.get(0).address,jobOrder.routes.get(0).warehouse_name,"","")));
                    intent.putExtra("origin_type",jobOrder.routes.get(0).type);
                    int lastIndex = jobOrder.routes.size()-1;
                    intent.putExtra("destination", Utility.utility.longFormatLocation(new Location(jobOrder.routes.get(lastIndex).distributor_code,jobOrder.routes.get(lastIndex).location,jobOrder.routes.get(lastIndex).city,jobOrder.routes.get(lastIndex).address,jobOrder.routes.get(lastIndex).warehouse_name,"","")));
                    intent.putExtra("destination_type",jobOrder.routes.get(lastIndex).type);intent.putExtra("from",from);
                    intent.putExtra("from",from);
                    startActivity(intent);
                    break;
                case R.id.action_cekpoint:
                    Intent intent3 = new Intent(this, CheckPoint.class);
                    intent3.putExtra("joid", jobOrder.joid);
                    intent3.putExtra("principle", jobOrder.principle);
                    intent3.putExtra("vendor", jobOrder.vendor);
                    intent3.putExtra("driver", jobOrder.driver);
                    startActivityForResult(intent3, 200);
                    break;
                case R.id.action_message:
                    Intent intent1 = new Intent(this, Chat.class);
                    intent1.putExtra("joid",jobOrder.joid);
                    startActivity(intent1);
                    break;
                case android.R.id.home:
                    // app icon in action bar clicked; goto parent activity.
                    this.finish();
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }

            return true;
        } else {
            return false;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 200) {
            if (resultCode == RESULT_OK) {
                getLastUpdate();
            }
        }
    }

    protected int getMenuType() {
        return R.menu.active_track_titlebar;
    }

    //API
    void getLastUpdate() {
        MyCookieJar cookieJar = Utility.utility.getCookieFromPreference(this);
        API api = Utility.utility.getAPIWithCookie(cookieJar);
        Call<JobOrderUpdateResponse> callGetJOUpdate = api.getJOUpdate(jobOrder.joid);
        callGetJOUpdate.enqueue(new Callback<JobOrderUpdateResponse>() {
            @Override
            public void onResponse(Call<JobOrderUpdateResponse> call, Response<JobOrderUpdateResponse> response) {
                if (Utility.utility.catchResponse(getApplicationContext(), response, "")) {
                    JobOrderUpdateResponse jobOrderUpdateResponse = response.body();
                    if (jobOrderUpdateResponse != null) {
                        if (jobOrderUpdateResponse.jobOrderUpdates != null) {
                            jobOrderUpdates = jobOrderUpdateResponse.jobOrderUpdates;
                            if (jobOrderUpdates.size() > 0) {
                                final JobOrderUpdateData lastJOStatus = jobOrderUpdates.get(0);
                                last_status.setVisibility(View.VISIBLE);
                                statusTimeTV.setText(Utility.formatDateFromstring(Utility.dateDBLongFormat, Utility.LONG_DATE_TIME_FORMAT, lastJOStatus.time));
                                statusTV.setText(lastJOStatus.status);
                                notesTV.setText(lastJOStatus.note);
                                ImageView button = (ImageView) findViewById(R.id.detail_active_last_map);
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Double latitude = Double.valueOf(lastJOStatus.latitude), longitude = Double.valueOf(lastJOStatus.longitude);
                                        if (latitude != null || longitude != null) {
                                            Intent maps = new Intent(getApplicationContext(), TrackOrderMaps.class);
                                            maps.putExtra("longitude", longitude);
                                            maps.putExtra("latitude", latitude);
                                            maps.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(maps);
                                        } else {
                                            Context c = getApplicationContext();
                                            Toast.makeText(c, R.string.nla, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                            } else {
                                last_status.setVisibility(View.GONE);
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<JobOrderUpdateResponse> call, Throwable t) {
                Utility.utility.showConnectivityUnstable(getApplicationContext());
            }
        });
    }

    //delegate
    protected String getTitleString(String title) {
        if (title.equals(""))
            return getResources().getString(R.string.track_order_list);
        else
            return title;
    }
    protected Boolean hasOptionMenu(Boolean has) {
        return has;
    }

    void getJobOrderBy(String joid) {
        MyCookieJar cookieJar = Utility.utility.getCookieFromPreference(this);
        API api = Utility.utility.getAPIWithCookie(cookieJar);
        final Activity activity = this;
        Call<GetJobOrderResponse> callJO = api.getJobOrderBy(joid);
        callJO.enqueue(new Callback<GetJobOrderResponse>() {
            @Override
            public void onResponse(Call<GetJobOrderResponse> call, Response<GetJobOrderResponse> response) {
                if (Utility.utility.catchResponse(getApplicationContext(), response,"")) {
                    GetJobOrderResponse jobOrderResponse = response.body();
                    if (jobOrderResponse.jobOrders != null) {
                        jobOrder = jobOrderResponse.jobOrders.get(0);
                        TextView principle = (TextView) findViewById(R.id.principle);
                        TextView ref = (TextView) findViewById(R.id.ref_id);
                        TextView joid = (TextView) findViewById(R.id.joid);
//                        TextView origin = (TextView) findViewById(R.id.origin);
//                        TextView destination = (TextView) findViewById(R.id.destination);
                        TextView vendor_name = (TextView) findViewById(R.id.vendor_name);
                        TextView vendor_cp_name = (TextView) findViewById(R.id.vendor_cp_name);
                        TextView vendor_cp_phone = (TextView) findViewById(R.id.vendor_cp_phone);
                        TextView principle_name = (TextView) findViewById(R.id.principle_name);
                        TextView principle_cp_name = (TextView) findViewById(R.id.principle_cp_name);
                        TextView principle_cp_phone = (TextView) findViewById(R.id.principle_cp_phone);
                        TextView cargoInfo = (TextView) findViewById(R.id.cargo_info);
                        TextView epd = (TextView) findViewById(R.id.pick_date);
                        TextView edd = (TextView) findViewById(R.id.delivered_date);
                        TextView volume = (TextView) findViewById(R.id.volume);
                        TextView truck = (TextView) findViewById(R.id.truck);
                        TextView truck_type = (TextView) findViewById(R.id.truck_type);
                        TextView truck_hull_no = (TextView) findViewById(R.id.truck_hull_no);
                        TextView cargoNote = (TextView) findViewById(R.id.cargo_notes);
                        TextView driver_name = (TextView) findViewById(R.id.driver_name);
                        TextView driver_phone = (TextView) findViewById(R.id.driver_phone);

                        Utility.utility.setTextView(principle, jobOrder.principle);

                        final ImageView profileImage = (ImageView)findViewById(R.id.profile_image);

                        String imageUrl = jobOrder.principle_image.get(0);
                        if (imageUrl != null) {
                            MyCookieJar cookieJar = Utility.utility.getCookieFromPreference(getApplicationContext());
                            API api = Utility.utility.getAPIWithCookie(cookieJar);
                            Call<ResponseBody> callImage = api.getImage(imageUrl);
                            callImage.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    if (response.isSuccessful()) {
                                        ResponseBody responseBody = response.body();
                                        if (responseBody != null) {
                                            Bitmap bm = BitmapFactory.decodeStream(response.body().byteStream());
                                            profileImage.setImageBitmap(bm);
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {

                                }
                            });
                        }

                        jobOrder.joid = joid.getText().toString();
                        if (jobOrder.ref == null) jobOrder.ref = "";
                        Utility.utility.setTextView(ref, "Ref No : " + jobOrder.ref.replace("\n", ""));
//                        origin.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                dialogStopLocation(jobOrder.routes.get(0));
//                            }
//                        });
//                        Utility.utility.setTextView(origin, Utility.utility.simpleFormatLocation(new Location(jobOrder.routes.get(0).distributor_code, jobOrder.routes.get(0).location, jobOrder.routes.get(0).city, jobOrder.routes.get(0).address, jobOrder.routes.get(0).warehouse_name, "", "")));
//                        destination.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                dialogStopLocation(jobOrder.routes.get(1));
//                            }
//                        });
//                        Utility.utility.setTextView(destination, Utility.utility.simpleFormatLocation(new Location(jobOrder.routes.get(1).distributor_code, jobOrder.routes.get(1).location, jobOrder.routes.get(1).city, jobOrder.routes.get(1).address, jobOrder.routes.get(1).warehouse_name, "", "")));

                        if (jobOrder.routes.size() > 2) {
                            StopLocationViewerAdapter stopLocationAdapter = new StopLocationViewerAdapter(getApplicationContext(), jobOrder.routes.subList(2, jobOrder.routes.size()), activity);
                            stopLocationList.setAdapter(stopLocationAdapter);
                            ViewTreeObserver observer = stopLocationList.getViewTreeObserver();

                            observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                                @Override
                                public void onGlobalLayout() {
                                    listHeight = Utility.utility.setAndGetListViewHeightBasedOnChildren(stopLocationList);
                                }
                            });

                            stopLocationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    dialogStopLocation(jobOrder.routes.subList(2, jobOrder.routes.size()).get(i));
                                }
                            });
                        }

                        Utility.utility.setTextView(joid, jobOrder.joid);
                        Utility.utility.setTextView(vendor_name, jobOrder.vendor);
                        Utility.utility.setTextView(vendor_cp_name, jobOrder.vendor_cp_name);
                        Utility.utility.setTextView(vendor_cp_phone, jobOrder.vendor_cp_phone);
                        Utility.utility.setDialContactPhone(vendor_cp_phone, jobOrder.vendor_cp_phone, activity);
                        Utility.utility.setTextView(principle_name, jobOrder.principle);
                        Utility.utility.setTextView(principle_cp_name, jobOrder.principle_cp_name);
                        Utility.utility.setTextView(principle_cp_phone, jobOrder.principle_cp_phone);
                        Utility.utility.setDialContactPhone(principle_cp_phone, jobOrder.principle_cp_phone, activity);
                        Utility.utility.setTextView(cargoInfo, jobOrder.cargoInfo);
                        Utility.utility.setTextView(epd, Utility.formatDateFromstring(Utility.dateDBShortFormat, Utility.dateLongFormat, jobOrder.etd));
                        Utility.utility.setTextView(edd, Utility.formatDateFromstring(Utility.dateDBShortFormat, Utility.dateLongFormat, jobOrder.eta));

                        Utility.utility.setTextView(volume, jobOrder.estimate_volume);
                        Utility.utility.setTextView(truck, jobOrder.truck);
                        Utility.utility.setTextView(truck_type, jobOrder.truck_type);
                        Utility.utility.setTextView(truck_hull_no, jobOrder.truck_lambung);
                        Utility.utility.setTextView(cargoNote, jobOrder.notes);
                        Utility.utility.setTextView(driver_name, jobOrder.driver_name);
                        Utility.utility.setTextView(driver_phone, jobOrder.driver_phone);
                        Utility.utility.setDialContactPhone(driver_phone, jobOrder.driver_phone, activity);
                    }
                    getLastUpdate();

                }

            }

            @Override
            public void onFailure(Call<GetJobOrderResponse> call, Throwable t) {
                Utility.utility.showConnectivityWithError(getApplicationContext());
            }
        });
    }
}
