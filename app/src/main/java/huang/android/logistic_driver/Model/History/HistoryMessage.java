package huang.android.logistic_driver.Model.History;

import huang.android.logistic_driver.Model.Default.DataMessage;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Kristoforus Gumilang on 8/19/2017.
 */

public class HistoryMessage extends DataMessage {
    @SerializedName("data")
    public HistoryData data;
}
