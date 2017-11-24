package huang.android.logistic_driver.Model.Location;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by davidwibisono on 20/10/17.
 */

public class LocationResponse {
    @SerializedName("data")
    public List<Location> locations;
}
