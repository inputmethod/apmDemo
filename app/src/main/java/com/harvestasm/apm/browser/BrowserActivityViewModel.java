package com.harvestasm.apm.browser;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.MainThread;

public class BrowserActivityViewModel extends ViewModel {
    private static final int CHART_AUTO = 0;
    private static final int CHART_MANUAL = 1;
    private static final int CHART_NETWORKING_DATA = 2;

    @MainThread
    public void startObserve(LifecycleOwner owner, Observer<Integer> observer) {
        DataStorage.get().currentState.observe(owner, observer);
        showChartList();
    }

    public void showChartList() {
        int index = DataStorage.get().isUsedAutoChart() ? CHART_AUTO : CHART_MANUAL;
        DataStorage.get().currentState.setValue(index);
    }

    public void showNetworkingData() {
        DataStorage.get().useAutoMeasurements();
        DataStorage.get().currentState.setValue(CHART_NETWORKING_DATA);
    }

    public void useManualMeasurements() {
        DataStorage.get().useManualMeasurements();
        showChartList();
    }

    public void useAutoMeasurements() {
        DataStorage.get().useAutoMeasurements();
        showChartList();
    }

    public boolean isChartList(int id) {
        return CHART_AUTO == id || CHART_MANUAL == id;
    }
}
