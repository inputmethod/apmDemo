package com.harvestasm.apm.main;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.MainThread;

import com.harvestasm.apm.add.AddDataStorage;

public class SetupActivityViewModel extends ViewModel {
    private static final String TAG = SetupActivityViewModel.class.getSimpleName();

    @MainThread
    public void showOptions() {
        AddDataStorage.get().nextStepState.setValue(1);
    }

    public void clickOption(int id) {
        AddDataStorage.get().nextStepState.setValue(id);
    }

    public void startObserve(LifecycleOwner owner, Observer<Integer> observer) {
        AddDataStorage.get().nextStepState.observe(owner, observer);
        AddDataStorage.get().nextStepState.setValue(0);
    }
}
