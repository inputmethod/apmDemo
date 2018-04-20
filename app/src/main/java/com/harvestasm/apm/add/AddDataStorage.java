package com.harvestasm.apm.add;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.WorkerThread;
import android.util.Log;

import com.harvestasm.apm.home.HomeDeviceItem;
import com.harvestasm.apm.repository.ApmRepository;
import com.harvestasm.apm.utils.IMEHelper;

import org.apache.http.util.Asserts;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import typany.apm.agent.android.AgentInitializationException;

public class AddDataStorage {
    private static final String TAG = AddDataStorage.class.getSimpleName();

    private final Context context;
    private static AddDataStorage _instance;

    public static final void init(Context context) {
        if (null == _instance) {
            _instance = new AddDataStorage(context.getApplicationContext());
        }
    }

    private AddDataStorage(Context context) {
        this.context = context;
    }

    public static AddDataStorage get() {
        Asserts.notNull(_instance, "AddDataStorage has not been initialized.");

        return _instance;
    }

    private final ApmRepository repository = new ApmRepository();

    public final MutableLiveData<List<HomeDeviceItem.AppItem>> appListLiveData = new MutableLiveData<>();

    @WorkerThread
    public List<HomeDeviceItem.AppItem> createImeAppList() {
        Log.e(TAG, "createImeAppList thread " + Thread.currentThread().getName());

        List<HomeDeviceItem.AppItem> informationList = new ArrayList<>();
        for (String name : IMEHelper.getInstallImePackageList(context)) {
            try {
                HomeDeviceItem.AppItem item = from(name);
                informationList.add(item);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return informationList;
    }

    private HomeDeviceItem.AppItem from(String packageName) throws AgentInitializationException {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo = null;

        try {
            packageInfo = packageManager.getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException var10) {
            throw new AgentInitializationException("Could not determine package version: " + var10.getMessage());
        }

        String appName;
        try {
            ApplicationInfo info = packageManager.getApplicationInfo(packageName, 0);
            appName = info != null ? packageManager.getApplicationLabel(info).toString() : packageName;
        } catch (PackageManager.NameNotFoundException var8) {
            appName = packageName;
        } catch (SecurityException var9) {
            appName = packageName;
        }

        HomeDeviceItem.AppItem item = new HomeDeviceItem.AppItem();
        item.setAppName(appName);
        item.setAppVersion(packageInfo.versionName);
        item.setAppPackage(packageName);
        item.setAppVersionCode(String.valueOf(packageInfo.versionCode));
        return item;
    }

    public void getImeListFeature(int nThreads) {
        ExecutorService executor = Executors.newFixedThreadPool(nThreads);
        Callable<List<HomeDeviceItem.AppItem>> callable = new Callable<List<HomeDeviceItem.AppItem>>() {
            @Override
            public List<HomeDeviceItem.AppItem> call() {
                return createImeAppList();
            }
        };

        Future<List<HomeDeviceItem.AppItem>> future = executor.submit(callable);

        // Flowable.fromFuture() 在非主线程执行Callable对象
        // Flowable.fromCallable(callable).subscribe(onNext);
        Flowable.fromFuture(future).subscribe(new Consumer<List<HomeDeviceItem.AppItem>>() {
            @Override
            public void accept(List<HomeDeviceItem.AppItem> appItems) {
                Log.e(TAG, "Consumer.accept in thread " + Thread.currentThread().getName());
                appListLiveData.setValue(appItems);
            }
        });
    }
}
