package com.harvestasm.apm.browser;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.Log;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.harvestasm.apm.add.AddDataStorage;
import com.harvestasm.apm.reporter.ApmDataSourceIndex;
import com.harvestasm.apm.repository.model.ApmMeasurementItem;
import com.harvestasm.apm.repository.model.ApmSourceData;
import com.harvestasm.apm.repository.model.search.ApmBaseUnit;
import com.harvestasm.apm.repository.model.search.ApmConnectSearchResponse;
import com.harvestasm.apm.repository.model.search.ApmDataSearchResponse;
import com.harvestasm.apm.utils.ApmRepositoryHelper;
import com.harvestasm.chart.listviewitems.BarChartItem;
import com.harvestasm.chart.listviewitems.ChartItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        if (/*null == connectResponse || */null == dataResponse) {
            Log.i(TAG, "checkResult, skip and wait until all data loaded.");
            return;
        }

//        ApmConnectSourceIndex connectSourceIndex = new ApmConnectSourceIndex(connectResponse);
        ApmDataSourceIndex dataSourceIndex = new ApmDataSourceIndex(dataResponse);
//        List<ApmSourceGroup> deviceGroupList = ApmSourceGroup.parseSourceGroup(dataSourceIndex, connectSourceIndex);
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
