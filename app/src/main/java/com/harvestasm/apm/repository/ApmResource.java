package com.harvestasm.apm.repository;

import com.harvestasm.apm.repository.model.ApmConnectData;
import com.harvestasm.apm.repository.model.ApmConnectResponse;
import com.harvestasm.apm.repository.model.ApmConnectSearchResponse;
import com.harvestasm.apm.repository.model.ApmDataSearchResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by yangfeng on 2017/12/5.
 */
public interface ApmResource {
    @Headers({ "Accept: application/json" })
    @GET("mobile/data/_search")
    Call<ApmDataSearchResponse> mobileDataSearch(
//            @Query("id") String id,
//            @Query("page") int page,
            @Query("size") int size
    );

    @POST("/mobile/connect")
    Call<ApmConnectResponse> mobileConnect(@Body ApmConnectData data);

    @Headers({ "Accept: application/json" })
    @GET("mobile/_search")
    Call<ApmDataSearchResponse> mobileAllSearch(
            @Query("size") int size
    );

    @Headers({ "Accept: application/json" })
    @GET("mobile/connect/_search")
    Call<ApmConnectSearchResponse> mobileConnectSearch(
            @Query("size") int size
    );
}
