package huang.android.logistic_driver;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import huang.android.logistic_driver.API.API;
import huang.android.logistic_driver.Model.APILogData;
import huang.android.logistic_driver.Model.Default.DataMessage;
import huang.android.logistic_driver.Model.Location.Location;
import huang.android.logistic_driver.Model.MyCookieJar;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by Kristoforus Gumilang on 8/24/2017.
 */

public class Utility {
    public static Utility utility = new Utility();

    public static String dateDBShortFormat = "yyyy-MM-dd";
    public static String dateLongFormat = "EEE, d MMM yyyy";

    public static String dateDBLongFormat = "yyyy-MM-dd HH:mm:ss";

    public static String LONG_DATE_TIME_FORMAT = "dd MMMM yyyy HH:mm:ss";
    public static String LONG_DATE_FORMAT = "dd MMMM yyyy";

    public void savelanguage(Activity activity, String bahasa){
        SharedPreferences prefs1 = activity.getSharedPreferences("LanguageSwitch", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs1.edit();
        editor.putString("language", bahasa);
        editor.commit();
    }

    public void saveBakgroundUpdate(Activity activity, int interval) {
        SharedPreferences prefs1 = activity.getSharedPreferences("GlobalSettings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs1.edit();
        editor.putInt("intervalGPS", interval);
        editor.commit();
    }
    public int getBackgroundUpdate(Context context) {
        SharedPreferences prefs1 =  context.getSharedPreferences("GlobalSettings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs1.edit();
        return prefs1.getInt("intervalGPS",30000);
    }
    public void getLanguage(Activity activity){
        SharedPreferences prefs = activity.getSharedPreferences("LanguageSwitch", Context.MODE_PRIVATE);
        String language = prefs.getString("language","Bahasa Indonesia");
        Log.e("Language",language);
        if(language.contentEquals("English")){
            setLocal(activity, "en");
        }
        else {
            setLocal(activity, "in");
        }
    }

    public Date stringToDate(String str) {
        SimpleDateFormat df = new SimpleDateFormat(dateDBLongFormat);
        try {
            return df.parse(str);
        } catch (ParseException err) {
            return null;
        }
    }

    public static String formatDateFromstring(String inputFormat, String outputFormat, String inputDate){

        if (inputDate == null) return "";
        if (inputDate.equals("")) return "";
        Date parsed = null;
        String outputDate = "";

        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, java.util.Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, java.util.Locale.getDefault());

        try {
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);

        }
        catch (ParseException e) {
            Log.e("DATE", "ParseException - dateFormat");
        }

        return outputDate;
    }

    private void setLocal(Activity activity, String language) {
        Locale myLocale;
        myLocale = new Locale(language);
        Resources res = activity.getResources();
        DisplayMetrics dm= res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf,dm);
    }

    public String getLoggedName(Activity activity) {
        SharedPreferences preferences = activity.getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        String name = preferences.getString("name","");
        return name;
    }
    public String getUsername(Activity activity) {
        SharedPreferences preferences = activity.getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        String usr = preferences.getString("usr","");
        return usr;
    }

    public String getLoggedName(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        String name = preferences.getString("name","");
        return name;
    }

