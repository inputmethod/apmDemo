package com.harvestasm.apm.browser;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.os.Bundle;
import android.support.annotation.MainThread;

public class BrowserActivityViewModel extends ViewModel {
    private static final int CHART_AUTO = 0;
    private static final int CHART_MANUAL = 1;
    private static final int CHART_NETWORKING_DATA = 2;
    private static final int CHART_NETWORKING_TIME = 3;
    private static final int CHART_ACTIVITY_MEMORY = 4;
    private static final int CHART_ACTIVITY_CPU = 5;

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

    public void showNetworkingTime() {
        DataStorage.get().useAutoMeasurements();
        DataStorage.get().currentState.setValue(CHART_NETWORKING_TIME);
    }

    public void useManualMeasurements() {
        DataStorage.get().useManualMeasurements();
        showChartList();
    }

    public void useAutoMeasurements() {
        DataStorage.get().useAutoMeasurements();
        showChartList();
    }

    public boolean isStatisticChartList(int id) {
        return CHART_AUTO == id || CHART_MANUAL == id;
    }

    public void showActivityManualMemory() {
        DataStorage.get().useManualMeasurements();
        DataStorage.get().currentState.setValue(CHART_ACTIVITY_MEMORY);
    }

    public void showActivityMemory() {
        DataStorage.get().useAutoMeasurements();
        DataStorage.get().currentState.setValue(CHART_ACTIVITY_MEMORY);
    }

    public void showActivityCpu() {
        DataStorage.get().useAutoMeasurements();
        DataStorage.get().currentState.setValue(CHART_ACTIVITY_CPU);
    }

    public boolean isActivityData(int id) {
        return CHART_ACTIVITY_MEMORY == id ||CHART_ACTIVITY_CPU == id;
    }

    private Bundle generateTypeBundle(boolean value) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("type", value);
        return bundle;
    }

    public Bundle parseArguments(int id) {
        if (isStatisticChartList(id)) {
            return null;
        } else if (isActivityData(id)){
            return generateTypeBundle(CHART_ACTIVITY_MEMORY == id);
        } else {
            return generateTypeBundle(CHART_NETWORKING_TIME == id);
        }
    }
}
