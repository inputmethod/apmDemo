package com.harvestasm.apm.preview;

import android.arch.lifecycle.MutableLiveData;
import android.graphics.Typeface;
import android.util.Log;

import com.github.mikephil.charting.data.BarEntry;
import com.harvestasm.apm.add.AddDataStorage;
import com.harvestasm.apm.base.BaseListViewModel;
import com.harvestasm.chart.listviewitems.ChartItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import io.reactivex.functions.Consumer;
import typany.apm.agent.android.harvest.ApplicationInformation;
import typany.apm.agent.android.harvest.ConnectInformation;
import typany.apm.agent.android.harvest.HarvestData;
import typany.apm.agent.android.measurement.CustomMetricMeasurement;

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

    private void buildEntry(ArrayList<BarEntry> entries, float value, int index) {
        entries.add(new BarEntry(index, value));
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

        return list;
    }

    private ChartItem generateDataBar(Map<ApplicationInformation, CustomMetricMeasurement> measurementMap,
                                      List<ApplicationInformation> appList, String option, Typeface typeface) {
        assert(appList.size() == measurementMap.size()); // 断言数据与选中的应用总数是一样的

        StringBuilder labelBuilder = new StringBuilder("|");
        ArrayList<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < appList.size(); i++) {
            ApplicationInformation information = appList.get(i);
            CustomMetricMeasurement measurement = measurementMap.get(information);
            buildEntry(entries, (float) measurement.getCustomMetric().getMax(), i);
            labelBuilder.append(information.getAppName())
                    .append(information.getAppVersion())
                    .append("|");
        }

        return generateDataBar(entries, option, labelBuilder.toString(), typeface);
    }

    public void performClick(ChartItem item) {
        clickItem.setValue(item);
    }
}
