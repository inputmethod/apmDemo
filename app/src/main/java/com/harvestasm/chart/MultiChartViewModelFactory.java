package com.harvestasm.chart;

import android.arch.lifecycle.ViewModelProvider;

public class MultiChartViewModelFactory implements ViewModelProvider.Factory {
    @Override
    public MultiChartViewModel create(Class modelClass) {
        return new MultiChartViewModel();
    }
}
