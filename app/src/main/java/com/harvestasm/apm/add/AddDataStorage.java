package com.harvestasm.apm.add;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.support.annotation.WorkerThread;
import android.util.Log;

import com.harvestasm.apm.repository.ApmRepository;
import com.harvestasm.apm.repository.model.connect.ApmConnectResponse;
import com.harvestasm.apm.utils.IMEHelper;
import com.harvestasm.apm.utils.SimpleFlowableService;

import org.apache.http.util.Asserts;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import typany.apm.agent.android.Agent;
import typany.apm.agent.android.harvest.ApplicationInformation;
import typany.apm.agent.android.harvest.ConnectInformation;
import typany.apm.agent.android.harvest.DeviceInformation;
import typany.apm.agent.android.harvest.HarvestData;
import typany.apm.agent.android.measurement.CustomMetricMeasurement;
import typany.apm.agent.android.tracing.ActivityTrace;
import typany.apm.agent.android.tracing.Sample;
import typany.apm.agent.android.tracing.Trace;
import typany.apm.agent.android.tracing.TraceMachine;
import typany.apm.agent.android.util.IMEApplicationHelper;

public class AddDataStorage {
    private static final String TAG = AddDataStorage.class.getSimpleName();

    private final Context context;
    private static AddDataStorage _instance;

    private final SimpleFlowableService simpleFlowableService = new SimpleFlowableService();
    public Disposable runWithFlowable(Callable callable, Consumer consumer) {
        return simpleFlowableService.runWithFlowable(callable, consumer);
    }

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
    public final MutableLiveData<List<ApplicationInformation>> appListLiveData = new MutableLiveData<>();
    public final MutableLiveData<DeviceInformation> hardwareLiveData = new MutableLiveData<>();

