package com.harvestasm.apm.setup;

import android.arch.lifecycle.Observer;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.util.Log;

import com.harvestasm.apm.add.AddDataStorage;
import com.harvestasm.apm.base.BaseViewModel;

import typany.apm.agent.android.harvest.DeviceInformation;

// todo: simplest implement without repository to store data item.
public class SetupFragmentViewModel extends BaseViewModel<DeviceInformation> {
    private final static String TAG = SetupFragmentViewModel.class.getSimpleName();

//    public final MutableLiveData<DeviceInformation> items = new MutableLiveData<>();
//    public final MutableLiveData<Boolean> refreshState = new MutableLiveData<>();
//    public final MutableLiveData<Integer> networkState = new MutableLiveData<>();

//    @MainThread
//    private void resetForLoading() {
//        refreshState.setValue(true);
//        networkState.postValue(0);
//    }
//
//    @MainThread
//    private void onDataLoaded(DeviceInformation item) {
//        items.setValue(item);
//
//        refreshState.setValue(false);
//        networkState.postValue(0);
//
//    }
//
//    @MainThread
//    public void showOptions() {
//        AddDataStorage.get().nextStepState.setValue(1);
//    }

    @MainThread
    public void load() {
        resetForLoading();

        AddDataStorage.get().hardwareLiveData.observeForever(new Observer<DeviceInformation>() {
            @Override
            public void onChanged(@Nullable DeviceInformation hardwareItem) {
                AddDataStorage.get().hardwareLiveData.removeObserver(this);
                if (null != hardwareItem) {
                    Log.i(TAG, "load, with local device: " + hardwareItem.getManufacturer());
                    onDataLoaded(hardwareItem);
                }
            }
        });
        AddDataStorage.get().getDeviceInfoFeature();
    }
}
