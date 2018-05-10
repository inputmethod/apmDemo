package com.harvestasm.apm.transaction;

import android.arch.lifecycle.MutableLiveData;
import android.graphics.Typeface;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;
import android.text.TextUtils;
import android.util.Log;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.harvestasm.apm.base.BaseListViewModel;
import com.harvestasm.apm.browser.DataStorage;
import com.harvestasm.apm.home.HomeDeviceItem;
import com.harvestasm.apm.repository.model.ApmMeasurementItem;
import com.harvestasm.apm.repository.model.ApmSourceData;
import com.harvestasm.apm.repository.model.search.ApmBaseUnit;
import com.harvestasm.apm.repository.model.search.ApmConnectSearchResponse;
import com.harvestasm.apm.repository.model.search.ApmDataSearchResponse;
import com.harvestasm.apm.utils.ApmRepositoryHelper;
import com.harvestasm.chart.listviewitems.BarChartItem;
import com.harvestasm.chart.listviewitems.ChartItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

// todo: simplest implement without repository to store data item.
public class TransactionViewModel extends BaseListViewModel<ChartItem> {
    private final static String TAG = TransactionViewModel.class.getSimpleName();

//    public final MutableLiveData<Boolean> refreshState = new MutableLiveData<>();
//    public final MutableLiveData<Integer> networkState = new MutableLiveData<>();

//    public final MutableLiveData<List<ChartItem>> items = new MutableLiveData<>();
    public final MutableLiveData<ChartItem> clickItem = new MutableLiveData<>();

    private Disposable disposable;

    // todo: reset需要把items内容clear??
//    private void resetForLoading() {
//        refreshState.setValue(true);
//        networkState.postValue(0);
//    }
//
//    private void onDataLoaded(List<ChartItem> list) {
//        items.setValue(list);
//
//        refreshState.setValue(false);
//        networkState.postValue(0);
//
//    }

    // Browser和Filter两个页面切换时，Fragment的onActivityCreated每次都执行，为load加上force
    // 区分Fragment创建时(false)和下拉刷新时(true)，DataStorage里根据这个布尔变量决定重用当前
    // 缓存数据还是重新发起新的请求
    @MainThread
    public void load(final Typeface typeface, boolean force) {
        resetForLoading();

        final ApmRepositoryHelper.CallBack callBack = new ApmRepositoryHelper.CallBack() {
            @Override
            public void onConnectResponse(ApmConnectSearchResponse responseBody) {
                DataStorage.get().setConnectResponse(responseBody);
                checkResult(typeface);
            }

            @Override
            public void onDataResponse(ApmDataSearchResponse responseBody) {
                DataStorage.get().setDataResponse(responseBody);
                checkResult(typeface);
            }
        };

        final ApmRepositoryHelper.RefreshInterface refreshInterface = new ApmRepositoryHelper.RefreshInterface() {
            @Override
            public void onRefresh() {
                checkResult(typeface);
            }
        };

        DataStorage.get().doLoadTask(callBack, refreshInterface, force);
    }

    @MainThread
    // todo: 以设备进行过滤或者按设备取数据
    private void checkResult(final Typeface typeface) {
        final Map<String, List<ApmBaseUnit<ApmSourceData>>> dataByOption = DataStorage.get().queryByOption();
        if (dataByOption.isEmpty()) {
            onDataLoaded(Collections.<ChartItem>emptyList());
            return;
        }

        Callable<List<ChartItem>> callable = new Callable<List<ChartItem>>() {
            @Override
            public List<ChartItem> call() {
                Log.e(TAG, "Callable.call thread " + Thread.currentThread().getName());
                return buildChartItemList(dataByOption,typeface);
            }
        };

        Consumer<List<ChartItem>> consumer = new Consumer<List<ChartItem>>() {
            @Override
            public void accept(List<ChartItem> chartItemList) {
                Log.e(TAG, "Consumer.accept thread " + Thread.currentThread().getName());
                onDataLoaded(chartItemList);
            }
        };

        dispose();
        disposable = DataStorage.get().runWithFlowable(callable, consumer);
    }

    public void dispose() {
        dispose(disposable);
    }

