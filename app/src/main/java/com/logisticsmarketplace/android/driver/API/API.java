package com.logisticsmarketplace.android.driver.API;

import com.logisticsmarketplace.android.driver.Model.Driver.Driver;
import com.logisticsmarketplace.android.driver.Model.Driver.DriverResponse;
import com.logisticsmarketplace.android.driver.Model.History.HistoryResponse;
import com.logisticsmarketplace.android.driver.Model.JobOrder.JobOrderData;
import com.logisticsmarketplace.android.driver.Model.JobOrder.JobOrderResponse;
import com.logisticsmarketplace.android.driver.Model.JobOrderUpdate.JobOrderUpdateData;
import com.logisticsmarketplace.android.driver.Model.JobOrderUpdate.JobOrderUpdateResponse;
import com.logisticsmarketplace.android.driver.Model.Login.DriverLogin;
import com.logisticsmarketplace.android.driver.Model.ProfilDriver.ProfilResponse;

import org.json.JSONObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface API {
    public final String BASE_URL = "http://172.104.166.12";

    //JOB ORDER UPDATE
    @GET("/api/resource/Job Order Update?fields=[\"name\",\"waktu\",\"lo\",\"lat\",\"note\",\"job_order\",\"docstatus\",\"status\",\"vendor\",\"principle\"]")
    Call<JobOrderUpdateResponse> getJOUpdate(@Query("filters") String filters);
    @POST("/api/resource/Job Order Update")
    Call<JSONObject> insertUpdateJO(@Body JobOrderUpdateData jobOrderUpdateData);

    //DRIVER
    @GET("/api/resource/Driver?fields=[\"name\",\"nama\",\"email\",\"address\",\"phone\"]")
    Call<DriverResponse> getDriver(@Query("filters") String filters);
    @POST("/api/resource/Driver")
    Call<JSONObject> registerDriver(@Body Driver newDriver);

    //JOB ORDER
    @PUT("/api/resource/Job Order/{id}")
    Call<JSONObject> updateJobOrder(@Path("id") String id, @Body HashMap<String, String> change);

    @GET("/api/resource/Job Order?fields=[\"status\",\"name\", \"principle\",\"vendor\",\"pick_location\",\"delivery_location\",\"nama_principle_cp\",\"telp_principle_cp\",\"nama_vendor_cp\",\"telp_vendor_cp\",\"pick_date\",\"expected_delivery\",\"goods_information\",\"notes\"]")
    Call<JobOrderResponse> getJobOrder(@Query("filters") String filters);

    @POST("/api/resource/Job Order")
    Call<JSONObject> submitJobOrder(@Body JobOrderData data);


    //old
    @GET("/api/resource/Vendor?fields=[\"nama\",\"telp\",\"alamat\",\"email\"]")
    Call<ProfilResponse> getProfile(@Query("filters") String filter);


    @GET("/api/method/logistic.job_order.history")
    Call<HistoryResponse> getHistory(@Query("jaid") String jaid);

    @GET("/api/method/login")
    Call<DriverLogin> login(@Query("usr") String usr, @Query("pwd") String pwd, @Query("device") String device);

    @GET("/api/method/logistic.job_order.list")
    Call<JobOrderResponse> getJobOrderList(@Query("type") String type, @Query("typeid") String typeid, @Query("status") String status);
}
