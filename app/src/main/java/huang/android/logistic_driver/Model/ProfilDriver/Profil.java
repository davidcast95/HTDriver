package huang.android.logistic_driver.Model.ProfilDriver;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by davidwibisono on 8/24/17.
 */

public class Profil {
    @SerializedName("nama")
    public String name;
    @SerializedName("phone")
    public String phone;
    @SerializedName("address")
    public String address;
    @SerializedName("email")
    public String email;
    @SerializedName("profile_image")
    public List<String> profile_image = new ArrayList<>();
}
