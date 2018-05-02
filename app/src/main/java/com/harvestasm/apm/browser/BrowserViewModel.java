package com.harvestasm.apm.browser;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.Log;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.harvestasm.apm.add.AddDataStorage;
import com.harvestasm.apm.reporter.ApmConnectSourceIndex;
import com.harvestasm.apm.reporter.ApmDataSourceIndex;
import com.harvestasm.apm.reporter.ApmSourceGroup;
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

import typany.apm.agent.android.harvest.ApplicationInformation;
import typany.apm.agent.android.measurement.CustomMetricMeasurement;

// todo: simplest implement without repository to store data item.
public class BrowserViewModel extends ViewModel {
    private final static String TAG = BrowserViewModel.class.getSimpleName();

    public final MutableLiveData<List<ChartItem>> items = new MutableLiveData<>();
    public final MutableLiveData<Boolean> refreshState = new MutableLiveData<>();
    public final MutableLiveData<Integer> networkState = new MutableLiveData<>();

    public final MutableLiveData<ChartItem> clickItem = new MutableLiveData<>();

    private void resetForLoading() {
        refreshState.setValue(true);
        networkState.postValue(0);
    }

    private void onDataLoaded(List<ChartItem> list) {
        items.setValue(list);

        refreshState.setValue(false);
        networkState.postValue(0);

    }

    private ApmConnectSearchResponse connectResponse;
    private ApmDataSearchResponse dataResponse;
    public void load(final Typeface typeface) {
        resetForLoading();

        final ApmRepositoryHelper.CallBack callBack = new ApmRepositoryHelper.CallBack() {
            @Override
            public void onConnectResponse(ApmConnectSearchResponse responseBody) {
                connectResponse = responseBody;
                checkResult(typeface);
            }

            @Override
            public void onDataResponse(ApmDataSearchResponse responseBody) {
                dataResponse = responseBody;
                checkResult(typeface);
            }
        };

        AddDataStorage.get().doLoadTask(callBack);
    }

    private void checkResult(final Typeface typeface) {
        if (null == connectResponse || null == dataResponse) {
            Log.i(TAG, "checkResult, skip and wait until all data loaded.");
            return;
        }

        ApmConnectSourceIndex connectSourceIndex = new ApmConnectSourceIndex(connectResponse);
        ApmDataSourceIndex dataSourceIndex = new ApmDataSourceIndex(dataResponse);
        List<ApmSourceGroup> deviceGroupList = ApmSourceGroup.parseSourceGroup(dataSourceIndex, connectSourceIndex);
        HashMap<String, List<ApmBaseUnit<ApmSourceData>>> dataByOption = dataSourceIndex.getMeasureNameIndexMap();

        List<ChartItem> list = new ArrayList<>();

        for (String key : dataByOption.keySet()) {
            List<ApmBaseUnit<ApmSourceData>> dataList = dataByOption.get(key);
            ArrayList<BarEntry> entries = new ArrayList<>();
            int index = 0;
            List<String> appList = new ArrayList<>();
            for (ApmBaseUnit<ApmSourceData> d : dataList) {
                ApmSourceData sourceData = d.get_source();
                List<String> app = sourceData.getApp();
                appList.add(app.get(0) + "," + app.get(1));
                for (ApmMeasurementItem item : sourceData.getMeasurement()) {
                    buildEntry(entries, (float) item.getMax(), index++);
                }
            }

            String label = TextUtils.join("|", appList);
            BarChartItem chartItem = generateDataBar(entries, label, key, typeface);
            list.add(chartItem);
        }

        // parse apps
//        HashMap<String, List<ApmBaseUnit<ApmSourceConnect>>> connectUnits = connectSourceIndex.getAppIndexMap();
//        ArrayList<BarEntry> entries = new ArrayList<>();
//        ArrayList<String> keySet = new ArrayList<>(connectUnits.keySet());
//        for (int i = 0; i < keySet.size(); i++) {
//            buildEntry(entries, connectUnits.get(keySet.get(i)).size(), i);
//        }
//        BarChartItem item = generateDataBar(entries, "版本", "App分布", typeface);
//        list.add(item);

        // final result list.
        onDataLoaded(list);
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

        BarChartItem item = generateDataBar(entries, option, labelBuilder.toString(), typeface);
        return item;
    }

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

    public void parsePieChartItem(ChartItem item) {
        ChartData data = item.getChartData();
        if (data instanceof PieData) {
            PieData lineData = (PieData) data;
            List<IPieDataSet> list = lineData.getDataSets();
            Log.v(TAG, "parseLineChartItem, data set size " + list.size());
        }
    }

    public void parseBarChartItem(ChartItem item) {
        ChartData data = item.getChartData();
        if (data instanceof BarData) {
            BarData lineData = (BarData) data;
            List<IBarDataSet> list = lineData.getDataSets();
            Log.v(TAG, "parseLineChartItem, data set size " + list.size());
        }
    }

    public void parseLineChartItem(ChartItem item) {
        ChartData data = item.getChartData();
        if (data instanceof LineData) {
            LineData lineData = (LineData) data;
            List<ILineDataSet> list = lineData.getDataSets();
            Log.v(TAG, "parseLineChartItem, data set size " + list.size());
        }
    }
}
