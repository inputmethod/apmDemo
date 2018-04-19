package com.harvestasm.apm.add;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.harvestasm.apm.home.HomeDeviceItem;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class AddViewModel extends ViewModel {
    private static final String TAG = AddViewModel.class.getSimpleName();

    public MutableLiveData<List<HomeDeviceItem.AppItem>> getImeAppLiveData() {
        return AddDataStorage.get().appListLiveData;
    }

    public void loadImeMethods() {
        Flowable.just(AddDataStorage.get().createImeAppList())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<HomeDeviceItem.AppItem>>() {
                    @Override
                    public void accept(List<HomeDeviceItem.AppItem> appItems) {
                        Log.e(TAG, "accept thread " + Thread.currentThread().getName());
                        getImeAppLiveData().setValue(appItems);
                    }
                });
    }
}
