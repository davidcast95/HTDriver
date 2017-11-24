package huang.android.logistic_driver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import huang.android.logistic_driver.Bantuan.Bantuan;
import huang.android.logistic_driver.GPSActivity.GPSActivity;
import huang.android.logistic_driver.Lihat_Pesanan.ViewJobOrder;
import huang.android.logistic_driver.Lihat_Profile.MyProfile;
import huang.android.logistic_driver.Pengaturan.Settings;

public class MainActivity extends GPSActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utility.utility.getLanguage(this);
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Fragment fragment = new Dashboard();
        FragmentManager manager2=getSupportFragmentManager();
        manager2.beginTransaction().replace(R.id.contentLayout,
                fragment,fragment.getTag()).commit();
        setTitle(R.string.dashboard);

        FirebaseApp.initializeApp(getApplicationContext());
        final String token = FirebaseInstanceId.getInstance().getToken();
        if (token != null) {
            Log.e("Token",token);
        }
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.e("Token",token);
            }
        };
        registerReceiver(broadcastReceiver,new IntentFilter(token));

        GoogleApiAvailability.getInstance().makeGooglePlayServicesAvailable(this);

        String driver = Utility.utility.getLoggedName(this).replace(" ","_");
        driver = driver.replace("@","_");
        FirebaseMessaging.getInstance().subscribeToTopic(driver);

    }

    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press back again to close", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_track_order) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void displaySelectedScreen(int id){
        Fragment fragment = null;
        switch (id){
            case R.id.nav_db:
                fragment = new Dashboard();
                setTitle(R.string.dashboard);
                break;
            case R.id.nav_vo:
                fragment = new ViewJobOrder();
                setTitle(R.string.view_order);
                break;
            case R.id.nav_viewprofile:
                fragment = new MyProfile();
                setTitle(R.string.view_profile);
                break;
            case R.id.nav_settings:
                fragment = new Settings();
                setTitle(R.string.settings);
                break;
            case R.id.nav_helpandsupport:
                fragment = new Bantuan();
                setTitle(R.string.hns);
                break;
            case R.id.nav_logout:
                SharedPreferences mPrefs;
                mPrefs = getSharedPreferences("myprefs",MODE_PRIVATE);
                SharedPreferences.Editor ed = mPrefs.edit();
                ed.putString("cookieJar", "null");
                ed.commit();
                Intent mainIntent = new Intent(MainActivity.this,SplashScreen.class);
                startActivity(mainIntent);
                finish();
                break;
        }
        if(fragment != null){
            FragmentManager manager2=getSupportFragmentManager();
            manager2.beginTransaction().replace(R.id.contentLayout,
                    fragment,fragment.getTag()).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        displaySelectedScreen(id);
        return true;
    }

    public static void resetIntervalGPS() {
        resetIntervalGPS();
    }
}
