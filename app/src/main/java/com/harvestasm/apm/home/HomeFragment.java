package com.harvestasm.apm.home;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.harvestasm.apm.sample.R;
import com.harvestasm.base.viewholder.BaseSwipeRefreshFragment;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class HomeFragment extends BaseSwipeRefreshFragment<HomeItem, RecyclerView> {

    private static final String TAG = HomeFragment.class.getSimpleName();

    private HomeModel homeModel;

    private HomeAdapter cda;

    public HomeFragment() {
    }

    @Override
    protected void refreshChangedData(RecyclerView lv, List<HomeItem> dataItems) {
        Log.d(TAG, "data size " + dataItems.size());
        cda = new HomeAdapter(getContext(), dataItems, homeModel);
        lv.setAdapter(cda);
    }

    @Override
    protected LiveData<List<HomeItem>> setViewModel() {
        homeModel = getViewModel();
        startLoading(homeModel.refreshState);
        return homeModel.items;
    }

    @Override
    protected void doLoadingTask() {
        homeModel.load(getTypeface());
    }

    @Override
    protected int getCollectionViewId() {
        return R.id.list;
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_home;
    }

    private HomeModel getViewModel() {
        return ViewModelProviders.of(this)
                .get(HomeModel.class);
    }
}
