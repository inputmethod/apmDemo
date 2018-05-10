package com.harvestasm.apm.filter;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.harvestasm.apm.browser.DataStorage;
import com.harvestasm.apm.sample.R;
import com.harvestasm.base.RefreshListFragment;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class FilterListFragment extends RefreshListFragment<FilterCategoryModel, RecyclerView> {
    private static final String TAG = FilterListFragment.class.getSimpleName();

    private FilterViewModel filterViewModel;
    private FilterAdapter filterAdapter;

    @Override
    protected @LayoutRes int getCollectionLayoutResourceId() {
        return R.layout.including_recyclerview;
    }

    protected void refreshChangedData(@NonNull RecyclerView recyclerView, @NonNull List<FilterCategoryModel> chartItems) {
        if (null == filterAdapter) {
            filterAdapter = new FilterAdapter(chartItems, filterViewModel);
            recyclerView.setAdapter(filterAdapter);
        } else {
            filterAdapter.notifyAdapter(chartItems, false);
        }
        setHasOptionsMenu(true);
    }

    protected void doLoadingTask(boolean force) {
        filterViewModel.load(force);
    }

    @Override
    protected LiveData<List<FilterCategoryModel>> setViewModel() {
        filterViewModel = newViewModel(FilterViewModel.class);

        filterViewModel.networkState.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                Log.d(TAG, "networking value " + integer);
                if (null != filterAdapter) {
                    filterAdapter.setNetworkState(integer);
                }
            }
        });

        startLoading(filterViewModel.refreshState);

        return filterViewModel.items;
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
            DataStorage.get().currentState.setValue(false);
        } else {
            return super.onOptionsItemSelected(item);
        }

        return true;
    }
}
