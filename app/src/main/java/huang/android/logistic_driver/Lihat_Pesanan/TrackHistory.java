package huang.android.logistic_driver.Lihat_Pesanan;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import huang.android.logistic_driver.API.API;
import huang.android.logistic_driver.Lihat_Pesanan.Active.DetailOrderActive;
import huang.android.logistic_driver.Lihat_Pesanan.Base.DetailOrder;
import huang.android.logistic_driver.Lihat_Pesanan.Done.DetailOrderDone;
import huang.android.logistic_driver.Maps.DirectionFinder;
import huang.android.logistic_driver.Maps.DirectionFinderListener;
import huang.android.logistic_driver.Maps.Route;
import huang.android.logistic_driver.Model.Driver.DriverBackgroundUpdateData;
import huang.android.logistic_driver.Model.Driver.DriverBackgroundUpdateResponse;
import huang.android.logistic_driver.Model.JobOrder.JobOrderStatus;
import huang.android.logistic_driver.Model.JobOrderUpdate.JobOrderUpdateData;
import huang.android.logistic_driver.Model.MyCookieJar;
import huang.android.logistic_driver.R;
import huang.android.logistic_driver.Utility;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class TrackHistory extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap mMap;
    private List<Marker> markers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();

    String joid, date,to,from, from_type,to_type;
    ListView lv;
    TrackHistoryAdapter trackHistoryAdapter;
    ProgressBar loading;
    LinearLayout layout;
    TextView noUpdateLocation;
    DriverBackgroundUpdateData lastUpdateDriver = null;

    List<JobOrderUpdateData> jobOrderUpdateDataList = new ArrayList<>();
    ImageView currentLocation, pickUpOrigin, dropOrigin, pickUpDestination, dropDestination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utility.utility.getLanguage(this);
        super.onCreate(savedInstanceState);
        setTitle(R.string.tracking_history);
        setContentView(R.layout.activity_track_history);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        int locationProvide = 0;

        if (DetailOrder.jobOrderUpdates != null) {
            for (int i = 0; i < DetailOrder.jobOrderUpdates.size(); i++) {
                if (DetailOrder.jobOrderUpdates.get(i).longitude != null && DetailOrder.jobOrderUpdates.get(i).latitude != null)
                    if (!DetailOrder.jobOrderUpdates.get(i).longitude.equals("0.0") && !DetailOrder.jobOrderUpdates.get(i).equals("0.0"))
                        locationProvide++;

            }
        } else {
            DetailOrder.jobOrderUpdates = new ArrayList<>();
        }



//        mapFragment.getView().setVisibility(View.GONE);

        currentLocation = (ImageView)findViewById(R.id.current_location);

        currentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                focusOnDriver();
            }
        });
        noUpdateLocation = (TextView)findViewById(R.id.no_update_location);

        if (locationProvide > 0) {
            mapFragment.getView().setVisibility(View.VISIBLE);
            currentLocation.setVisibility(View.VISIBLE);
            noUpdateLocation.setVisibility(View.GONE);
        }
        else {
            mapFragment.getView().setVisibility(View.GONE);
            currentLocation.setVisibility(View.GONE);
            noUpdateLocation.setVisibility(View.VISIBLE);
        }
//
//        RelativeLayout mapHolder = (RelativeLayout)findViewById(R.id.mapholder);
//        mapHolder.setVisibility(View.GONE);



