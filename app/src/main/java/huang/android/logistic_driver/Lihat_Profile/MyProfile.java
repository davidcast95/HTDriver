package huang.android.logistic_driver.Lihat_Profile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import huang.android.logistic_driver.API.API;
import huang.android.logistic_driver.Model.MyCookieJar;
import huang.android.logistic_driver.Model.ProfilDriver.Profil;
import huang.android.logistic_driver.Model.ProfilDriver.ProfilResponse;
import huang.android.logistic_driver.R;
import huang.android.logistic_driver.Utility;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyProfile extends Fragment {
    TextView nameTextEdit, phoneTextEdit, emailTextEdit, addressTextEdit;

    public MyProfile() {
        // Required empty public constructor
    }
    View v;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_view_profile, container, false);
        nameTextEdit = (TextView)v.findViewById(R.id.name);
        phoneTextEdit = (TextView)v.findViewById(R.id.telephone);
        emailTextEdit = (TextView)v.findViewById(R.id.email);
        addressTextEdit = (TextView)v.findViewById(R.id.address);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        getProfile();
    }

    //API Connectivity
    void getProfile() {
        MyCookieJar cookieJar = Utility.utility.getCookieFromPreference(this.getActivity());
        API api = Utility.utility.getAPIWithCookie(cookieJar);
        String name = Utility.utility.getLoggedName(this.getActivity());
        Call<ProfilResponse> profilResponseCall = api.getProfile("[[\"Driver\",\"name\",\"=\",\""+name+"\"]]");
        profilResponseCall.enqueue(new Callback<ProfilResponse>() {
            @Override
            public void onResponse(Call<ProfilResponse> call, Response<ProfilResponse> response) {
                if (Utility.utility.catchResponse(getActivity().getApplicationContext(), response, "")) {
                    ProfilResponse profilResponse = response.body();
                    List<Profil> profils = profilResponse.data;
                    if (profils.size() > 0) {
                        Profil profil = profils.get(0);
                        Utility.utility.setTextView(nameTextEdit,profil.name);
                        Utility.utility.setTextView(phoneTextEdit,profil.phone);
                        Utility.utility.setTextView(emailTextEdit,profil.email);
                        Utility.utility.setTextView(addressTextEdit,profil.address);
                    }
                }
            }

            @Override
            public void onFailure(Call<ProfilResponse> call, Throwable t) {

            }
        });
    }

}
