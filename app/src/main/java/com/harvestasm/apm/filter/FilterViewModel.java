package com.harvestasm.apm.filter;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.harvestasm.apm.browser.DataStorage;
import com.harvestasm.apm.filter.item.FilterItemModel;
import com.harvestasm.apm.repository.model.ApmSourceData;
import com.harvestasm.apm.repository.model.search.ApmBaseSearchResponse;
import com.harvestasm.apm.repository.model.search.ApmConnectSearchResponse;

import java.util.ArrayList;
import java.util.List;

// todo: simplest implement without repository to store data item.
public class FilterViewModel extends ViewModel {
    private final static String TAG = FilterViewModel.class.getSimpleName();

    public final MutableLiveData<List<FilterItemModel>> items = new MutableLiveData<>();
    public final MutableLiveData<Boolean> refreshState = new MutableLiveData<>();
    public final MutableLiveData<Integer> networkState = new MutableLiveData<>();

    private ApmConnectSearchResponse connectResponse = null;
    private ApmBaseSearchResponse<ApmSourceData> dataResponse = null;

    private final List<FilterItemModel> list = new ArrayList<>();

    private void resetForLoading() {
        refreshState.setValue(true);
        networkState.postValue(0);

        connectResponse = null;
        dataResponse = null;
        list.clear();
    }

    private void onDataLoaded(List<FilterItemModel> list) {
        items.setValue(list);

        refreshState.setValue(false);
        networkState.postValue(0);

    }

    public void load(final boolean full) {
        resetForLoading();

        // FilterViewModel在BrowserViewModel完成加载后显示，数据已经加载在DataStorage，直接读
        checkResult();
    }

    private void checkResult() {
        list.addAll(DataStorage.get().queryFilterList());

        // final result list.
        onDataLoaded(list);
    }

    public boolean isSelect(String category, String name) {
        return DataStorage.get().isSelect(category, name);
    }

    public void toggleSelected(String category, String name) {
        DataStorage.get().toggleSelected(category, name);
    }
}
