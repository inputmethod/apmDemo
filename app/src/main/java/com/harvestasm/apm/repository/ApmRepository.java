package com.harvestasm.apm.repository;

import com.harvestasm.apm.repository.base.BaseRepository;
import com.harvestasm.apm.repository.model.ApmSourceConnect;
import com.harvestasm.apm.repository.model.connect.ApmConnectResponse;
import com.harvestasm.apm.repository.model.search.ApmCommonSearchResponse;
import com.harvestasm.apm.repository.model.search.ApmConnectSearchResponse;
import com.harvestasm.apm.repository.model.search.ApmDataSearchResponse;

import typany.apm.com.google.gson.JsonElement;
import typany.apm.retrofit2.Call;
import typany.apm.retrofit2.Response;

/**
 * Created by yangfeng on 2017/12/5.
 */

// http://10.134.73.228/api/soundres?proto=1
public class ApmRepository extends BaseRepository<ApmResource> {
    private static final String APM_COLLECTOR_BASE_URL = "http://10.135.71.155:9200/";
    private static final int MAX_QUERY_COUNT = 10000;

    private static final String INDEX_MOBILE = "mobile";
    private static final String INDEX_DEMO = "apmdemo";
    private static final String INDEX_TEST = "apmtest";

    public ApmRepository() {
        super(APM_COLLECTOR_BASE_URL, ApmResource.class, new ApmRequestInterceptor());
    }

    public Call<ApmDataSearchResponse> mobileDataSearch() {
        return getSource().dataSearch(INDEX_MOBILE, MAX_QUERY_COUNT);
    }

    public ApmConnectResponse mobileConnect(ApmSourceConnect data) throws Exception {
        Call<ApmConnectResponse> call = getSource().connect(INDEX_MOBILE, data);
        Response<ApmConnectResponse> response = call.execute();
        return response.body();
    }

    public Call<ApmCommonSearchResponse> mobileAllSearch() {
        return getSource().allSearch(INDEX_MOBILE, MAX_QUERY_COUNT);
    }

    public Call<ApmConnectSearchResponse> mobileConnectSearch() {
        return getSource().connectSearch(INDEX_MOBILE, MAX_QUERY_COUNT);
    }


    public ApmConnectResponse apmTestConnect(JsonElement data) throws Exception {
        Call<ApmConnectResponse> call = getSource().connect(INDEX_TEST, data);
        Response<ApmConnectResponse> response = call.execute();
        return response.body();
    }
}
