package com.harvestasm.apm.browser;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.MainThread;

public class BrowserActivityViewModel extends ViewModel {
    @MainThread
    public void startObserve(LifecycleOwner owner, Observer<Integer> observer) {
        DataStorage.get().currentState.observe(owner, observer);
        DataStorage.get().currentState.setValue(0);
    }

    public boolean toggleNext() {
        int current = DataStorage.get().currentState.getValue().intValue();
        if (current == 0) {
            DataStorage.get().currentState.setValue(1);
            return true;
        } else if (current == 1) {
            DataStorage.get().currentState.setValue(0);
            return true;
        }
        // todo:
        return false;
    }

    public void navigateBy(int id) {
        DataStorage.get().currentState.setValue(3);
    }
}
