package com.harvestasm.apm.utils;

import android.util.Log;

import com.harvestasm.apm.repository.ApmRepository;
import com.harvestasm.apm.repository.model.search.ApmConnectSearchResponse;
import com.harvestasm.apm.repository.model.search.ApmDataSearchResponse;

import typany.apm.retrofit2.Call;
import typany.apm.retrofit2.Callback;
import typany.apm.retrofit2.Response;

import static android.content.ContentValues.TAG;

public class ApmRepositoryHelper {
    public static void doLoadTask(ApmRepository repository, final CallBack callBack) {
        repository.apmTestConnectSearch().enqueue(new Callback<ApmConnectSearchResponse>() {
            @Override
            public void onResponse(Call<ApmConnectSearchResponse> call, Response<ApmConnectSearchResponse> response) {
                callBack.onConnectResponse(response.body());
            }

            @Override
            public void onFailure(Call<ApmConnectSearchResponse> call, Throwable throwable) {
                Log.e(TAG, "repository.connectSearch() failed " + throwable.getMessage());
            }
        });

        repository.apmTestDataSearch().enqueue(new Callback<ApmDataSearchResponse>() {
            @Override
            public void onResponse(Call<ApmDataSearchResponse> call, Response<ApmDataSearchResponse> response) {
                callBack.onDataResponse(response.body());
            }

            @Override
            public void onFailure(Call<ApmDataSearchResponse> call, Throwable throwable) {
                Log.e(TAG, "repository.dataSearch() failed " + throwable.getMessage());
            }
        });
    }

    public interface CallBack {
        void onConnectResponse(ApmConnectSearchResponse body);
        void onDataResponse(ApmDataSearchResponse body);
    }

    public interface RefreshInterface {
        void onRefresh();
    }
}
