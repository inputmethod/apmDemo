package com.harvestasm.apm.transaction;

import android.graphics.Typeface;
import android.support.annotation.WorkerThread;
import android.text.TextUtils;

import com.github.mikephil.charting.data.BarEntry;
import com.harvestasm.apm.base.BaseChartViewModel;
import com.harvestasm.apm.browser.DataStorage;
import com.harvestasm.apm.repository.model.ApmSourceData;
import com.harvestasm.apm.repository.model.ApmTransactionItem;
import com.harvestasm.apm.repository.model.search.ApmBaseUnit;
import com.harvestasm.chart.listviewitems.BarChartItem;
import com.harvestasm.chart.listviewitems.ChartItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

// todo: simplest implement without repository to store data item.
public class TransactionViewModel extends BaseChartViewModel {
    private final static String TAG = TransactionViewModel.class.getSimpleName();

    @Override
    @WorkerThread
    // todo: 合并计算一个option下相同app的多次值（简单求平均值）
    protected void buildChartItem(List<ChartItem> list, String key, List<ApmBaseUnit<ApmSourceData>> dataList, Typeface typeface) {
        // build chart item with the built map
        ArrayList<BarEntry> entries = new ArrayList<>();
        List<String> appList = new ArrayList<>();

        Map<String, List<ApmTransactionItem>> transactionByUrl = parseTransactionByUrl(key, dataList);
        int index = 0;
        for (String urlText : transactionByUrl.keySet()) {
            appList.add(urlText);
            buildTransactionEntry(entries, transactionByUrl.get(urlText), index++);
        }
//        if (transactionByUrl.isEmpty()) {
//            Map<String, List<ApmMeasurementItem>> measurementByScope = parseMeasurementByScope(key, dataList);
//            int index = 0;
//            for (String scope : measurementByScope.keySet()) {
//                appList.add(scope);
//                buildEntry(entries, measurementByScope.get(scope), index++);
//            }
//        } else {
//            int index = 0;
//            for (String appText : transactionByUrl.keySet()) {
//                HomeDeviceItem.AppItem appItem = new HomeDeviceItem.AppItem();
//                appItem.parseFrom(appText.split(","));
//                appList.add(appItem.getAppName() + "," + appItem.getAppVersionCode());
//                buildEntry(entries, transactionByUrl.get(appText), index++);
//            }
//        }

        String label = TextUtils.join("|", appList);
        BarChartItem chartItem = generateDataBar(entries, key, label, typeface);
        list.add(chartItem);
    }

    @WorkerThread
    private Map<String, List<ApmTransactionItem>> parseTransactionByUrl(String key, List<ApmBaseUnit<ApmSourceData>> dataList) {
        // build measurement map by app.
        Map<String, List<ApmTransactionItem>> transactionByUrl = new HashMap<>();
        final Set<String> appFilterList = DataStorage.get().getFilterApps();
        final Set<String> deviceIdList = DataStorage.get().getFilterDeviceIds();
        for (ApmBaseUnit<ApmSourceData> d : dataList) {
            ApmSourceData sourceData = d.get_source();
            String deviceId = sourceData.getDeviceId();
            if (deviceIdList.contains(deviceId)) {
                parseTransactionByUrl(transactionByUrl, key, appFilterList, sourceData);
            } else {
                // skip as it is filtered out by devices.
            }
        }
        return transactionByUrl;
        // build map end
    }

    @WorkerThread
    private void parseTransactionByUrl(Map<String, List<ApmTransactionItem>> transactionByUrl, String key,
                                       Set<String> appFilterList, ApmSourceData sourceData) {
        List<String> app = sourceData.getApp();
        String appText = null == app ? null : app.toString();
        if (null == appText || appFilterList.contains(appText)) {
            for (ApmTransactionItem item : sourceData.getTransaction()) {
                if (TextUtils.equals(key, item.getUrl())) {
                    List<ApmTransactionItem> itemList = transactionByUrl.get(key);
                    if (null == itemList) {
                        itemList = new ArrayList<>();
                        transactionByUrl.put(key, itemList);
                    }
                    itemList.add(item);
                }
            }
        } else {
            // skip as it is filtered out by apps.
        }
    }

    protected Map<String, List<ApmBaseUnit<ApmSourceData>>> queryOptions() {
        return DataStorage.get().queryTransaction();
    }

    @Override
    protected Set<String> getOptionFilter() {
        return DataStorage.get().getTransactionFilterOptions();
    }

}
