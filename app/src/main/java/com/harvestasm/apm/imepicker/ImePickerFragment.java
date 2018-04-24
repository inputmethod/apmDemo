package com.harvestasm.apm.imepicker;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.harvestasm.apm.sample.R;
import com.harvestasm.base.viewholder.BaseSwipeRefreshFragment;

import java.util.List;

import typany.apm.agent.android.harvest.ApplicationInformation;

/**
 * A placeholder fragment containing a simple view.
 */
public class ImePickerFragment extends BaseSwipeRefreshFragment<ApplicationInformation, RecyclerView> {
    private static final String TAG = ImePickerFragment.class.getSimpleName();

    private ImePickerViewModel imePickerViewModel;
    private ImePickerAdapter cda;

    @Override
    protected @LayoutRes int getCollectionLayoutResourceId() {
        return R.layout.including_recyclerview;
    }

    protected void refreshChangedData(RecyclerView lv, List<ApplicationInformation> chartItems) {
        if (null == cda) {
            cda = new ImePickerAdapter(getContext(), chartItems, imePickerViewModel);
            cda.setEditMode(1);
            lv.setAdapter(cda);
        } else {
            cda.notifyAdapter(chartItems, false);
        }
    }

    protected void doLoadingTask() {
        imePickerViewModel.load(true);
    }

    @Override
    protected LiveData<List<ApplicationInformation>> setViewModel() {
        imePickerViewModel = newViewModel(ImePickerViewModel.class);

        imePickerViewModel.clickItem.observe(this, new Observer<ApplicationInformation>() {
            @Override
            public void onChanged(@Nullable ApplicationInformation item) {
                onClickById(item);
            }
        });

        imePickerViewModel.networkState.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                Log.d(TAG, "networking value " + integer);
                if (null != cda) {
                    cda.setNetworkState(integer);
                }
            }
        });

        startLoading(imePickerViewModel.refreshState);

        return imePickerViewModel.items;
    }

    private void onClickById(@Nullable ApplicationInformation item) {
        if (null == item) {
            Log.e(TAG, "onClickById skip with null item.");
            return;
        }
    }
}
