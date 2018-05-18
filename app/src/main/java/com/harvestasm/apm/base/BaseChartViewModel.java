package com.harvestasm.apm.base;

import android.arch.lifecycle.MutableLiveData;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;
import android.util.Log;

import com.harvestasm.apm.browser.DataStorage;
import com.harvestasm.apm.repository.model.ApmSourceData;
import com.harvestasm.apm.repository.model.search.ApmBaseUnit;
import com.harvestasm.apm.repository.model.search.ApmConnectSearchResponse;
import com.harvestasm.apm.repository.model.search.ApmDataSearchResponse;
import com.harvestasm.apm.utils.ApmRepositoryHelper;
import com.harvestasm.chart.listviewitems.ChartItem;

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

    public final MutableLiveData<ChartItem> clickItem = new MutableLiveData<>();

    private Disposable disposable;

    // Browser和Filter两个页面切换时，Fragment的onActivityCreated每次都执行，为load加上force
    // 区分Fragment创建时(false)和下拉刷新时(true)，DataStorage里根据这个布尔变量决定重用当前
    // 缓存数据还是重新发起新的请求
    @MainThread
    public final void load(final Typeface typeface, boolean force) {
        resetForLoading();

        final ApmRepositoryHelper.CallBack callBack = new ApmRepositoryHelper.CallBack() {
            private ApmConnectSearchResponse connectResponse;
            private ApmDataSearchResponse dataResponse;
            private boolean usingAutoSource;
            private void check() {
                if (null == connectResponse && null == dataResponse) {
                    Log.i(TAG, "load check and wait for both data and connect reponse.");
                    return;
                }
                if (usingAutoSource != DataStorage.get().isUsedAutoChart()) {
                    Log.i(TAG, "load check skip while data storage type was changed " + usingAutoSource);
                    return;
                }

                DataStorage.get().setDataConnectResponse(connectResponse, dataResponse);
                checkResult(typeface);
            }

            @Override
            public void setTag(boolean autoSource) {
                usingAutoSource = autoSource;
            }

            @Override
            public void onConnectResponse(ApmConnectSearchResponse responseBody) {
                this.connectResponse = responseBody;
                check();
            }

            @Override
            public void onDataResponse(ApmDataSearchResponse responseBody) {
                dataResponse = responseBody;
                check();
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
        if (null == optionFilterList) {
            return Collections.emptyList();
        }

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

    public final void performClick(ChartItem item) {
        clickItem.setValue(item);
    }

    abstract protected void buildChartItem(List<ChartItem> list, String key, List<ApmBaseUnit<ApmSourceData>> dataList, Typeface typeface);
    abstract protected Map<String, List<ApmBaseUnit<ApmSourceData>>> queryOptions();
    abstract protected Set<String> getOptionFilter();

    public void parseArguments(Bundle arguments) {
    }
}