    // 已经选中的Typany和竞品ime版本列表
    public final Set<ApplicationInformation> selectedImeAppList = new HashSet<>();
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
    private List<ApplicationInformation> createImeAppList() {
        Log.e(TAG, "createImeAppList thread " + Thread.currentThread().getName());

        boolean noSelected = checkToInitSelected();

        List<ApplicationInformation> informationList = new ArrayList<>();
        for (String name : IMEHelper.getInstallImePackageList(context)) {
            try {
                ApplicationInformation item = IMEApplicationHelper.parseInstallImePackage(context, name);
                if (localCheckedImeList.contains(name)) {
                    if (noSelected) {
                        selectedImeAppList.add(item);
                    }
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

    public void getImeListFeature() {
        Callable<List<ApplicationInformation>> callable = new Callable<List<ApplicationInformation>>() {
            @Override
            public List<ApplicationInformation> call() {
                return createImeAppList();
            }
        };

        Consumer consumer = new Consumer<List<ApplicationInformation>>() {
            @Override
            public void accept(List<ApplicationInformation> appItems) {
                Log.e(TAG, "Consumer.accept in thread " + Thread.currentThread().getName());
                appListLiveData.setValue(appItems);
            }
        };

//        Future<List<ApplicationInformation>> future = executor.submit(callable);
//
//        // Flowable.fromFuture() 在非主线程执行Callable对象
//        // Flowable.fromCallable(callable).subscribe(onNext);
//        Flowable.fromFuture(future).subscribe(consumer);
        runWithFlowable(callable, consumer);
    }

    public void getDeviceInfoFeature() {
        Callable<DeviceInformation> callable = new Callable<DeviceInformation>() {
            @Override
            public DeviceInformation call() {
                return Agent.getDeviceInformation();
            }
        };
        Consumer consumer = new Consumer<DeviceInformation>() {
            @Override
            public void accept(DeviceInformation deviceInformation) {
                Log.e(TAG, "Consumer.accept in thread " + Thread.currentThread().getName());
//                HomeDeviceItem.HardwareItem hardwareItem = new HomeDeviceItem.HardwareItem();
//                hardwareItem.setHwModel(deviceInformation.getModel());
//                hardwareItem.setHwVendor(deviceInformation.getManufacturer());
//                hardwareItem.setHwId(deviceInformation.getDeviceId());
                hardwareLiveData.setValue(deviceInformation);
            }
        };

//        Future<DeviceInformation> future = executor.submit(callable);
//
//        // Flowable.fromFuture() 在非主线程执行Callable对象
//        // Flowable.fromCallable(callable).subscribe(onNext);
//        Flowable.fromFuture(future).subscribe(consumer);
        runWithFlowable(callable, consumer);
    }

    // todo: change without new Thread, call.enque() instead
    public void testData(final HarvestData harvestData) {
        runInThread(new Runnable() {
            @Override
            public void run() {
                try {
                    ApmConnectResponse response = repository.apmTestData(harvestData.toJson());
                    Log.d("mft", "That is it " + response.get_id());
                } catch (Exception ex) {
                    Log.d("mft", "testData exception: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        }, "testData");
    }

    // todo: change without new Thread
    public void testConnect(final ConnectInformation connectInformation) {
        runInThread(new Runnable() {
            @Override
            public void run() {
                try {
                    ApmConnectResponse response = repository.apmTestConnect(connectInformation.asELKJson());
                    Log.d("mft", "That is it " + response.get_id());
                } catch (Exception ex) {
                    Log.d("mft", "testConnect exception: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        }, "testConnect");
    }


    public List<ConnectInformation> getCachedConnection() {
        if (selectedImeAppList.isEmpty()) {
            return Collections.emptyList();
        }

        List<ConnectInformation> informationList = new ArrayList<>();
        DeviceInformation deviceInformation = Agent.getDeviceInformation();
        for (ApplicationInformation applicationInformation : selectedImeAppList) {
            ConnectInformation connectInformation = new ConnectInformation(applicationInformation, deviceInformation);
            informationList.add(connectInformation);
        }
        return informationList;
    }


    public List<HarvestData> getCachedData() {
        if (selectedImeAppList.isEmpty() || measurementByApp.isEmpty()) {
            return Collections.emptyList();
        }

        assert (selectedImeAppList.size() == measurementByApp.size());

        final DeviceInformation deviceInformation = Agent.getDeviceInformation();
        List<HarvestData> dataList = new ArrayList<>();

        for (ApplicationInformation app : measurementByApp.keySet()) {
            HarvestData harvestData = new HarvestData(app, deviceInformation);
            Map<String, CustomMetricMeasurement> m = measurementByApp.get(app);
            for (String s : m.keySet()) {
                CustomMetricMeasurement measurement = m.get(s);
                harvestData.getMetrics().addMetric(measurement.getCustomMetric());
            }
            dataList.add(harvestData);
        }

        for (ApplicationInformation app : activityTraceByApp.keySet()) {
            HarvestData harvestData = new HarvestData(app, deviceInformation);
            Map<String, ActivityTrace> m = activityTraceByApp.get(app);
            for (String s : m.keySet()) {
                ActivityTrace activityTrace = m.get(s);
                harvestData.getActivityTraces().add(activityTrace);
            }
            dataList.add(harvestData);
        }

        return dataList;
    }

    private void runInThread(Runnable runnable, String threadName) {
        new Thread(runnable, threadName).start();
    }

    private final Map<String, Map<ApplicationInformation, CustomMetricMeasurement>> measurementByOption = new HashMap<>();
    private final Map<ApplicationInformation, Map<String, CustomMetricMeasurement>> measurementByApp = new HashMap<>();

    public Map<String, Map<ApplicationInformation, CustomMetricMeasurement>> getMeasurementByOption() {
        return measurementByOption;
    }

    public Map<ApplicationInformation, CustomMetricMeasurement> getMeasurementByOption(String option) {
        return measurementByOption.get(option);
    }

    // 应用item增加统计数据measurement, 更新两个索引: app -> measure list，在最后数据上报时使用，
    // measure_option -> measure per app list，在展示图表时用，其中measure_option为字符串，取measurement里
    // 的category/name格式
    public void addCache(String optionName, ApplicationInformation item, CustomMetricMeasurement measurement) {
        fillCache(optionName, item, measurement, measurementByOption, measurementByApp);
    }

    private static <T> void fillCache(String optionName, ApplicationInformation item, T measurement,
                           Map<String, Map<ApplicationInformation, T>> mapByApp,
                           Map<ApplicationInformation, Map<String, T>> mapByOption) {
        Map<ApplicationInformation, T> mapList = mapByApp.get(optionName);
        if (null == mapList) {
            mapList = new HashMap<>();
            mapByApp.put(optionName, mapList);
        }
        mapList.put(item, measurement);

        Map<String, T> list = mapByOption.get(item);
        if (null == list) {
            list = new HashMap<>();
            mapByOption.put(item, list);
        }
        list.put(optionName, measurement);
    }

    // 内存和cpu使用情况与measurement对应也有两个索引app-> samples list有数据上报用，option-> samples per app list展示图表时用

    private final Map<String, Map<ApplicationInformation, ActivityTrace>> activityTraceByOption = new HashMap<>();
    private final Map<ApplicationInformation, Map<String, ActivityTrace>> activityTraceByApp = new HashMap<>();

    public Map<String, Map<ApplicationInformation, ActivityTrace>> getActivityTraceByOption() {
        return activityTraceByOption;
    }

    public Map<ApplicationInformation, Map<String, ActivityTrace>> getActivityTraceByApp() {
        return activityTraceByApp;
    }

    public void addCache(String optionName, ApplicationInformation item, EnumMap<Sample.SampleType, Collection<Sample>> samples) {
        Trace rootTrace = new Trace();
        //
        rootTrace.displayName = TraceMachine.formatActivityDisplayName(item.getPackageId() + optionName);
        rootTrace.metricName = TraceMachine.formatActivityMetricName(rootTrace.displayName);
        rootTrace.metricBackgroundName = TraceMachine.formatActivityBackgroundMetricName(rootTrace.displayName);
        rootTrace.entryTimestamp = System.currentTimeMillis();

        ActivityTrace activityTrace = new ActivityTrace(rootTrace);
        activityTrace.setVitals(samples);

        fillCache(optionName, item, activityTrace, activityTraceByOption, activityTraceByApp);
    }
}
