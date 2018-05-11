package com.harvestasm.apm.transaction;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ListView;

import com.harvestasm.apm.sample.R;
import com.harvestasm.base.RefreshListFragment;
import com.harvestasm.chart.listviewitems.ChartItem;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class TransactionListFragment extends RefreshListFragment<ChartItem, ListView> {
    private static final String TAG = TransactionListFragment.class.getSimpleName();

    private TransactionViewModel transactionViewModel;
    private TransactionAdapter transactionAdapter;

    @Override
    protected @LayoutRes int getCollectionLayoutResourceId() {
        return R.layout.including_listview;
    }

    // todo: 每次都重新赋予新的adapter实例, 环保或者ui闪烁是否比更新数据效果要差?
    protected void refreshChangedData(@NonNull ListView listView, @NonNull List<ChartItem> chartItems) {
        transactionAdapter = new TransactionAdapter(getContext(), chartItems, transactionViewModel);
        listView.setAdapter(transactionAdapter);
        setHasOptionsMenu(!chartItems.isEmpty());
    }

    @MainThread
    protected void doLoadingTask(boolean force) {
        transactionViewModel.load(getTypeface(), force);
    }

    @Override
    protected LiveData<List<ChartItem>> setViewModel() {
        transactionViewModel = newViewModel(TransactionViewModel.class);

        Bundle bundle = getArguments();
        boolean isTime = null == bundle ? false : bundle.getBoolean("type");
        transactionViewModel.setType(isTime);

        transactionViewModel.clickItem.observe(this, new Observer<ChartItem>() {
            @Override
            public void onChanged(@Nullable ChartItem item) {
                onClickById(item);
            }
        });

        transactionViewModel.networkState.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                Log.d(TAG, "networking value " + integer);
                if (null != transactionAdapter) {
                    transactionAdapter.setNetworkState(integer);
                }
            }
        });

        startLoading(transactionViewModel.refreshState);

        return transactionViewModel.items;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        transactionViewModel.dispose();
    }

    private void onClickById(@Nullable ChartItem item) {
        if (null == item) {
            Log.e(TAG, "onClickById skip with null item.");
            return;
        }

        int id = item.getId();
        Log.v(TAG, "onClickById, id = " + id);
        if (ChartItem.ID.STASTIC_PREVIEW == id) {
            // todo: handle click event on item.
        } else {
            Log.e(TAG, "Unexpected item id: " + id);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_next, menu);
        menu.findItem(R.id.action_next).setTitle(R.string.data_filter);
    }
}
