package huang.android.logistic_driver.Model.JobOrderRoute;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by davidwibisono on 1/20/18.
 */

public class JobOrderRouteResponse {
    @SerializedName("data")
    public List<JobOrderRouteData> routes;
}