//        nodata = (TextView) findViewById(R.id.no_data);
//        nodata.setVisibility(View.GONE);
        loading=(ProgressBar)findViewById(R.id.loading);
        lv=(ListView) findViewById(R.id.historylist);
        layout=(LinearLayout)findViewById(R.id.layout);

        Intent intent = getIntent();
        joid = intent.getStringExtra("joid");
        from = intent.getStringExtra("origin");
        from_type = intent.getStringExtra("origin_type");
        to = intent.getStringExtra("destination");
        to_type = intent.getStringExtra("destination_type");

        TextView locationfrom = (TextView)findViewById(R.id.origin);
        locationfrom.setText(Html.fromHtml(from));
        TextView locationto = (TextView)findViewById(R.id.destination);
        locationto.setText(Html.fromHtml(to));

        pickUpOrigin = (ImageView)findViewById(R.id.pickup_origin_icon);
        dropOrigin = (ImageView)findViewById(R.id.drop_origin_icon);
        pickUpDestination = (ImageView)findViewById(R.id.pickup_destination_icon);
        dropDestination = (ImageView)findViewById(R.id.drop_destination_icon);
        if (from_type.equals("Pick Up")) {
            pickUpOrigin.setVisibility(View.VISIBLE);
        } else {
            dropOrigin.setVisibility(View.VISIBLE);
        }
        if (to_type.equals("Pick Up")) {
            pickUpDestination.setVisibility(View.VISIBLE);
        } else {
            dropDestination.setVisibility(View.VISIBLE);
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



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        drawJOUpdateMarker();

        trackHistoryAdapter = new TrackHistoryAdapter(getApplicationContext(),R.layout.activity_track_history_list, DetailOrder.jobOrderUpdates, mMap, markers);
        lv.setAdapter(trackHistoryAdapter);
        layout.setVisibility(View.VISIBLE);
        loading.setVisibility(View.INVISIBLE);
    }

    void drawJOUpdateMarker() {
        boolean isPinned = false;
        double minLat = -1, maxLat = -1, minLong = -1, maxLong = -1;
        if (DetailOrder.jobOrderUpdates != null) {
            for (int i = 0; i < DetailOrder.jobOrderUpdates.size(); i++) {
                if (DetailOrder.jobOrderUpdates.get(i).longitude == null || DetailOrder.jobOrderUpdates.get(i).latitude == null) {
                    Toast.makeText(getApplicationContext(), getString(R.string.no_update_location), Toast.LENGTH_SHORT).show();
                } else {
                    if (DetailOrder.jobOrderUpdates.get(i).latitude.equals("0.0") || DetailOrder.jobOrderUpdates.get(i).equals("0.0")) {

                    } else {
                        Double lat = Double.valueOf(DetailOrder.jobOrderUpdates.get(i).latitude), longi = Double.valueOf(DetailOrder.jobOrderUpdates.get(i).longitude);
                        LatLng currentLocation = new LatLng(lat, longi);

                        int icon = R.drawable.loc;
                        String statusIndex = DetailOrder.jobOrderUpdates.get(i).status.substring(0, 1);
                        switch (statusIndex) {
                            case "1":
                                icon = R.drawable.loc_1;
                                break;
                            case "2":
                                icon = R.drawable.loc_2;
                                break;
                            case "3":
                                icon = R.drawable.loc_3;
                                break;
                            case "4":
                                icon = R.drawable.loc_4;
                                break;
                            case "5":
                                icon = R.drawable.loc_5;
                                break;
                            case "6":
                                icon = R.drawable.loc_6;
                                break;
                        }

                        MarkerOptions marker = new MarkerOptions()
                                .position(currentLocation)
                                .title(DetailOrder.jobOrderUpdates.get(i).status)
                                .snippet(getString(R.string.last_update_on) + " " + Utility.formatDateFromstring(Utility.dateDBLongFormat, Utility.LONG_DATE_TIME_FORMAT, DetailOrder.jobOrderUpdates.get(i).time))
                                .icon(BitmapDescriptorFactory.fromResource(icon));
                        markers.add(mMap.addMarker(marker));
                        isPinned = true;
                        if (i == 0) {
                            minLat = lat;
                            maxLat = lat;
                            minLong = longi;
                            maxLong = longi;
                        } else {
                            if (lat < minLat) minLat = lat;
                            else if (lat > maxLat) maxLat = lat;
                            if (longi < minLong) minLong = longi;
                            else if (longi > maxLong) maxLong = longi;
                        }

                        if (i >= 1) {
                            Double originLat = Double.valueOf(DetailOrder.jobOrderUpdates.get(i - 1).latitude), originLong = Double.valueOf(DetailOrder.jobOrderUpdates.get(i - 1).longitude);
                            drawDirection(originLat, originLong, lat, longi);
                        }
                    }

                }
            }
        }
        if (!isPinned) {
            RelativeLayout mapHolder = (RelativeLayout)findViewById(R.id.mapholder);
            mapHolder.setVisibility(View.GONE);
        }

        //update driver mark road
        if (lastUpdateDriver != null) {
            Double lat = Double.valueOf(lastUpdateDriver.lat), longi = Double.valueOf(lastUpdateDriver.lo);
            LatLng lastLocation = new LatLng(lat,longi);
            MarkerOptions marker = new MarkerOptions()
                    .position(lastLocation)
                    .title(getString(R.string.last_position))
                    .snippet(getString(R.string.last_update_on) + " " + Utility.formatDateFromstring(Utility.dateDBLongFormat,Utility.LONG_DATE_TIME_FORMAT,lastUpdateDriver.last_update))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.loc_truck));
            markers.add(mMap.addMarker(marker));

            if (lat < minLat) minLat = lat;
            else if (lat > maxLat) maxLat = lat;
            if (longi < minLong) minLong = longi;
            else if (longi > maxLong) maxLong = longi;

            if (DetailOrder.jobOrderUpdates.size() > 0) {
                int lastIndex = DetailOrder.jobOrderUpdates.size() - 1;
                Double originLat = Double.valueOf(DetailOrder.jobOrderUpdates.get(lastIndex).latitude), originLong = Double.valueOf(DetailOrder.jobOrderUpdates.get(lastIndex).longitude);
                drawDirection(originLat, originLong, lat, longi);
            }

        }


        LatLng minLoc = new LatLng(minLat,minLong), maxLoc = new LatLng(maxLat, maxLong);
        mMap.setLatLngBoundsForCameraTarget(new LatLngBounds(minLoc, maxLoc));
        mMap.setMinZoomPreference(9.5f);

        focusOnDriver();



    }

    void drawDirection(Double originLat, Double originLong, Double destinationLat, Double destinationLong) {
        DirectionFinder directionFinder = new DirectionFinder(new DirectionFinderListener() {
            @Override
            public void onDirectionFinderStart() {

            }

            @Override
            public void onDirectionFinderSuccess(List<Route> routes) {
                for (Route route : routes) {

                    PolylineOptions polylineOptions = new PolylineOptions().
                            geodesic(true).
                            color(Color.rgb(88,114,47)).
                            width(10);

                    for (int i = 0; i < route.points.size(); i++)
                        polylineOptions.add(route.points.get(i));

                    polylinePaths.add(mMap.addPolyline(polylineOptions));
                }
            }
        },originLat + "," + originLong,destinationLat + "," + destinationLong);
        try {
            directionFinder.execute();
        } catch (UnsupportedEncodingException err) {

        }
    }

    void focusOnDriver() {
        if (lastUpdateDriver != null) {
            Double lat = Double.valueOf(lastUpdateDriver.lat), longi = Double.valueOf(lastUpdateDriver.lo);
            LatLng lastLocation = new LatLng(lat,longi);
            CameraUpdate location = CameraUpdateFactory.newLatLngZoom(
                    lastLocation, mMap.getCameraPosition().zoom);
            mMap.animateCamera(location);
            int lastIndex = markers.size()-1;
            markers.get(lastIndex).showInfoWindow();
        }
    }

    public void refreshDriverPosition(String driver) {
        if (driver.equals(DetailOrder.jobOrder.driver)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getDriverPosition();
                }
            }, 2000);
        }
    }

    //API
    void getDriverPosition() {
        if (DetailOrder.jobOrder.status.equals(JobOrderStatus.DONE)) return;
        MyCookieJar cookieJar = Utility.utility.getCookieFromPreference(this);
        API api = Utility.utility.getAPIWithCookie(cookieJar);
        String filters = "[[\"Driver Background Update\",\"driver\",\"=\",\"" + DetailOrder.jobOrder.driver + "\"]]";
        Call<DriverBackgroundUpdateResponse> callbg = api.getBackgroundUpdate(filters);
        callbg.enqueue(new Callback<DriverBackgroundUpdateResponse>() {
            @Override
            public void onResponse(Call<DriverBackgroundUpdateResponse> call, Response<DriverBackgroundUpdateResponse> response) {
                if (Utility.utility.catchResponse(getApplicationContext(),response,"")) {
                    DriverBackgroundUpdateResponse driverBackgroundUpdateResponse = response.body();
                    if (driverBackgroundUpdateResponse != null) {
                        if (driverBackgroundUpdateResponse.data.size() > 0) {
                            lastUpdateDriver = driverBackgroundUpdateResponse.data.get(0);
                            drawJOUpdateMarker();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<DriverBackgroundUpdateResponse> call, Throwable t) {

            }
        });
    }
}
