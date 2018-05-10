package com.harvestasm.apm.utils;

import android.util.Log;

import com.harvestasm.apm.repository.model.search.ApmConnectSearchResponse;
import com.harvestasm.apm.repository.model.search.ApmDataSearchResponse;

import java.util.List;
import java.util.Set;

import typany.apm.agent.android.harvest.ApplicationInformation;
import typany.apm.retrofit2.Call;
import typany.apm.retrofit2.Callback;
import typany.apm.retrofit2.Response;

import static android.content.ContentValues.TAG;

public class ApmRepositoryHelper {
    private static final String DELIMITER_COMMA = ",";

    public static void doLoadTask(Call<ApmConnectSearchResponse> connectCall,
                                  Call<ApmDataSearchResponse> dataCall, final CallBack callBack) {
        connectCall.enqueue(new Callback<ApmConnectSearchResponse>() {
            @Override
            public void onResponse(Call<ApmConnectSearchResponse> call, Response<ApmConnectSearchResponse> response) {
                callBack.onConnectResponse(response.body());
            }

            @Override
            public void onFailure(Call<ApmConnectSearchResponse> call, Throwable throwable) {
                Log.e(TAG, "repository.connectSearch() failed " + throwable.getMessage());
            }
        });

        dataCall.enqueue(new Callback<ApmDataSearchResponse>() {
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

    public static void parseApplicationList(List<ApplicationInformation> informationList, Set<String> appSet) {
        for (String app : appSet) {
            String[] segments = app.split(DELIMITER_COMMA);
            ApplicationInformation appItem = new ApplicationInformation(segments[0], segments[1], segments[2], segments[1]);
            informationList.add(appItem);
        }
    }
}
