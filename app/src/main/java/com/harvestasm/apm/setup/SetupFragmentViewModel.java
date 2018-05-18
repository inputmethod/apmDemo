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
