package com.harvestasm.apm.browser;

import com.harvestasm.apm.base.BaseChartListFragment;

/**
 * A placeholder fragment containing a simple view.
 */
public class BrowserListFragment extends BaseChartListFragment<BrowserViewModel> {
    private static final String TAG = BrowserListFragment.class.getSimpleName();

    @Override
    protected Class<BrowserViewModel> getViewModelClassName() {
        return BrowserViewModel.class;
    }
}
