package com.harvestasm.apm.add;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.harvestasm.apm.home.HomeDeviceItem;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

public class AddViewModel extends ViewModel {
    private static final String TAG = AddViewModel.class.getSimpleName();

    public MutableLiveData<List<HomeDeviceItem.AppItem>> getImeAppLiveData() {
        return AddDataStorage.get().appListLiveData;
    }

    // Flowable.fromFuture() 在非主线程执行Callable对象
    public void loadImeMethods() {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        Callable<List<HomeDeviceItem.AppItem>> callable = new Callable<List<HomeDeviceItem.AppItem>>() {
            @Override
            public List<HomeDeviceItem.AppItem> call() {
                return AddDataStorage.get().createImeAppList();
            }
        };

        Future<List<HomeDeviceItem.AppItem>> future = executor.submit(callable);

        Consumer<List<HomeDeviceItem.AppItem>> onNext = new Consumer<List<HomeDeviceItem.AppItem>>() {
            @Override
            public void accept(List<HomeDeviceItem.AppItem> appItems) {
                Log.e(TAG, "onNext thread " + Thread.currentThread().getName());
                getImeAppLiveData().setValue(appItems);
            }
        };

//        Flowable.fromCallable(callable).subscribe(onNext);
        Flowable.fromFuture(future).subscribe(onNext);

//        Flowable.just(AddDataStorage.get().createImeAppList())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(onNext);
    }
}
