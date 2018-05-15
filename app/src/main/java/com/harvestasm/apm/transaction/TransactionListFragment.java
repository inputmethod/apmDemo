package com.harvestasm.apm.transaction;

import com.harvestasm.apm.base.BaseChartListFragment;

/**
 * A placeholder fragment containing a simple view.
 */
public class TransactionListFragment extends BaseChartListFragment<TransactionViewModel> {
    private static final String TAG = TransactionListFragment.class.getSimpleName();

    @Override
    protected Class<TransactionViewModel> getViewModelClassName() {
        return TransactionViewModel.class;
    }
}
