package com.harvestasm.apm.preview;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ListView;

import com.harvestasm.apm.sample.R;
import com.harvestasm.base.viewholder.BaseSwipeRefreshFragment;
import com.harvestasm.chart.listviewitems.ChartItem;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class PreviewFragment extends BaseSwipeRefreshFragment<ChartItem, ListView> {
    private static final String TAG = PreviewFragment.class.getSimpleName();

    private PreviewViewModel viewMultiChartModel;
    private PreviewAdapter cda;

    @Override
    protected @LayoutRes int getCollectionLayoutResourceId() {
        return R.layout.including_listview;
    }

    protected void refreshChangedData(ListView lv, List<ChartItem> chartItems) {
        cda = new PreviewAdapter(getContext(), chartItems, viewMultiChartModel);
        lv.setAdapter(cda);
    }

    protected void doLoadingTask() {
        viewMultiChartModel.load(getTypeface());
    }

    @Override
    protected LiveData<List<ChartItem>> setViewModel() {
        viewMultiChartModel = newViewModel(PreviewViewModel.class);

        viewMultiChartModel.clickItem.observe(this, new Observer<ChartItem>() {
            @Override
            public void onChanged(@Nullable ChartItem item) {
                onClickById(item);
            }
        });

        viewMultiChartModel.networkState.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                Log.d(TAG, "networking value " + integer);
                if (null != cda) {
                    cda.setNetworkState(integer);
                }
            }
        });

        startLoading(viewMultiChartModel.refreshState);

        return viewMultiChartModel.items;
    }

    private void onClickById(@Nullable ChartItem item) {
        if (null == item) {
            Log.e(TAG, "onClickById skip with null item.");
            return;
        }

        int id = item.getId();
        Log.v(TAG, "onClickById, id = " + id);
        if (ChartItem.ID.MEMORY == id) {
            viewMultiChartModel.parseLineChartItem(item);
        } else if (ChartItem.ID.KEYBOARD_HIDE == id) {
            viewMultiChartModel.parseBarChartItem(item);
        } else if (ChartItem.ID.BATTARY == id) {
            viewMultiChartModel.parseBarChartItem(item);
        } else if (ChartItem.ID.CPU == id) {
            viewMultiChartModel.parseBarChartItem(item);
        } else if (ChartItem.ID.SKIN_SLIP == id) {
            viewMultiChartModel.parseBarChartItem(item);
        } else if (ChartItem.ID.EMOJI_SLIP == id) {
            viewMultiChartModel.parseBarChartItem(item);
        } else if (ChartItem.ID.SYMBOL_KB_SWITCH == id) {
            viewMultiChartModel.parseBarChartItem(item);
        } else if (ChartItem.ID.EMOJI_KB_SWITCH == id) {
            viewMultiChartModel.parseBarChartItem(item);
        } else if (ChartItem.ID.KB_SETTING == id) {
            viewMultiChartModel.parseBarChartItem(item);
        } else if (ChartItem.ID.KB_BALLOOM == id) {
            viewMultiChartModel.parseBarChartItem(item);
        } else if (ChartItem.ID.DEMO_PI == id) {
            viewMultiChartModel.parsePieChartItem(item);
        } else {
            Log.e(TAG, "Unknown item id: " + id);
        }
    }
}
