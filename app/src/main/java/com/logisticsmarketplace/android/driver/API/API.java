package com.logisticsmarketplace.android.driver.API;

import com.logisticsmarketplace.android.driver.Model.BackgroundData.BackgroundData;
import com.logisticsmarketplace.android.driver.Model.Default.DataMessage;
import com.logisticsmarketplace.android.driver.Model.Driver.Driver;
import com.logisticsmarketplace.android.driver.Model.Driver.DriverResponse;
import com.logisticsmarketplace.android.driver.Model.History.HistoryResponse;
import com.logisticsmarketplace.android.driver.Model.JobOrder.JobOrderData;
import com.logisticsmarketplace.android.driver.Model.JobOrder.JobOrderResponse;
import com.logisticsmarketplace.android.driver.Model.JobOrderUpdate.JobOrderUpdateCreation;
import com.logisticsmarketplace.android.driver.Model.JobOrderUpdate.JobOrderUpdateData;
import com.logisticsmarketplace.android.driver.Model.JobOrderUpdate.JobOrderUpdateImage;
import com.logisticsmarketplace.android.driver.Model.JobOrderUpdate.JobOrderUpdateResponse;
import com.logisticsmarketplace.android.driver.Model.Login.DriverLogin;
import com.logisticsmarketplace.android.driver.Model.ProfilDriver.ProfilResponse;

import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface API {
    //    public final String BASE_URL = "http://172.104.166.12";
    public final String BASE_URL = "http://172.104.163.118";

    //JOB ORDER UPDATE
    @GET("/api/resource/Job Order Update?fields=[\"name\",\"waktu\",\"lo\",\"lat\",\"note\",\"job_order\",\"docstatus\",\"status\",\"vendor\",\"principle\"]")
    Call<JobOrderUpdateResponse> getJOUpdate(@Query("filters") String filters, @Query("limit_page_length") String limit);
    @POST("/api/resource/Job Order Update")
    Call<JobOrderUpdateCreation> insertUpdateJO(@Body JobOrderUpdateData jobOrderUpdateData);

    //JOB ORDER UPDATE IMAGE
//    @Multipart
//    Call<ResponseBody> uploadImage(@Part("jouid") RequestBody jobOrderUpdateData, @Part("file") MultipartBody.Part file);
//    @Headers("Content-Type:application/json")
//    @POST("/api/resource/Job Order Update Image")
    @POST("/api/method/logistic_marketplace.job_order.image")
    Call<ResponseBody> uploadImage(@Body HashMap<String,String> data);

    //DRIVER
    @GET("/api/resource/Driver?fields=[\"name\",\"nama\",\"email\",\"address\",\"phone\"]")
    Call<DriverResponse> getDriver(@Query("filters") String filters);
    @POST("/api/resource/Driver")
    Call<JSONObject> registerDriver(@Body Driver newDriver);
    @PUT("/api/resource/Driver/{id}")
    Call<JSONObject> updateDriver(@Path("id") String id, @Body HashMap<String , String> change);

    //JOB ORDER
    @PUT("/api/resource/Job Order/{id}")
    Call<JSONObject> updateJobOrder(@Path("id") String id, @Body HashMap<String, String> change);

    @GET("/api/resource/Job Order?fields=[\"reference\",\"status\",\"name\", \"principle\",\"vendor\",\"pick_location\",\"delivery_location\",\"nama_principle_cp\",\"telp_principle_cp\",\"nama_vendor_cp\",\"telp_vendor_cp\",\"pick_date\",\"expected_delivery\",\"goods_information\",\"notes\",\"accept_date\",\"suggest_truck_type\",\"strict\",\"estimate_volume\",\"truck\",\"truck_type\",\"truck_volume\",\"driver\",\"kota_pengambilan\",\"alamat_pengambilan\",\"kode_distributor_pengambilan\",\"nama_gudang_pengambilan\",\"kota_pengiriman\",\"alamat_pengiriman\",\"kode_distributor_pengiriman\",\"nama_gudang_pengiriman\"]")
    Call<JobOrderResponse> getJobOrder(@Query("filters") String filters);

    @POST("/api/resource/Job Order")
    Call<JSONObject> submitJobOrder(@Body JobOrderData data);


    @GET("/api/resource/Driver?fields=[\"nama\",\"phone\",\"address\",\"email\"]")
    Call<ProfilResponse> getProfile(@Query("filters") String filter);

    //BACKGROUND
    @POST("/api/resource/Driver Background Update")
    Call<ResponseBody> updateBackground(@Body BackgroundData data);

    @GET("/api/method/logistic.job_order.history")
    Call<HistoryResponse> getHistory(@Query("jaid") String jaid);

    @GET("/api/method/login")
    Call<DriverLogin> login(@Query("usr") String usr, @Query("pwd") String pwd, @Query("device") String device);

    @GET("/api/method/logistic.job_order.list")
    Call<JobOrderResponse> getJobOrderList(@Query("type") String type, @Query("typeid") String typeid, @Query("status") String status);
}
