package com.harvestasm.apm.preview;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.LayoutRes;
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
public class PreviewListFragment extends RefreshListFragment<ChartItem, ListView> {
    private static final String TAG = PreviewListFragment.class.getSimpleName();

    private PreviewViewModel previewViewModel;
    private PreviewAdapter cda;

    @Override
    protected @LayoutRes int getCollectionLayoutResourceId() {
        return R.layout.including_listview;
    }

    protected void refreshChangedData(@NonNull ListView lv, @NonNull List<ChartItem> chartItems) {
        cda = new PreviewAdapter(getContext(), chartItems, previewViewModel);
        lv.setAdapter(cda);
        setHasOptionsMenu(!chartItems.isEmpty());
    }

    protected void doLoadingTask(boolean force) {
        previewViewModel.load(getTypeface());
    }

    @Override
    protected LiveData<List<ChartItem>> setViewModel() {
        previewViewModel = newViewModel(PreviewViewModel.class);

        previewViewModel.clickItem.observe(this, new Observer<ChartItem>() {
            @Override
            public void onChanged(@Nullable ChartItem item) {
                onClickById(item);
            }
        });

        previewViewModel.networkState.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                Log.d(TAG, "networking value " + integer);
                if (null != cda) {
                    cda.setNetworkState(integer);
                }
            }
        });

        startLoading(previewViewModel.refreshState);

        return previewViewModel.items;
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_next) {
            // todo: close the preview activity or show error when with error
            previewViewModel.pushCache();
//            getActivity().finish();
        } else {
            return super.onOptionsItemSelected(item);
        }

        return true;
    }
}
