package huang.android.logistic_driver.Model.JobOrderUpdate;

import com.google.gson.annotations.SerializedName;

import okhttp3.MultipartBody;

/**
 * Created by davidwibisono on 22/10/17.
 */

public class JobOrderUpdateImage {
    @SerializedName("jouid")
    public String jouid;
    @SerializedName("file")
    public MultipartBody.Part image;
}
