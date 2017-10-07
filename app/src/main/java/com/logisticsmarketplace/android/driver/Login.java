package com.logisticsmarketplace.android.driver;

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

import com.logisticsmarketplace.android.driver.API.API;
import com.logisticsmarketplace.android.driver.Model.Login.DriverLogin;
import com.logisticsmarketplace.android.driver.Model.MyCookieJar;

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
                if(Utility.utility.catchResponse(getApplicationContext(), response)) {
                    DriverLogin driverLogin = response.body();
                    Utility.utility.saveLoggedName(user, activity);

                    Utility.utility.saveCookieJarToPreference(cookieJar, activity);
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    finish();
                }
            }

            @Override
            public void onFailure(Call<DriverLogin> call, Throwable throwable) {
                Utility.utility.showConnectivityWithError(getApplicationContext());
                loading.setVisibility(View.GONE);
            }
        });
    }
}
