package huang.android.logistic_driver.Model.Login;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by davidwibisono on 14/11/17.
 */

public class LoginUserPermissionResponse {
    @SerializedName("data")
    public List<LoginUserPermission> data;
}
