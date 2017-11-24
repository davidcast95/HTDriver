package huang.android.logistic_driver.Model.History;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Kristoforus Gumilang on 8/19/2017.
 */

public class HistoryHistory {
    @SerializedName("posting_date")
    public String date;

    @SerializedName("state")
    public String state;

    @SerializedName("note")
    public String note;

    @SerializedName("latitude")
    public Double latitude;

    @SerializedName("longitude")
    public Double longitude;
}
