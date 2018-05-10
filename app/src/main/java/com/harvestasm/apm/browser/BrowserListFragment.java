package com.harvestasm.apm.browser;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.LayoutRes;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.harvestasm.apm.sample.R;
import com.harvestasm.base.RefreshListFragment;
import com.harvestasm.chart.listviewitems.ChartItem;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class BrowserListFragment extends RefreshListFragment<ChartItem, ListView> {
    private static final String TAG = BrowserListFragment.class.getSimpleName();

    private BrowserViewModel browserViewModel;
    private BrowserAdapter browserAdapter;

    @Override
    protected @LayoutRes int getCollectionLayoutResourceId() {
        return R.layout.including_listview;
    }

    // todo: 每次都重新赋予新的adapter实例, 环保或者ui闪烁是否比更新数据效果要差?
    protected void refreshChangedData(@NonNull ListView listView, @NonNull List<ChartItem> chartItems) {
        browserAdapter = new BrowserAdapter(getContext(), chartItems, browserViewModel);
        listView.setAdapter(browserAdapter);
        setHasOptionsMenu(!chartItems.isEmpty());
    }

    @MainThread
    protected void doLoadingTask(boolean force) {
        browserViewModel.load(getTypeface(), force);
    }

    @Override
    protected LiveData<List<ChartItem>> setViewModel() {
        browserViewModel = newViewModel(BrowserViewModel.class);

        browserViewModel.clickItem.observe(this, new Observer<ChartItem>() {
            @Override
            public void onChanged(@Nullable ChartItem item) {
                onClickById(item);
            }
        });

        browserViewModel.networkState.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                Log.d(TAG, "networking value " + integer);
                if (null != browserAdapter) {
                    browserAdapter.setNetworkState(integer);
                }
            }
        });

        startLoading(browserViewModel.refreshState);

        return browserViewModel.items;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        browserViewModel.dispose();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_next) {
            DataStorage.get().currentState.setValue(true);
        } else {
            return super.onOptionsItemSelected(item);
        }

        return true;
    }
}
