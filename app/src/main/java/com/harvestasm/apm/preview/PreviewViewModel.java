package com.harvestasm.apm.preview;

import android.arch.lifecycle.MutableLiveData;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.Log;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.harvestasm.apm.add.AddDataStorage;
import com.harvestasm.apm.base.BaseListViewModel;
import com.harvestasm.apm.repository.model.ApmActivityItem;
import com.harvestasm.chart.ChartItemHelper;
import com.harvestasm.chart.listviewitems.ChartItem;
import com.harvestasm.chart.listviewitems.LineChartItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import io.reactivex.functions.Consumer;
import typany.apm.agent.android.harvest.ApplicationInformation;
import typany.apm.agent.android.harvest.ConnectInformation;
import typany.apm.agent.android.harvest.HarvestData;
import typany.apm.agent.android.measurement.CustomMetricMeasurement;
import typany.apm.agent.android.metric.Metric;
import typany.apm.agent.android.tracing.ActivityTrace;

// todo: simplest implement without repository to store data item.
public class PreviewViewModel extends BaseListViewModel<ChartItem> {
    private final static String TAG = PreviewViewModel.class.getSimpleName();

    public final MutableLiveData<ChartItem> clickItem = new MutableLiveData<>();

    public void load(final Typeface typeface) {
        resetForLoading();

        checkResult(typeface);
    }

    // todo: 设置上传中状态，上传保存，清除上传状态，处理上传成功或失败结果
    public void pushCache() {
        // upload connection
        final Callable<List<ConnectInformation>> callable = new Callable<List<ConnectInformation>>() {
            @Override
            public List<ConnectInformation> call() {
                return AddDataStorage.get().getCachedConnection();
            }
        };
        final Consumer<List<ConnectInformation>> consumer = new Consumer<List<ConnectInformation>>() {
            @Override
            public void accept(List<ConnectInformation> connectInformationList) {
                for (ConnectInformation info : connectInformationList) {
                    AddDataStorage.get().testConnect(info);
                }
            }
        };

        AddDataStorage.get().runWithFlowable(callable, consumer);

        // upload data
        final Callable<List<HarvestData>> dataCallable = new Callable<List<HarvestData>>() {
            @Override
            public List<HarvestData> call() {
                return AddDataStorage.get().getCachedData();
            }
        };

        final Consumer<List<HarvestData>> dataConsumer = new Consumer<List<HarvestData>>() {
            @Override
            public void accept(List<HarvestData> harvestData) {
                for (HarvestData d : harvestData) {
                    AddDataStorage.get().testData(d);
                }
            }
        };
        AddDataStorage.get().runWithFlowable(dataCallable, dataConsumer);
    }

    private void checkResult(final Typeface typeface) {
        // SEETO: rxjava
        Callable<List<ChartItem>> callable = new Callable<List<ChartItem>>() {
            @Override
            public List<ChartItem> call() {
                return getDisplayChartItemList(typeface);
            }
        };
        Consumer<List<ChartItem>> consumer = new Consumer<List<ChartItem>>() {
            @Override
            public void accept(List<ChartItem> chartItemList) {
                Log.e(TAG, "Consumer.accept in thread " + Thread.currentThread().getName());
//                hardwareLiveData.setValue(deviceInformation);
                onDataLoaded(chartItemList);
            }
        };

        AddDataStorage.get().runWithFlowable(callable, consumer);
    }

    // 缓存的数据视图
    private List<ChartItem> getDisplayChartItemList(Typeface typeface) {
        List<ChartItem> list = new ArrayList<>();

        List<ApplicationInformation> appList = new ArrayList<>(AddDataStorage.get().selectedImeAppList); // 统一app排列顺序
        Map<String, Map<ApplicationInformation, CustomMetricMeasurement>> measurementByOption = AddDataStorage.get().getMeasurementByOption();
        List<String> optionList = new ArrayList<>(measurementByOption.keySet());
        Collections.sort(optionList);
        for (String option : optionList) {
            Map<ApplicationInformation, CustomMetricMeasurement> measurementMap = measurementByOption.get(option);
            list.add(generateDataBar(measurementMap, appList, option, typeface));
        }

        // todo: 显示内存图缓存
        Map<String, Map<ApplicationInformation, ActivityTrace>> activityTraceByOption =
                AddDataStorage.get().getActivityTraceByOption();
        assert(activityTraceByOption.size() == 1); // 只会有1个元素且key是内存使用报表
        optionList = new ArrayList<>(activityTraceByOption.keySet());
        Collections.sort(optionList);
        for (String option : optionList) {
            Map<ApplicationInformation, ActivityTrace> sampleMap = activityTraceByOption.get(option);
            ChartItem item = generateDataLine(sampleMap, appList, option, typeface);
            if (null == item) {
                Log.w(TAG, "getDisplayChartItemList, skip null memory line chart: " + option);
            } else {
                list.add(item);
            }
        }

        return list;
    }

    // 已经选中的应用appList, option为内存使用的标识，各种应用的内存数据在sampleMap里。
    private ChartItem generateDataLine(Map<ApplicationInformation, ActivityTrace> sampleMap,
                                       List<ApplicationInformation> selectedApps, String option, Typeface typeface) {
        // build chart item with the built map
        List<Entry> entries = new ArrayList<>();
        List<String> appList = new ArrayList<>();

        Map<String, List<ApmActivityItem.VitalUnit>> memoryByVitals = buildMemoryByVitals(sampleMap, selectedApps);
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
            LineChartItem chartItem = ChartItemHelper.generateDataLine(entries, label, option, typeface);
            return chartItem;
        }

        return null;
    }

    private Map<String, List<ApmActivityItem.VitalUnit>> buildMemoryByVitals(Map<ApplicationInformation, ActivityTrace> sampleMap,
                                                                             List<ApplicationInformation> selectedApps) {
        Map<String, List<ApmActivityItem.VitalUnit>> memoryByVitals = new HashMap<>();
        if (null != selectedApps && null != sampleMap && selectedApps.size() == sampleMap.size()) {
            for (ApplicationInformation item : selectedApps) {
                ActivityTrace activityTrace = sampleMap.get(item);
                try {
                    String str = activityTrace.toJson().get("vitals").toString();
                    ChartItemHelper.parseVitalUnitList(memoryByVitals, str, item.getAppName(), true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return memoryByVitals;
    }

    private ChartItem generateDataBar(Map<ApplicationInformation, CustomMetricMeasurement> measurementMap,
                                      List<ApplicationInformation> appList, String option, Typeface typeface) {
        assert(appList.size() == measurementMap.size()); // 断言数据与选中的应用总数是一样的

        StringBuilder labelBuilder = new StringBuilder("|");
        ArrayList<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < appList.size(); i++) {
            ApplicationInformation information = appList.get(i);
            CustomMetricMeasurement measurement = measurementMap.get(information);
            Metric metric = measurement.getCustomMetric();
            ChartItemHelper.buildEntry(entries, (float) metric.getMax(),
                    (float) metric.getMax(), metric.getCount(), i);
            labelBuilder.append(information.getAppName())
                    .append(information.getAppVersion())
                    .append("|");
        }

        return ChartItemHelper.generateDataBar(entries, option, labelBuilder.toString(), typeface);
    }

    public void performClick(ChartItem item) {
        clickItem.setValue(item);
    }
}
