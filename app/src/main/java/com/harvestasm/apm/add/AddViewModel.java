package com.harvestasm.apm.add;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import typany.apm.agent.android.harvest.ApplicationInformation;

public class AddViewModel extends ViewModel {
    private static final String TAG = AddViewModel.class.getSimpleName();

    public MutableLiveData<List<ApplicationInformation>> getImeAppLiveData() {
        return AddDataStorage.get().appListLiveData;
    }

    public void loadImeMethods() {
        AddDataStorage.get().getImeListFeature();
    }
}
