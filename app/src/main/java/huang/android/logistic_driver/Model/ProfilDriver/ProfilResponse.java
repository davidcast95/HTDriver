package huang.android.logistic_driver.Model.ProfilDriver;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by davidwibisono on 8/24/17.
 */

public class ProfilResponse {
    @SerializedName("message")
    public List<Profil> data;
}
