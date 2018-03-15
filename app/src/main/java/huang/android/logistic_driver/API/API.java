package huang.android.logistic_driver.API;

import huang.android.logistic_driver.Model.APILogData;
import huang.android.logistic_driver.Model.BackgroundData.BackgroundData;
import huang.android.logistic_driver.Model.Communication.CommunicationCreation;
import huang.android.logistic_driver.Model.Communication.CommunicationData;
import huang.android.logistic_driver.Model.Communication.CommunicationResponse;
import huang.android.logistic_driver.Model.Default.DataMessage;
import huang.android.logistic_driver.Model.Driver.Driver;
import huang.android.logistic_driver.Model.Driver.DriverBackgroundUpdateResponse;
import huang.android.logistic_driver.Model.Driver.DriverResponse;
import huang.android.logistic_driver.Model.History.HistoryResponse;
import huang.android.logistic_driver.Model.JobOrder.GetJobOrderResponse;
import huang.android.logistic_driver.Model.JobOrder.JobOrderData;
import huang.android.logistic_driver.Model.JobOrder.JobOrderMetaDataResponse;
import huang.android.logistic_driver.Model.JobOrder.JobOrderResponse;
import huang.android.logistic_driver.Model.JobOrderRoute.JobOrderRouteResponse;
import huang.android.logistic_driver.Model.JobOrderUpdate.JobOrderUpdateCreation;
import huang.android.logistic_driver.Model.JobOrderUpdate.JobOrderUpdateData;
import huang.android.logistic_driver.Model.JobOrderUpdate.JobOrderUpdateImage;
import huang.android.logistic_driver.Model.JobOrderUpdate.JobOrderUpdateResponse;
import huang.android.logistic_driver.Model.JobOrderUpdateImage.JobOrderUpdateImageResponse;
import huang.android.logistic_driver.Model.Login.DriverLogin;
import huang.android.logistic_driver.Model.Login.LoginUserPermissionResponse;
import huang.android.logistic_driver.Model.ProfilDriver.ProfilResponse;

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
import retrofit2.http.Url;


public interface API {
//    public final String BASE_URL = "http://172.104.49.99";
//    public final String BASE_URL = "http://172.104.163.118";
    public final String BASE_URL = "http://system.digitruk.com";

    //API Log
    @POST("/api/resource/API Log")
    Call<JSONObject> sendAPILog(@Body APILogData apiLogData);

    //JOB ORDER UPDATE IMAGE
    @GET
    Call<ResponseBody> getImage(@Url String image_link);
    @GET("/api/method/logistic_marketplace.api.get_image_jo_update")
    Call<JobOrderUpdateImageResponse> getJOUpdateImage(@Query("jod_name") String jod_name);

    //DRIVER BACKGROUND UPDATE
    @GET("/api/resource/Driver Background Update?fields=[\"lo\",\"lat\",\"last_update\"]&limit_page_length=1")
    Call<DriverBackgroundUpdateResponse> getBackgroundUpdate(@Query("filters") String filters);


    //JOB ORDER UPDATE
    @GET("/api/method/logistic_marketplace.api.get_job_order_update")
    Call<JobOrderUpdateResponse> getJOUpdate(@Query("job_order") String job_order);
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

    //COMMUNICATION
    @GET("/api/resource/Communication?fields=[\"creation\",\"sender\",\"sender_full_name\",\"content\"]&limit_page_length=1000")
    Call<CommunicationResponse> getComment(@Query("filters") String filters);
    @POST("/api/resource/Communication")
    Call<CommunicationCreation> insertComment(@Body CommunicationData communicationData);


    //JOB ORDER
    @PUT("/api/resource/Job Order/{id}")
    Call<JSONObject> updateJobOrder(@Path("id") String id, @Body HashMap<String, String> change);

    //JOB ORDER ROUTE
    @GET("/api/resource/Job Order Route?fields=[\"location\",\"warehouse_name\",\"distributor_code\",\"city\",\"address\",\"contact\",\"nama\",\"phone\",\"type\",\"item_info\",\"remark\",\"order_index\",\"job_order\"]&limit_page_length=1000")
    Call<JobOrderRouteResponse> getJobOrderRoute(@Query("filters") String filters);


//    @GET("/api/resource/Job Order?fields=[\"modified\",\"reference\",\"status\",\"name\",\"driver_nama\",\"driver_phone\", \"principle\",\"vendor\",\"pick_location\",\"delivery_location\",\"nama_principle_cp\",\"telp_principle_cp\",\"nama_vendor_cp\",\"telp_vendor_cp\",\"pick_date\",\"expected_delivery\",\"goods_information\",\"notes\",\"accept_date\",\"suggest_truck_type\",\"strict\",\"estimate_volume\",\"truck\",\"truck_lambung\",\"truck_type\",\"truck_volume\",\"driver\",\"kota_pengambilan\",\"alamat_pengambilan\",\"kode_distributor_pengambilan\",\"nama_gudang_pengambilan\",\"kota_pengiriman\",\"alamat_pengiriman\",\"kode_distributor_pengiriman\",\"nama_gudang_pengiriman\"]")
//    Call<JobOrderResponse> getJobOrder(@Query("filters") String filters,@Query("limit_start") String start);
    @GET("/api/method/logistic_marketplace.api.get_job_order")
    Call<GetJobOrderResponse> getJobOrder(@Query("status") String status, @Query("driver") String driver, @Query("ref") String ref, @Query("start") String start);
    @GET("/api/method/logistic_marketplace.api.get_job_order_count?role=driver")
    Call<JobOrderMetaDataResponse> getJobOrderCount(@Query("id") String id);
    @GET("/api/method/logistic_marketplace.api.get_job_order_by")
    Call<GetJobOrderResponse> getJobOrderBy(@Query("name") String name);

    @POST("/api/resource/Job Order")
    Call<JSONObject> submitJobOrder(@Body JobOrderData data);


    @GET("/api/method/logistic_marketplace.api.get_user")
    Call<ProfilResponse> getProfile(@Query("driver") String driver);

    //BACKGROUND
    @POST("/api/resource/Driver Background Update")
    Call<ResponseBody> updateBackground(@Body BackgroundData data);

    @GET("/api/method/logistic.job_order.history")
    Call<HistoryResponse> getHistory(@Query("jaid") String jaid);

    @GET("/api/method/login")
    Call<DriverLogin> login(@Query("usr") String usr, @Query("pwd") String pwd, @Query("device") String device);
    @GET("/api/resource/User Permission/?fields=[\"for_value\",\"allow\"]")
    Call<LoginUserPermissionResponse> loginPermission(@Query("filters") String filters);

    @GET("/api/method/logistic.job_order.list")
    Call<JobOrderResponse> getJobOrderList(@Query("type") String type, @Query("typeid") String typeid, @Query("status") String status);
}
