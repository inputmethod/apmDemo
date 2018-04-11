package com.harvestasm.apm.home;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.graphics.Typeface;

import java.util.ArrayList;
import java.util.List;

// todo: simplest implement without repository to store data item.
public class HomeModel extends ViewModel {
    private final static String TAG = HomeModel.class.getSimpleName();

    public final MutableLiveData<List<HomeItem>> items = new MutableLiveData<>();
    public final MutableLiveData<Boolean> loadingState = new MutableLiveData<>();
    public final MutableLiveData<Integer> networkState = new MutableLiveData<>();

    public final MutableLiveData<HomeItem> clickItem = new MutableLiveData<>();

    public void load(Typeface typeface) {
        loadingState.setValue(true);
        startLoadWorker(typeface);
    }

    // todo: load within worker thread.
    private void startLoadWorker(Typeface typeface) {
        List<HomeItem> list = new ArrayList<>();
        fillSampleCharItem(list, typeface);
        items.postValue(list);
        loadingState.postValue(false);
        networkState.postValue(0);
    }

    private void fillSampleCharItem(List<HomeItem> list, Typeface typeface) {
    }
}
