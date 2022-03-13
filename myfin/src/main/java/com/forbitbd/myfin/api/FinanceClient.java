package com.forbitbd.myfin.api;

import com.forbitbd.myfin.models.Account;
import com.forbitbd.myfin.models.Dashboard;
import com.forbitbd.myfin.models.TransactionResponse;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface FinanceClient {

    @GET
    Call<ResponseBody> getImage(@Url String url);

    @GET("/civil/api/projects/{project_id}/transactions/dashboard")
    Call<Dashboard> getDashboard(@Path("project_id") String projectId);

    @POST("/civil/api/projects/{project_id}/transactions")
    @Multipart
    Call<TransactionResponse> saveTransaction(@Path("project_id") String projectId,
                                              @Part MultipartBody.Part file,
                                              @PartMap() Map<String, RequestBody> partMap);

    @PUT("/civil/api/projects/{project_id}/transactions/{transaction_id}")
    @Multipart
    Call<TransactionResponse> updateTransaction(@Path("project_id") String projectId,
                                                @Path("transaction_id") String transaction_id,
                                                @Part MultipartBody.Part file,
                                                @PartMap() Map<String, RequestBody> partMap);

    @DELETE("/civil/api/projects/{project_id}/transactions/{transaction_id}")
    Call<Void> deleteTransaction(@Path("project_id") String project_id, @Path("transaction_id") String transaction_id);

    @DELETE("/civil/api/projects/{project_id}/accounts/{account_id}")
    Call<Void> deleteAccount(@Path("project_id") String project_id, @Path("account_id") String account_id);
    @POST("/civil/api/projects/{project_id}/accounts")
    Call<Account> saveAccount(@Path("project_id") String projectId, @Body Account account);

    @PUT("/civil/api/projects/{project_id}/accounts/{account_id}")
    Call<Account> updateAccount(@Path("project_id") String project_id, @Path("account_id") String account_id, @Body Account account);

}
