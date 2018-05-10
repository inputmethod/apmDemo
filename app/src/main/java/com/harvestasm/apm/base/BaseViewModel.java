package com.harvestasm.apm.base;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.CallSuper;

public class BaseViewModel<T> extends ViewModel {
    public final MutableLiveData<T> items = new MutableLiveData<>();
    public final MutableLiveData<Boolean> refreshState = new MutableLiveData<>();
    public final MutableLiveData<Integer> networkState = new MutableLiveData<>();

    @CallSuper
    protected void resetForLoading() {
        refreshState.setValue(true);
        networkState.postValue(0);

//        list.clear();
    }

    @CallSuper
    protected void onDataLoaded(T data) {
        items.setValue(data);

        refreshState.setValue(false);
        networkState.postValue(0);
    }
}
