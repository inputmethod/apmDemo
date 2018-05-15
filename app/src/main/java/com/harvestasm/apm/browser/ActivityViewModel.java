package com.harvestasm.apm.browser;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.WorkerThread;
import android.text.TextUtils;

import com.github.mikephil.charting.data.Entry;
import com.harvestasm.apm.base.BaseChartViewModel;
import com.harvestasm.apm.repository.model.ApmActivityItem;
import com.harvestasm.apm.repository.model.ApmSourceData;
import com.harvestasm.apm.repository.model.search.ApmBaseUnit;
import com.harvestasm.chart.listviewitems.ChartItem;
import com.harvestasm.chart.listviewitems.LineChartItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import typany.apm.com.google.gson.Gson;

// todo: simplest implement without repository to store data item.
public class ActivityViewModel extends BaseChartViewModel {
    private final static String TAG = ActivityViewModel.class.getSimpleName();

    @Override
    @WorkerThread
    // todo: 合并计算一个option下相同app的多次值（简单求平均值）
    protected void buildChartItem(List<ChartItem> list, String key, List<ApmBaseUnit<ApmSourceData>> dataList, Typeface typeface) {
        // build chart item with the built map
        ArrayList<Entry> entries = new ArrayList<>();
        List<String> appList = new ArrayList<>();

        Map<String, List<ApmActivityItem.VitalUnit>> memoryByVitals = parseMemoryByVitals(key, dataList);
        int index = 0;
        for (String urlText : memoryByVitals.keySet()) {
            List<ApmActivityItem.VitalUnit> itemList = memoryByVitals.get(urlText);

            appList.add(urlText);
            for (ApmActivityItem.VitalUnit item : itemList) {
                entries.add(new Entry(index++, item.get(1).floatValue()));
            }
        }

        if (!entries.isEmpty()) {
            String label = TextUtils.join("|", appList);
            LineChartItem chartItem = generateDataLine(entries, label, key, typeface);
            list.add(chartItem);
        }
    }

    @WorkerThread
    private Map<String, List<ApmActivityItem.VitalUnit>> parseMemoryByVitals(String key, List<ApmBaseUnit<ApmSourceData>> dataList) {
        // build measurement map by app.
        Map<String, List<ApmActivityItem.VitalUnit>> transactionByUrl = new HashMap<>();
        final Set<String> appFilterList = DataStorage.get().getFilterApps();
        final Set<String> deviceIdList = DataStorage.get().getFilterDeviceIds();
        for (ApmBaseUnit<ApmSourceData> d : dataList) {
            ApmSourceData sourceData = d.get_source();
            String deviceId = sourceData.getDeviceId();
            if (deviceIdList.contains(deviceId)) {
                parseMemoryByVitals(transactionByUrl, key, appFilterList, sourceData);
            } else {
                // skip as it is filtered out by devices.
            }
        }
        return transactionByUrl;
        // build map end
    }

    @WorkerThread
    private void parseMemoryByVitals(Map<String, List<ApmActivityItem.VitalUnit>> memoryByVitals, String key,
                                     Set<String> appFilterList, ApmSourceData sourceData) {
        List<String> app = sourceData.getApp();
        String appText = null == app ? null : app.toString();
        if (null == appText || appFilterList.contains(appText)) {
            for (ApmActivityItem item : sourceData.getActivity()) {
                String str = item.getVitals();
                if (TextUtils.equals(key, str)) {
                    ApmActivityItem.Vitals[] vitals = new Gson().fromJson(str, ApmActivityItem.Vitals[].class);
                    if (null != vitals) {
                        for (ApmActivityItem.Vitals v : vitals) {
                            List<ApmActivityItem.VitalUnit> vitalItems = isMemoryChart ? v.getMemory() : v.getCpu();
                            if (null != vitalItems && !vitalItems.isEmpty()) {
                                String mapKey = item.getDisplayName();
                                List<ApmActivityItem.VitalUnit> unitList = memoryByVitals.get(mapKey);
                                if (null == unitList) {
                                    unitList = new ArrayList<>();
                                    memoryByVitals.put(mapKey, unitList);
                                }
                                unitList.addAll(vitalItems);
                            }
                        }
                    }
                }
            }
        } else {
            // skip as it is filtered out by apps.
        }
    }

    @Override
    protected Map<String, List<ApmBaseUnit<ApmSourceData>>> queryOptions() {
        return DataStorage.get().queryActivityVitals();
    }

    @Override
    protected Set<String> getOptionFilter() {
        return DataStorage.get().getActivityFilterOptions();
    }

    private boolean isMemoryChart;
    @Override
    public void parseArguments(Bundle bundle) {
        isMemoryChart = null == bundle ? false : bundle.getBoolean("type");
    }
}
