package com.harvestasm.apm.imepicker;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.harvestasm.apm.home.HomeDeviceItem;
import com.harvestasm.apm.sample.R;
import com.harvestasm.base.viewholder.BaseSwipeRefreshFragment;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class ImePickerFragment extends BaseSwipeRefreshFragment<HomeDeviceItem.AppItem, RecyclerView> {
    private static final String TAG = ImePickerFragment.class.getSimpleName();

    private ImePickerViewModel viewMultiChartModel;
    private ImePickerAdapter cda;

    @Override
    protected @LayoutRes int getCollectionLayoutResourceId() {
        return R.layout.including_recyclerview;
    }

    protected void refreshChangedData(RecyclerView lv, List<HomeDeviceItem.AppItem> chartItems) {
        if (null == cda) {
            cda = new ImePickerAdapter(getContext(), chartItems, viewMultiChartModel);
            lv.setAdapter(cda);
        } else {
            cda.notifyAdapter(chartItems, false);
        }
    }

    protected void doLoadingTask() {
        viewMultiChartModel.load(getTypeface());
    }

    @Override
    protected LiveData<List<HomeDeviceItem.AppItem>> setViewModel() {
        viewMultiChartModel = newViewModel(ImePickerViewModel.class);

        viewMultiChartModel.clickItem.observe(this, new Observer<HomeDeviceItem.AppItem>() {
            @Override
            public void onChanged(@Nullable HomeDeviceItem.AppItem item) {
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

    private void onClickById(@Nullable HomeDeviceItem.AppItem item) {
        if (null == item) {
            Log.e(TAG, "onClickById skip with null item.");
            return;
        }
    }
}
