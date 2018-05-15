package com.harvestasm.apm.base;

import android.arch.lifecycle.MutableLiveData;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;
import android.util.Log;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.harvestasm.apm.browser.DataStorage;
import com.harvestasm.apm.repository.model.ApmSourceData;
import com.harvestasm.apm.repository.model.search.ApmBaseUnit;
import com.harvestasm.apm.repository.model.search.ApmConnectSearchResponse;
import com.harvestasm.apm.repository.model.search.ApmDataSearchResponse;
import com.harvestasm.apm.utils.ApmRepositoryHelper;
import com.harvestasm.chart.listviewitems.BarChartItem;
import com.harvestasm.chart.listviewitems.ChartItem;
import com.harvestasm.chart.listviewitems.LineChartItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

// todo: simplest implement without repository to store data item.
abstract public class BaseChartViewModel extends BaseListViewModel<ChartItem> {
    private final static String TAG = BaseChartViewModel.class.getSimpleName();

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
    public final void load(final Typeface typeface, boolean force) {
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
        final Map<String, List<ApmBaseUnit<ApmSourceData>>> dataByOption = queryOptions();
        if (null == dataByOption) {
            return;
        } else if (dataByOption.isEmpty()) {
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

    public final void dispose() {
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
        final Set<String> optionFilterList = getOptionFilter();
        assert(dataByOption.keySet().containsAll(optionFilterList));

        List<ChartItem> list = new ArrayList<>();
        for (String key : dataByOption.keySet()) {
            if (optionFilterList.isEmpty() || optionFilterList.contains(key)) {
                buildChartItem(list, key, dataByOption.get(key), typeface);
            } else {
                // skip as it is filtered out by options.
            }
        }

        // final result list.
        return list;
    }

    @WorkerThread
    protected void buildEntry(ArrayList<BarEntry> entries, float value, int index) {
        entries.add(new BarEntry(index, value));
    }

    @WorkerThread
    protected final BarChartItem generateDataBar(ArrayList<BarEntry> entries, String label,
                                         String title, Typeface typeface) {
        BarDataSet d = new BarDataSet(entries, label);
        d.setColors(ColorTemplate.VORDIPLOM_COLORS);
        d.setHighLightAlpha(255);
        BarData cd = new BarData(d);
        cd.setBarWidth(0.9f);
        return new BarChartItem(cd, title, ChartItem.ID.STASTIC_PREVIEW, typeface);
    }

    @WorkerThread
    protected final LineChartItem generateDataLine(ArrayList<Entry> entries, String label,
                                                  String title, Typeface typeface) {
        LineDataSet d = new LineDataSet(entries, label);
        d.setColors(ColorTemplate.VORDIPLOM_COLORS);
        d.setLineWidth(2.5f);
        d.setCircleRadius(4.5f);
        d.setHighLightColor(Color.rgb(244, 117, 117));
//        d.setDrawValues(false);

        LineData cd = new LineData(d);
        return new LineChartItem(cd, title, ChartItem.ID.STASTIC_PREVIEW, typeface);
    }

    public final void performClick(ChartItem item) {
        clickItem.setValue(item);
    }

    abstract protected void buildChartItem(List<ChartItem> list, String key, List<ApmBaseUnit<ApmSourceData>> dataList, Typeface typeface);
    abstract protected Map<String, List<ApmBaseUnit<ApmSourceData>>> queryOptions();
    abstract protected Set<String> getOptionFilter();

    public void parseArguments(Bundle arguments) {
    }
}
