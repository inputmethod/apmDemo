package com.harvestasm.apm.home;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.graphics.Typeface;
import android.util.Log;

import com.harvestasm.apm.reporter.ApmConnectSourceIndex;
import com.harvestasm.apm.reporter.ApmDataSourceIndex;
import com.harvestasm.apm.reporter.ApmSourceGroup;
import com.harvestasm.apm.repository.ApmRepository;
import com.harvestasm.apm.repository.model.ApmSourceData;
import com.harvestasm.apm.repository.model.search.ApmBaseSearchResponse;
import com.harvestasm.apm.repository.model.search.ApmConnectSearchResponse;
import com.harvestasm.apm.repository.model.search.ApmDataSearchResponse;
import com.harvestasm.apm.utils.ApmRepositoryHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

// todo: simplest implement without repository to store data item.
public class HomeModel extends ViewModel {
    private final static String TAG = HomeModel.class.getSimpleName();

    private final ApmRepository repository = new ApmRepository();

    public final MutableLiveData<List<HomeItem>> items = new MutableLiveData<>();
    public final MutableLiveData<Boolean> refreshState = new MutableLiveData<>();
    public final MutableLiveData<Integer> networkState = new MutableLiveData<>();

    public final MutableLiveData<HomeItem> clickItem = new MutableLiveData<>();

    private ApmConnectSearchResponse connectResponse = null;
    private ApmBaseSearchResponse<ApmSourceData> dataResponse = null;

    public void load(final Typeface typeface) {
        resetForLoading();

        final ApmRepositoryHelper.CallBack callBack = new ApmRepositoryHelper.CallBack() {
            @Override
            public void onConnectResponse(ApmConnectSearchResponse responseBody) {
                connectResponse = responseBody;
                checkResult(typeface);
            }

            @Override
            public void onDataResponse(ApmDataSearchResponse responseBody) {
                dataResponse = responseBody;
                checkResult(typeface);
            }
        };

        ApmRepositoryHelper.doLoadTask(repository, callBack);
    }

    private void resetForLoading() {
        refreshState.setValue(true);
        networkState.postValue(0);

        connectResponse = null;
        dataResponse = null;
    }

    private void onDataLoaded(List<HomeItem> list) {
        items.setValue(list);

        refreshState.setValue(false);
        networkState.postValue(0);

    }

    private void checkResult(Typeface typeface) {
        if (null == connectResponse || null == dataResponse) {
            Log.i(TAG, "checkResult, skip and wait until all data loaded.");
            return;
        }

        ApmConnectSourceIndex connectSourceIndex = new ApmConnectSourceIndex(connectResponse);
        ApmDataSourceIndex dataSourceIndex = new ApmDataSourceIndex(dataResponse);
        List<ApmSourceGroup> deviceGroupList = ApmSourceGroup.parseSourceGroup(dataSourceIndex, connectSourceIndex);

        List<HomeItem> list = new ArrayList<>();

        // parse device info to app, os and hardware info
        list.addAll(from(deviceGroupList));

        // final result list.
        onDataLoaded(list);
    }

    // parse device info to app, os and hardware info
    private Collection<? extends HomeItem> from(List<ApmSourceGroup> deviceGroupList) {
        ArrayList<HomeItem> itemList = new ArrayList<>();
        for (ApmSourceGroup group : deviceGroupList) {
            itemList.add(new HomeDeviceItem(group));
        }
        return itemList;
    }
}
