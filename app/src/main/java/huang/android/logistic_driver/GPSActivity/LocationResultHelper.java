/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package huang.android.logistic_driver.GPSActivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.BatteryManager;
import android.preference.PreferenceManager;
import android.support.v4.app.TaskStackBuilder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.gson.Gson;

import java.util.Date;
import java.util.List;

import huang.android.logistic_driver.API.API;
import huang.android.logistic_driver.MainActivity;
import huang.android.logistic_driver.Model.BackgroundData.BackgroundData;
import huang.android.logistic_driver.Model.MyCookieJar;
import huang.android.logistic_driver.R;
import huang.android.logistic_driver.Utility;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.BATTERY_SERVICE;
import static android.content.Context.TELEPHONY_SERVICE;

/**
 * Class to process location results.
 */
public class LocationResultHelper {

    final static String KEY_LOCATION_UPDATES_RESULT = "location-update-result";

    final private static String PRIMARY_CHANNEL = "default";


    private Context mContext;
    private List<Location> mLocations;
    private NotificationManager mNotificationManager;
    PhoneSignalHelper phoneSignalHelper;

    LocationResultHelper(Context context, List<Location> locations) {
        mContext = context;
        mLocations = locations;
        phoneSignalHelper = new PhoneSignalHelper(context);
    }

    /**
     * Returns the title for reporting about a list of {@link Location} objects.
     */
    private String getLocationResultTitle() {
        return "Connected";
    }

    private String getLocationResultText() {
        if (mLocations.isEmpty()) {
            return "-";
        }
        StringBuilder sb = new StringBuilder();
        for (Location location : mLocations) {
            sb.append("(");
            sb.append(location.getLatitude());
            sb.append(", ");
            sb.append(location.getLongitude());
            sb.append(")");
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Saves location result as a string to {@link android.content.SharedPreferences}.
     */
    void saveResults() {
        PreferenceManager.getDefaultSharedPreferences(mContext)
                .edit()
                .putString(KEY_LOCATION_UPDATES_RESULT, getLocationResultTitle() + "\n" +
                        getLocationResultText())
                .apply();
    }

    /**
     * Fetches location results from {@link android.content.SharedPreferences}.
     */
    static String getSavedLocationResult(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(KEY_LOCATION_UPDATES_RESULT, "");
    }

    /**
     * Get the notification mNotificationManager.
     * <p>
     * Utility method as this helper works with it a lot.
     *
     * @return The system service NotificationManager
     */
    private NotificationManager getNotificationManager() {
        if (mNotificationManager == null) {
            mNotificationManager = (NotificationManager) mContext.getSystemService(
                    Context.NOTIFICATION_SERVICE);
        }
        return mNotificationManager;
    }

    /**
     * Displays a notification with the location results.
     */
    void showNotification() {
        Intent notificationIntent = new Intent(mContext, MainActivity.class);

        // Construct a task stack.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);

        // Add the main Activity to the task stack as the parent.
        stackBuilder.addParentStack(MainActivity.class);

        // Push the content Intent onto the stack.
        stackBuilder.addNextIntent(notificationIntent);

        // Get a PendingIntent containing the entire back stack.
        PendingIntent notificationPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder notificationBuilder = new Notification.Builder(mContext)
                .setContentTitle(getLocationResultTitle())
                .setContentText(getLocationResultText())
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setAutoCancel(true)
                .setContentIntent(notificationPendingIntent);

        getNotificationManager().notify(0, notificationBuilder.build());
    }

    public void updateBackground() {
        MyCookieJar cookieJar = Utility.utility.getCookieFromPreference(mContext);
        API api = Utility.utility.getAPIWithCookie(cookieJar);
        BackgroundData backgroundData = new BackgroundData();
        int lastIndex = mLocations.size() - 1;
        backgroundData.lo = mLocations.get(lastIndex).getLongitude() + "";
        backgroundData.lat = mLocations.get(lastIndex).getLatitude() + "";
        Date today = new Date();
        backgroundData.last_update = Utility.utility.dateToFormatDatabase(today);
        backgroundData.driver = Utility.utility.getLoggedName(mContext);
        BatteryManager bm = (BatteryManager) mContext.getSystemService(BATTERY_SERVICE);
        int batteryLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        backgroundData.battery = batteryLevel + "";
        backgroundData.signal = phoneSignalHelper.getCurrentSignal() + "";
        backgroundData.status_gps = "On";
        String a = new Gson().toJson(backgroundData);
        Call<ResponseBody> backgroundCall = api.updateBackground(backgroundData);
        backgroundCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("tes","as");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}
