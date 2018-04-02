package com.harvestasm.chart;

import android.arch.lifecycle.ViewModelProvider;

public class ListViewMultiChartModelFactory implements ViewModelProvider.Factory {
    @Override
    public ListViewMultiChartModel create(Class modelClass) {
        return new ListViewMultiChartModel();
    }
}
