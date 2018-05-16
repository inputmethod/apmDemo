package com.harvestasm.apm.browser;

import android.graphics.Typeface;
import android.support.annotation.WorkerThread;
import android.text.TextUtils;

import com.github.mikephil.charting.data.BarEntry;
import com.harvestasm.apm.base.BaseChartViewModel;
import com.harvestasm.apm.home.HomeDeviceItem;
import com.harvestasm.apm.repository.model.ApmMeasurementItem;
import com.harvestasm.apm.repository.model.ApmSourceData;
import com.harvestasm.apm.repository.model.search.ApmBaseUnit;
import com.harvestasm.chart.listviewitems.ChartItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

// todo: simplest implement without repository to store data item.
public class BrowserViewModel extends BaseChartViewModel {
    private final static String TAG = BrowserViewModel.class.getSimpleName();

    @Override
    @WorkerThread
    // todo: 合并计算一个option下相同app的多次值（简单求平均值）
    protected void buildChartItem(List<ChartItem> list, String key, List<ApmBaseUnit<ApmSourceData>> dataList, Typeface typeface) {
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
        list.add(generateDataBar(entries, key, label, typeface));
    }

    @WorkerThread
    // todo: 简单求平均值拟合图表。
    private final void buildEntry(ArrayList<BarEntry> entries, List<ApmMeasurementItem> itemList, int index) {
        double total = 0;
        for (ApmMeasurementItem item : itemList) {
            total += (item.getMax() + item.getMin()) / 2;
        }
        buildEntry(entries, (float) total / itemList.size(), index);
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

    @Override
    protected Map<String, List<ApmBaseUnit<ApmSourceData>>> queryOptions() {
        return DataStorage.get().queryByOption();
    }

    @Override
    protected Set<String> getOptionFilter() {
        return DataStorage.get().getFilterOptions();
    }
}
