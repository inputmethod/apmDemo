package com.harvestasm.apm.imepicker;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.harvestasm.apm.add.AddDataStorage;
import com.harvestasm.apm.base.BaseListViewModel;
import com.harvestasm.apm.base.pikcer.ActionModelInterface;
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
import java.util.Set;

import typany.apm.agent.android.harvest.ApplicationInformation;

// todo: simplest implement without repository to store data item.
public class ImePickerViewModel extends BaseListViewModel<ApplicationInformation>
        implements ActionModelInterface<ImeAppModel> {
    private final static String TAG = ImePickerViewModel.class.getSimpleName();

    private final ApmRepository repository = new ApmRepository();

//    public final MutableLiveData<List<ApplicationInformation>> items = new MutableLiveData<>();
//    public final MutableLiveData<Boolean> refreshState = new MutableLiveData<>();
//    public final MutableLiveData<Integer> networkState = new MutableLiveData<>();

    public final MutableLiveData<ApplicationInformation> clickItem = new MutableLiveData<>();

    private ApmConnectSearchResponse connectResponse = null;
    private ApmBaseSearchResponse<ApmSourceData> dataResponse = null;

    private final List<ApplicationInformation> list = new ArrayList<>();

    @Override
    protected void resetForLoading() {
        super.resetForLoading();
//        refreshState.setValue(true);
//        networkState.postValue(0);

        connectResponse = null;
        dataResponse = null;
        list.clear();
    }

//    private void onDataLoaded(List<ApplicationInformation> list) {
//        items.setValue(list);
//
//        refreshState.setValue(false);
//        networkState.postValue(0);
//
//    }

    public void load(final boolean full) {
        resetForLoading();

        AddDataStorage.get().appListLiveData.observeForever(new Observer<List<ApplicationInformation>>() {
            @Override
            public void onChanged(@Nullable List<ApplicationInformation> appItems) {
                AddDataStorage.get().appListLiveData.removeObserver(this);
                if (null != appItems) {
                    Log.i(TAG, "load, with local size: " + appItems.size());
                    list.addAll(appItems);
                }
                if (full) {
                    loadRemoteList();
                } else {
                    onDataLoaded(list);
                }
            }
        });
        AddDataStorage.get().getImeListFeature();
    }

    private void loadRemoteList() {
        final ApmRepositoryHelper.CallBack callBack = new ApmRepositoryHelper.CallBack() {
            @Override
            public void setTag(boolean autoSource) {
                // do nothing.
            }

            @Override
            public void onConnectResponse(ApmConnectSearchResponse responseBody) {
                connectResponse = responseBody;
                checkResult();
            }

            @Override
            public void onDataResponse(ApmDataSearchResponse responseBody) {
                dataResponse = responseBody;
                checkResult();
            }
        };

        ApmRepositoryHelper.doLoadTask(repository.apmTestConnectSearch(), repository.apmTestDataSearch(), callBack);
    }

    private void checkResult() {
        if (null == connectResponse || null == dataResponse) {
            Log.i(TAG, "checkResult, skip and wait until all data loaded.");
            return;
        }

        ApmConnectSourceIndex connectSourceIndex = new ApmConnectSourceIndex(connectResponse);

        // parse apps
        HashMap<String, List<ApmBaseUnit<ApmSourceConnect>>> connectUnits = connectSourceIndex.getAppIndexMap();
        ApmRepositoryHelper.parseApplicationList(list, connectUnits.keySet());

        // final result list.
        onDataLoaded(list);
    }

    public void performClick(ApplicationInformation item) {
        clickItem.setValue(item);
    }

    private boolean isSelect(ApplicationInformation myLive) {
        return AddDataStorage.get().selectedImeAppList.contains(myLive);
    }

    private void toggleSelected(ApplicationInformation myLive) {
        Set<ApplicationInformation> selectedList = AddDataStorage.get().selectedImeAppList;
        if (selectedList.contains(myLive)) {
            selectedList.remove(myLive);
        } else {
            selectedList.add(myLive);
        }
    }

    @Override
    public void toggleSelected(@NonNull ImeAppModel itemModel) {
        toggleSelected(itemModel.getApplicationInformation());
    }

    @Override
    public boolean isSelect(@NonNull ImeAppModel itemModel) {
        return isSelect(itemModel.getApplicationInformation());
    }
}
