package huang.android.logistic_driver;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import huang.android.logistic_driver.API.API;
import huang.android.logistic_driver.Model.FirebaseID.FirebaseIDData;
import huang.android.logistic_driver.Model.FirebaseID.FirebaseIDResponse;
import huang.android.logistic_driver.Model.Login.DriverLogin;
import huang.android.logistic_driver.Model.Login.LoginUserPermission;
import huang.android.logistic_driver.Model.Login.LoginUserPermissionResponse;
import huang.android.logistic_driver.Model.MyCookieJar;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.utility.getLanguage(this);
        setContentView(R.layout.activity_login);
        loading=(ProgressBar)findViewById(R.id.loading);
    }

    String user,pw;
    SharedPreferences mPrefs;
    ProgressBar loading;

    public void clicklogin(View v) {

        EditText username= (EditText)findViewById(R.id.username);
        user= username.getText().toString();
        EditText password= (EditText)findViewById(R.id.password);
        pw= password.getText().toString();

        if(user.isEmpty()||pw.isEmpty()){
            Context c = getApplicationContext();
            Toast.makeText(c,"username or password cannot be empty", Toast.LENGTH_SHORT).show();
        }
        else {
            loading.setVisibility(View.VISIBLE);
            doLogin();
        }
    }


    public void doLogin() {
        final MyCookieJar cookieJar = new MyCookieJar();
        API api = Utility.utility.getAPIWithCookie(cookieJar);
        Call<DriverLogin> login = api.login(user,pw,"mobile");
        final Activity activity = this;
        login.enqueue(new Callback<DriverLogin>() {
            @Override
            public void onResponse(Call<DriverLogin> call, Response<DriverLogin> response) {

                loading.setVisibility(View.GONE);
                if(response.code() == 200) {
                    checkPermission(cookieJar);
                } else {
                    Toast.makeText(getApplicationContext(),"Username or password is invalid",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DriverLogin> call, Throwable throwable) {
                Utility.utility.showConnectivityWithError(getApplicationContext());
                loading.setVisibility(View.GONE);
            }
        });
    }

    void checkPermission(final MyCookieJar cookieJar) {
        API api = Utility.utility.getAPIWithCookie(cookieJar);
        Call<FirebaseIDResponse> loginUserPermissionResponseCall = api.getFirebaseID("Driver");
        final Activity activity = this;
        loginUserPermissionResponseCall.enqueue(new Callback<FirebaseIDResponse>() {
            @Override
            public void onResponse(Call<FirebaseIDResponse> call, Response<FirebaseIDResponse> response) {
                if (Utility.utility.catchResponse(getApplicationContext(), response, "")) {
                    FirebaseIDResponse firebaseIDResponse = response.body();
                    if (firebaseIDResponse != null) {
                        if (firebaseIDResponse.message.role.equals("valid")) {
                            List<FirebaseIDData> data = firebaseIDResponse.message.for_value;
                            if (data.size() > 0) {
                                Utility.utility.saveLoggedName(user, activity);
                                Utility.utility.saveUsername(user,activity);
                                Utility.utility.saveCookieJarToPreference(cookieJar, activity);
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(),"Username or password is invalid",Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(),"Username or password is invalid",Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(),"Username or password is invalid",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<FirebaseIDResponse> call, Throwable t) {
                Utility.utility.showConnectivityUnstable(getApplicationContext());
            }
        });
    }
}
