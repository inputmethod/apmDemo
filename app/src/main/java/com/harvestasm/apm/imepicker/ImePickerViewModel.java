package com.harvestasm.apm.imepicker;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.graphics.Typeface;
import android.util.Log;

import com.harvestasm.apm.home.HomeDeviceItem;
import com.harvestasm.apm.reporter.ApmConnectSourceIndex;
import com.harvestasm.apm.repository.ApmRepository;
import com.harvestasm.apm.repository.model.ApmSourceConnect;
import com.harvestasm.apm.repository.model.ApmSourceData;
import com.harvestasm.apm.repository.model.search.ApmBaseSearchResponse;
import com.harvestasm.apm.repository.model.search.ApmBaseUnit;
import com.harvestasm.apm.repository.model.search.ApmConnectSearchResponse;
import com.harvestasm.apm.repository.model.search.ApmDataSearchResponse;
import com.harvestasm.apm.utils.ApmRepositoryHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// todo: simplest implement without repository to store data item.
public class ImePickerViewModel extends ViewModel {
    private final static String TAG = ImePickerViewModel.class.getSimpleName();

    private final ApmRepository repository = new ApmRepository();

    public final MutableLiveData<List<HomeDeviceItem.AppItem>> items = new MutableLiveData<>();
    public final MutableLiveData<Boolean> refreshState = new MutableLiveData<>();
    public final MutableLiveData<Integer> networkState = new MutableLiveData<>();

    public final MutableLiveData<HomeDeviceItem.AppItem> clickItem = new MutableLiveData<>();

    private ApmConnectSearchResponse connectResponse = null;
    private ApmBaseSearchResponse<ApmSourceData> dataResponse = null;

    private void resetForLoading() {
        refreshState.setValue(true);
        networkState.postValue(0);

        connectResponse = null;
        dataResponse = null;
    }

    private void onDataLoaded(List<HomeDeviceItem.AppItem> list) {
        items.setValue(list);

        refreshState.setValue(false);
        networkState.postValue(0);

    }

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

    private void checkResult(Typeface typeface) {
        if (null == connectResponse || null == dataResponse) {
            Log.i(TAG, "checkResult, skip and wait until all data loaded.");
            return;
        }

        ApmConnectSourceIndex connectSourceIndex = new ApmConnectSourceIndex(connectResponse);

        List<HomeDeviceItem.AppItem> list = new ArrayList<>();

        // parse apps
        HashMap<String, List<ApmBaseUnit<ApmSourceConnect>>> connectUnits = connectSourceIndex.getAppIndexMap();
//        list.add(new BarChartItem(generateDataBar(connectUnits), "App分布", HomeDeviceItem.AppItem.ID.STASTIC_BY_APP, typeface));
//        list.add(new PieChartItem(generateDataPie(connectUnits), "App分布",  HomeDeviceItem.AppItem.ID.STASTIC_BY_APP, typeface));
        HomeDeviceItem.parseAppItemList(list, connectUnits.keySet());

        // final result list.
        onDataLoaded(list);
    }

    public void performClick(HomeDeviceItem.AppItem item) {
        clickItem.setValue(item);
    }

    public boolean isSelect(HomeDeviceItem.AppItem myLive) {
        return false;
    }
}
