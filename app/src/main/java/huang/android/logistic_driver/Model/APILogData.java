package huang.android.logistic_driver.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by davidwibisono on 2/20/18.
 */

public class APILogData {
    @SerializedName("url")
    public String url="";
    @SerializedName("message")
    public String message="";
    @SerializedName("error_code")
    public int error_code=0;
}
