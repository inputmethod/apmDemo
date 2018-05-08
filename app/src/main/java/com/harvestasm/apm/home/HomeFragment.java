package com.harvestasm.apm.home;

import android.arch.lifecycle.LiveData;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.harvestasm.apm.sample.R;
import com.harvestasm.base.viewholder.BaseSwipeRefreshFragment;

import java.util.List;

/***
 * 首页数据显示，维护管理和RecyclerView对应的ViewModel, 含HomeItem列表的数据Adapter。
 */
public class HomeFragment extends BaseSwipeRefreshFragment<HomeItem, RecyclerView> {

    private static final String TAG = HomeFragment.class.getSimpleName();

    private HomeModel homeModel;

    private HomeAdapter cda;

    @Override
    protected void refreshChangedData(RecyclerView lv, List<HomeItem> dataItems) {
        Log.d(TAG, "data size " + dataItems.size());
        cda = new HomeAdapter(getContext(), dataItems, homeModel);
        lv.setAdapter(cda);
    }

    @Override
    protected LiveData<List<HomeItem>> setViewModel() {
        homeModel = newViewModel(HomeModel.class);
        startLoading(homeModel.refreshState);
        return homeModel.items;
    }

    @Override
    protected void doLoadingTask(boolean force) {
        homeModel.load(getTypeface());
    }

    @Override
    protected int getCollectionLayoutResourceId() {
        return R.layout.including_recyclerview;
    }
}
