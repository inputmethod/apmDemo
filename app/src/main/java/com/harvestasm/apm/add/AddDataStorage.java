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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import typany.apm.agent.android.Agent;
import typany.apm.agent.android.AgentInitializationException;
import typany.apm.agent.android.harvest.DeviceInformation;

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

    // 加载本地、云端Typany版本及其它竞品ime版本的LiveData, View通过ViewModel读取并进行observe变化
    public final MutableLiveData<List<HomeDeviceItem.AppItem>> appListLiveData = new MutableLiveData<>();
    public final MutableLiveData<DeviceInformation> hardwareLiveData = new MutableLiveData<>();

    // 已经选中的Typany和竞品ime版本列表
    public final Set<HomeDeviceItem.AppItem> selectedImeAppList = new HashSet<>();
    private final Set<String> localCheckedImeList = new HashSet<>();

    // 下一步操作
    public final MutableLiveData<Integer> nextStepState = new MutableLiveData<>();

    private boolean checkToInitSelected() {
        if (selectedImeAppList.isEmpty()) {
            localCheckedImeList.clear();
            for (String imeName : IMEHelper.getCheckedImeList(context)) {
                localCheckedImeList.add(imeName.split("/")[0]);
            }
            return true;
        }
        return false;
    }

    @WorkerThread
    private List<HomeDeviceItem.AppItem> createImeAppList() {
        Log.e(TAG, "createImeAppList thread " + Thread.currentThread().getName());

        checkToInitSelected();

        List<HomeDeviceItem.AppItem> informationList = new ArrayList<>();
        for (String name : IMEHelper.getInstallImePackageList(context)) {
            try {
                HomeDeviceItem.AppItem item = from(name);
                if (localCheckedImeList.contains(name)) {
                    selectedImeAppList.add(item);
                    informationList.add(0, item);
                } else {
                    informationList.add(item);
                }
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

    public void getDeviceInfoFeature(int nThreads) {
        ExecutorService executor = Executors.newFixedThreadPool(nThreads);
        Callable<DeviceInformation> callable = new Callable<DeviceInformation>() {
            @Override
            public DeviceInformation call() {
                return Agent.getDeviceInformation();
            }
        };

        Future<DeviceInformation> future = executor.submit(callable);

        // Flowable.fromFuture() 在非主线程执行Callable对象
        // Flowable.fromCallable(callable).subscribe(onNext);
        Flowable.fromFuture(future).subscribe(new Consumer<DeviceInformation>() {
            @Override
            public void accept(DeviceInformation deviceInformation) {
                Log.e(TAG, "Consumer.accept in thread " + Thread.currentThread().getName());
//                HomeDeviceItem.HardwareItem hardwareItem = new HomeDeviceItem.HardwareItem();
//                hardwareItem.setHwModel(deviceInformation.getModel());
//                hardwareItem.setHwVendor(deviceInformation.getManufacturer());
//                hardwareItem.setHwId(deviceInformation.getDeviceId());
                hardwareLiveData.setValue(deviceInformation);
            }
        });
    }
}
