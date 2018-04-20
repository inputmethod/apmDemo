package com.harvestasm.apm.main;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.Nullable;
import android.util.Log;

import com.harvestasm.apm.add.AddDataStorage;

import typany.apm.agent.android.harvest.DeviceInformation;

// todo: simplest implement without repository to store data item.
public class SetupNoticeViewModel extends ViewModel {
    private final static String TAG = SetupNoticeViewModel.class.getSimpleName();

    public final MutableLiveData<DeviceInformation> items = new MutableLiveData<>();
    public final MutableLiveData<Boolean> refreshState = new MutableLiveData<>();
    public final MutableLiveData<Integer> networkState = new MutableLiveData<>();

    private void resetForLoading() {
        refreshState.setValue(true);
        networkState.postValue(0);
    }

    private void onDataLoaded(DeviceInformation item) {
        items.setValue(item);

        refreshState.setValue(false);
        networkState.postValue(0);

    }

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
        AddDataStorage.get().getDeviceInfoFeature(2);
    }
}
