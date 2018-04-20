package com.harvestasm.apm.add;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.harvestasm.apm.home.HomeDeviceItem;

import java.util.List;

public class AddViewModel extends ViewModel {
    private static final String TAG = AddViewModel.class.getSimpleName();

    public MutableLiveData<List<HomeDeviceItem.AppItem>> getImeAppLiveData() {
        return AddDataStorage.get().appListLiveData;
    }

    public void loadImeMethods() {
        AddDataStorage.get().getImeListFeature(2);
    }
}
