package huang.android.logistic_driver.Model.JobOrder;

import com.google.gson.annotations.SerializedName;

/**
 * Created by davidwibisono on 2/3/18.
 */

public class JobOrderMetaDataMessage {
    @SerializedName("Dalam Proses")
    public JobOrderMetaData onprogress;
    @SerializedName("Selesai")
    public JobOrderMetaData done;
}
