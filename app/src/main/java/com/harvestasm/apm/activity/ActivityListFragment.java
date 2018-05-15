package com.harvestasm.apm.activity;

import com.harvestasm.apm.base.BaseChartListFragment;

/**
 * A placeholder fragment containing a simple view.
 */
public class ActivityListFragment extends BaseChartListFragment<ActivityViewModel> {
    private static final String TAG = ActivityListFragment.class.getSimpleName();

    @Override
    protected  Class<ActivityViewModel> getViewModelClassName() {
        return ActivityViewModel.class;
    }
}
