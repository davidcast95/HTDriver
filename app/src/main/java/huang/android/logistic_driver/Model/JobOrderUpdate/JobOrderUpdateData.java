package huang.android.logistic_driver.Model.JobOrderUpdate;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import huang.android.logistic_driver.Model.JobOrderUpdateImage.JobOrderUpdateImageData;

/**
 * Created by davidwibisono on 9/10/17.
 */

public class JobOrderUpdateData {
    @SerializedName("name")
    public String id;
    @SerializedName("waktu")
    public String time;
    @SerializedName("job_order")
    public String joid;
    @SerializedName("note")
    public String note;
    @SerializedName("lo")
    public String longitude;
    @SerializedName("lat")
    public String latitude;
    @SerializedName("vendor")
    public String vendor;
    @SerializedName("principle")
    public String principle;
    @SerializedName("docstatus")
    public int docstatus;
    @SerializedName("status")
    public String status;
    @SerializedName("location")
    public String location;
    @SerializedName("warehouse_name")
    public String warehouse_name;
    @SerializedName("city")
    public String city;
    @SerializedName("image_count")
    public List<JobOrderUpdateImageData> images;
}
