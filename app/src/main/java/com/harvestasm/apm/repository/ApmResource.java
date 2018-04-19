package com.harvestasm.apm.repository;

import com.harvestasm.apm.repository.model.ApmSourceConnect;
import com.harvestasm.apm.repository.model.connect.ApmConnectResponse;
import com.harvestasm.apm.repository.model.search.ApmCommonSearchResponse;
import com.harvestasm.apm.repository.model.search.ApmConnectSearchResponse;
import com.harvestasm.apm.repository.model.search.ApmDataSearchResponse;

import typany.apm.com.google.gson.JsonElement;
import typany.apm.retrofit2.Call;
import typany.apm.retrofit2.http.Body;
import typany.apm.retrofit2.http.GET;
import typany.apm.retrofit2.http.Headers;
import typany.apm.retrofit2.http.POST;
import typany.apm.retrofit2.http.Path;
import typany.apm.retrofit2.http.Query;

/**
 * Created by yangfeng on 2017/12/5.
 */
public interface ApmResource {
//    @Headers({"Accept: application/json"})
//    @GET("{index}/{type}/_search")
//    Call search(@Path("index") String index, @Path("type") String type, @Query("size") int size);

    @Headers({ "Accept: application/json" })
    @GET("{index}/data/_search")
    Call<ApmDataSearchResponse> dataSearch(
            @Path("index") String index,
            @Query("size") int size
    );

    @POST("{index}/connect")
    Call<ApmConnectResponse> connect(
            @Path("index") String index,
            @Body ApmSourceConnect data);

    @POST("{index}/connect")
    Call<ApmConnectResponse> connect(
            @Path("index") String index,
            @Body JsonElement data);

    @POST("{index}/data")
    Call<ApmConnectResponse> data(
            @Path("index") String index,
            @Body String data);

    @Headers({ "Accept: application/json" })
    @GET("{index}/_search")
    Call<ApmCommonSearchResponse> allSearch(
            @Path("index") String index,
            @Query("size") int size
    );

    @Headers({ "Accept: application/json" })
    @GET("{index}/connect/_search")
    Call<ApmConnectSearchResponse> connectSearch(
            @Path("index") String index,
            @Query("size") int size
    );
}