    public void saveLoggedName(String name, Activity activity) {
        SharedPreferences preferences = activity.getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = preferences.edit();
        ed.putString("name", name);
        ed.commit();
    }
    public void saveUsername(String usr, Activity activity) {
        SharedPreferences preferences = activity.getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = preferences.edit();
        ed.putString("usr", usr);
        ed.commit();
    }
    public MyCookieJar getCookieFromPreference(Activity activity) {
        SharedPreferences preferences = activity.getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        String cookieJson = preferences.getString("cookieJar","");
        Gson gson = new Gson();
        MyCookieJar cookieJar = gson.fromJson(cookieJson, MyCookieJar.class);
        if (cookieJar == null) { cookieJar = new MyCookieJar(); }
        return cookieJar;
    }
    public MyCookieJar getCookieFromPreference(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        String cookieJson = preferences.getString("cookieJar","");
        Gson gson = new Gson();
        MyCookieJar cookieJar = gson.fromJson(cookieJson, MyCookieJar.class);
        if (cookieJar == null) { cookieJar = new MyCookieJar(); }
        return cookieJar;
    }
    public void saveCookieJarToPreference(MyCookieJar cookieJar, Activity activity) {
        SharedPreferences preferences = activity.getSharedPreferences("myprefs", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = gson.toJson(cookieJar);
        SharedPreferences.Editor ed = preferences.edit();
        ed.putString("cookieJar", json);
        ed.commit();
    }

    public String normalizeStringForFirebaseTopic(String name) {
        return name.replace(" ","_").replace("-","_").replace("(","").replace(")","").replace(".", "_").replace("@", "_");
    }


    public API getAPIWithCookie(MyCookieJar cookieJar) {
        //create client to get cookies from OkHttp
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        okHttpClient.cookieJar(cookieJar);
        OkHttpClient client = okHttpClient.build();

        //add cookie jar intercept to retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        return retrofit.create(API.class);
    }

    public <T> boolean catchResponse(Context context, Response<T> response, String json) {
        MyCookieJar cookieJar = getCookieFromPreference(context);
        API api = getAPIWithCookie(cookieJar);
        APILogData apiLogData = new APILogData();
        if (response.code() == 200) {
            Log.e("DATA UPLOADED","OK");
            return true;
        } else {
            apiLogData.error_code = response.code();
            apiLogData.url = response.raw().request().url().toString();
            apiLogData.message = json;
            if (response.code() == 401) {
                if (context == null) return false;
                Toast.makeText(context, "Invalid username or password", Toast.LENGTH_SHORT).show();
            } else if (response.code() == 500 || response.code() == 417) {
                if (context == null) return false;
                Toast.makeText(context, "Server is unreachable", Toast.LENGTH_SHORT).show();
            } else if (response.message().equals("Forbidden")) {

                if (context == null) return false;
                Toast.makeText(context, "Your session is expired. Please renew it by re-login", Toast.LENGTH_SHORT).show();
            } else {
                Log.e("FALSE", "");
            }

            Call<JSONObject> callAPILog = api.sendAPILog(apiLogData);
            callAPILog.enqueue(new Callback<JSONObject>() {
                @Override
                public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                    Log.e("APILOG","success send!");
                }

                @Override
                public void onFailure(Call<JSONObject> call, Throwable t) {
                    Log.e("APILOG","success failed!");
                }
            });
        }
        return false;
    }

    public boolean catchMessage(Context context, DataMessage dataMessage) {
        if (dataMessage.code == 200) {
            return true;
        } else {
            if (context != null)
                Toast.makeText(context,"Something went wrong",Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public void showConnectivityUnstable(Context context) {
        if (context == null) return;
        Toast.makeText(context,"Connectivity unstable",Toast.LENGTH_SHORT).show();
    }

    public void showConnectivityWithError(Context context) {
        if (context == null) return;
        Toast.makeText(context,"No network connection",Toast.LENGTH_SHORT).show();
    }


    //UNTUK format date di database e ko Ronny
    public String dateToFormatDatabase(Date date) {
        String myFormat = "yyyy-MM-dd HH:mm:ss.SSS";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        return sdf.format(date);
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public void setDialContactPhone(View trigger, final String phoneNumber,final Activity activity) {
        trigger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
            }
        });
    }

    public String longFormatLocation(Location location) {
        if (location.code == null) {
            if (location.address == null && location.city != null) return "<h4>" + location.warehouse + "</h4><big> " + location.city + "</big>";
            if (location.address != null && location.city == null) return "<h4>" + location.warehouse + "</h4><big>" + location.address +  "</big>";
            return "<h4>" + location.warehouse + "</h4><big>" + location.address + ", " + location.city + "</big>";
        } else {
            if (location.address == null && location.city != null) return "<h4>" + location.warehouse + " (" + location.code + ")</h4><big> " + location.city + "</big>";
            if (location.address != null && location.city == null) return "<h4>" + location.warehouse + " (" + location.code + ")</h4><big>" + location.address +  "</big>";
            return "<h4>" + location.warehouse + " (" + location.code + ")</h4> <big>" + location.address + ", " + location.city + "</big>";
        }
    }
    public String simpleFormatLocation(Location location) {
        if (location.city == null) return "<big><strong>" +location.warehouse + "</strong></big>";
        return "<big><strong>" +location.city + "</strong> - " + location.warehouse +"</big>";
    }
    public void setTextView(TextView view, String text) {
        if (view != null && text != null) {
            text = text.replace("\n","<br>");
            view.setText(Html.fromHtml(text));
        } else if (view != null && text == null) {
            view.setText(Html.fromHtml("-"));
        }
    }
    public void setEditText(EditText view, String text) {
        if (view != null && text != null) {
            text = text.replace("\n","<br>");
            view.setText(Html.fromHtml(text));
        } else if (view != null && text == null) {
            view.setText(Html.fromHtml("-"));
        }
    }
    public int setAndGetListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return 0;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
        return params.height;
    }

    public void setListViewHeigth(ListView listView, int height) {
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}