    private static void dispose(Disposable disposable) {
        if (null != disposable && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

    @WorkerThread
    @NonNull
    private List<ChartItem> buildChartItemList(Map<String, List<ApmBaseUnit<ApmSourceData>>> dataByOption, Typeface typeface) {
        final Set<String> optionFilterList = DataStorage.get().getFilterOptions();
        assert(dataByOption.keySet().containsAll(optionFilterList));

        List<ChartItem> list = new ArrayList<>();
        for (String key : dataByOption.keySet()) {
            if (optionFilterList.contains(key)) {
                buildChartItem(list, key, dataByOption.get(key), typeface);
            } else {
                // skip as it is filtered out by options.
            }
        }

        // final result list.
        return list;
    }

    @WorkerThread
    // todo: 合并计算一个option下相同app的多次值（简单求平均值）
    private void buildChartItem(List<ChartItem> list, String key, List<ApmBaseUnit<ApmSourceData>> dataList, Typeface typeface) {
        // build chart item with the built map
        ArrayList<BarEntry> entries = new ArrayList<>();
        List<String> appList = new ArrayList<>();

        Map<String, List<ApmMeasurementItem>> measurementByApp = parseMeasurementByApp(key, dataList);
        if (measurementByApp.isEmpty()) {
            Map<String, List<ApmMeasurementItem>> measurementByScope = parseMeasurementByScope(key, dataList);
            int index = 0;
            for (String scope : measurementByScope.keySet()) {
                appList.add(scope);
                buildEntry(entries, measurementByScope.get(scope), index++);
            }
        } else {
            int index = 0;
            for (String appText : measurementByApp.keySet()) {
                HomeDeviceItem.AppItem appItem = new HomeDeviceItem.AppItem();
                appItem.parseFrom(appText.split(","));
                appList.add(appItem.getAppName() + "," + appItem.getAppVersionCode());
                buildEntry(entries, measurementByApp.get(appText), index++);
            }
        }

        String label = TextUtils.join("|", appList);
        BarChartItem chartItem = generateDataBar(entries, key, label, typeface);
        list.add(chartItem);
    }

    @WorkerThread
    private Map<String, List<ApmMeasurementItem>> parseMeasurementByScope(String key, List<ApmBaseUnit<ApmSourceData>> dataList) {
        Map<String, List<ApmMeasurementItem>> measurementByScope = new HashMap<>();
        final Set<String> deviceIdList = DataStorage.get().getFilterDeviceIds();
        for (ApmBaseUnit<ApmSourceData> d : dataList) {
            ApmSourceData sourceData = d.get_source();
            String deviceId = sourceData.getDeviceId();
            if (deviceIdList.contains(deviceId)) {
                parseMeasurementByScope(measurementByScope, key, sourceData);
            } else {
                // skip as it is filtered out by devices.
            }
        }
        return measurementByScope;
    }
    @WorkerThread
    private void parseMeasurementByScope(Map<String, List<ApmMeasurementItem>> measurementByScope, String key, ApmSourceData sourceData) {
        for (ApmMeasurementItem item : sourceData.getMeasurement()) {
            if (TextUtils.equals(key, item.getName())) {
                String scopeText = item.getScope();
                List<ApmMeasurementItem> itemList = measurementByScope.get(scopeText);
                if (null == itemList) {
                    itemList = new ArrayList<>();
                    measurementByScope.put(scopeText, itemList);
                }
                itemList.add(item);
            }
        }
    }

    @WorkerThread
    private Map<String, List<ApmMeasurementItem>> parseMeasurementByApp(String key, List<ApmBaseUnit<ApmSourceData>> dataList) {
        // build measurement map by app.
        Map<String, List<ApmMeasurementItem>> measurementByApp = new HashMap<>();
        final Set<String> appFilterList = DataStorage.get().getFilterApps();
        final Set<String> deviceIdList = DataStorage.get().getFilterDeviceIds();
        for (ApmBaseUnit<ApmSourceData> d : dataList) {
            ApmSourceData sourceData = d.get_source();
            String deviceId = sourceData.getDeviceId();
            if (deviceIdList.contains(deviceId)) {
                parseMeasurementByApp(measurementByApp, key, appFilterList, sourceData);
            } else {
                // skip as it is filtered out by devices.
            }
        }
        return measurementByApp;
        // build map end
    }

    @WorkerThread
    private void parseMeasurementByApp(Map<String, List<ApmMeasurementItem>> measurementByApp, String key,
                                       Set<String> appFilterList, ApmSourceData sourceData) {
        List<String> app = sourceData.getApp();
        String appText = null == app ? "" : app.toString();
        if (appFilterList.contains(appText)) {
            for (ApmMeasurementItem item : sourceData.getMeasurement()) {
                if (TextUtils.equals(key, item.getName())) {
                    List<ApmMeasurementItem> itemList = measurementByApp.get(appText);
                    if (null == itemList) {
                        itemList = new ArrayList<>();
                        measurementByApp.put(appText, itemList);
                    }
                    itemList.add(item);
                }
            }
        } else {
            // skip as it is filtered out by apps.
        }
    }

    @WorkerThread
    // todo: 简单求平均值拟合图表。
    private void buildEntry(ArrayList<BarEntry> entries, List<ApmMeasurementItem> itemList, int index) {
        double total = 0;
        for (ApmMeasurementItem item : itemList) {
            total += (item.getMax() + item.getMax()) / 2;
        }
        buildEntry(entries, (float) total / itemList.size(), index);
    }

    @WorkerThread
    private void buildEntry(ArrayList<BarEntry> entries, float value, int index) {
        entries.add(new BarEntry(index, value));
    }

    @WorkerThread
    private BarChartItem generateDataBar(ArrayList<BarEntry> entries, String label,
                                         String title, Typeface typeface) {
        BarDataSet d = new BarDataSet(entries, label);
        d.setColors(ColorTemplate.VORDIPLOM_COLORS);
        d.setHighLightAlpha(255);
        BarData cd = new BarData(d);
        cd.setBarWidth(0.9f);
        return new BarChartItem(cd, title, ChartItem.ID.STASTIC_PREVIEW, typeface);
    }

    public void performClick(ChartItem item) {
        clickItem.setValue(item);
    }
}
