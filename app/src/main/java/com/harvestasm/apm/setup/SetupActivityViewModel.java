package com.harvestasm.apm.setup;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.MainThread;

import com.harvestasm.apm.add.AddDataStorage;

public class SetupActivityViewModel extends ViewModel {
    private static final String TAG = SetupActivityViewModel.class.getSimpleName();
    public static final int SETUP_NOTICE = 0;
    public static final int SETUP_PREVIEW = 1;

    @MainThread
    public void showNotice(){
        AddDataStorage.get().nextStepState.setValue(SETUP_NOTICE);
    }

    @MainThread
    public void startObserve(LifecycleOwner owner, Observer<Integer> observer) {
        AddDataStorage.get().nextStepState.observe(owner, observer);
        showNotice();
    }

    public boolean toggleNext() {
        if (AddDataStorage.get().nextStepState.getValue().intValue() == 0) {
            AddDataStorage.get().nextStepState.setValue(SETUP_PREVIEW);
            return true;
        }
        return false;
    }
}
