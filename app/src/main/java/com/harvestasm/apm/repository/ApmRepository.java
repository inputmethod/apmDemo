package com.harvestasm.apm.repository;

import com.harvestasm.apm.repository.base.BaseRepository;
import com.harvestasm.apm.repository.model.ApmSourceConnect;
import com.harvestasm.apm.repository.model.connect.ApmConnectResponse;
import com.harvestasm.apm.repository.model.search.ApmCommonSearchResponse;
import com.harvestasm.apm.repository.model.search.ApmConnectSearchResponse;
import com.harvestasm.apm.repository.model.search.ApmDataSearchResponse;

import typany.apm.retrofit2.Call;
import typany.apm.retrofit2.Response;

/**
 * Created by yangfeng on 2017/12/5.
 */

// http://10.134.73.228/api/soundres?proto=1
public class ApmRepository extends BaseRepository<ApmResource> {
    private static final String APM_COLLECTOR_BASE_URL = "http://10.135.71.155:9200/";
    private static final int MAX_QUERY_COUNT = 10000;

    public ApmRepository() {
        super(APM_COLLECTOR_BASE_URL, ApmResource.class, new ApmRequestInterceptor());
    }

    public ApmDataSearchResponse mobileDataSearch() throws Exception {
        Call<ApmDataSearchResponse> call = getSource().mobileDataSearch(MAX_QUERY_COUNT);
        Response<ApmDataSearchResponse> response = call.execute();
        return response.body();
    }

    public ApmConnectResponse mobileConnect(ApmSourceConnect data) throws Exception {
        Call<ApmConnectResponse> call = getSource().mobileConnect(data);
        Response<ApmConnectResponse> response = call.execute();
        return response.body();
    }

    public ApmCommonSearchResponse mobileAllSearch() throws Exception {
        Call<ApmCommonSearchResponse> call = getSource().mobileAllSearch(MAX_QUERY_COUNT);
        Response<ApmCommonSearchResponse> response = call.execute();
        return response.body();
    }

    public ApmConnectSearchResponse mobileConnectSearch() throws Exception {
        Call<ApmConnectSearchResponse> call = getSource().mobileConnectSearch(MAX_QUERY_COUNT);
        Response<ApmConnectSearchResponse> response = call.execute();
        return response.body();
    }
}
