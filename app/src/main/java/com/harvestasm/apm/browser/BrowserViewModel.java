package com.harvestasm.apm.browser;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.graphics.Typeface;
import android.text.TextUtils;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.harvestasm.apm.add.AddDataStorage;
import com.harvestasm.apm.repository.model.ApmMeasurementItem;
import com.harvestasm.apm.repository.model.ApmSourceData;
import com.harvestasm.apm.repository.model.search.ApmBaseUnit;
import com.harvestasm.apm.repository.model.search.ApmConnectSearchResponse;
import com.harvestasm.apm.repository.model.search.ApmDataSearchResponse;
import com.harvestasm.apm.utils.ApmRepositoryHelper;
import com.harvestasm.chart.listviewitems.BarChartItem;
import com.harvestasm.chart.listviewitems.ChartItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    public void load(final Typeface typeface) {
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

        AddDataStorage.get().doLoadTask(callBack);
    }

    // todo: 以设备进行过滤或者按设备取数据
    private void checkResult(final Typeface typeface) {
        Map<String, List<ApmBaseUnit<ApmSourceData>>> dataByOption = DataStorage.get().queryByOption();
        if (dataByOption.isEmpty()) {
            return;
        }

        final Set<String> optionFilterList = DataStorage.get().getFilterOptions();
        assert(dataByOption.keySet().containsAll(optionFilterList));

        final Set<String> appFilterList = DataStorage.get().getFilterApps();

        final Set<String> deviceIdList = DataStorage.get().getFilterDeviceIds();

        List<ChartItem> list = new ArrayList<>();
        for (String key : dataByOption.keySet()) {
            if (!optionFilterList.contains(key)) {
                // skip as it is filtered out by options.
                continue;
            }

            List<ApmBaseUnit<ApmSourceData>> dataList = dataByOption.get(key);
            ArrayList<BarEntry> entries = new ArrayList<>();
            int index = 0;
            List<String> appList = new ArrayList<>();
            for (ApmBaseUnit<ApmSourceData> d : dataList) {
                ApmSourceData sourceData = d.get_source();
                String deviceId = sourceData.getDeviceId();
                if (!deviceIdList.contains(deviceId)) {
                    // skip as it is filtered out by devices.
                    continue;
                }
                List<String> app = sourceData.getApp();
                String appText = null == app ? "" : app.toString();
                if (!appFilterList.contains(appText)) {
                    // skip as it is filtered out by apps.
                    continue;
                }

                appList.add(app.get(0) + "," + app.get(1));
                for (ApmMeasurementItem item : sourceData.getMeasurement()) {
                    if (TextUtils.equals(key, item.getName())) {
                        buildEntry(entries, (float) item.getMax(), index++);
                    }
                }
            }

            String label = TextUtils.join("|", appList);
            BarChartItem chartItem = generateDataBar(entries, key, label, typeface);
            list.add(chartItem);
        }

        // final result list.
        onDataLoaded(list);
    }

    private void buildEntry(ArrayList<BarEntry> entries, float value, int index) {
        entries.add(new BarEntry(index, value));
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
}
