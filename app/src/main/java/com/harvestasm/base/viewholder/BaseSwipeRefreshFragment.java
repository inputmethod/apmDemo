package com.harvestasm.base.viewholder;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.harvestasm.apm.sample.R;

import java.util.List;

/***
 * 带标准下拉Refresh刷新控件的Fragment抽象基类，实现view和activity创建时的回调流程。
 * 管理维护 @SwipeRefreshLayout 下拉控件和用于显示列表的list/recycler控件，开始加载
 * 数据并监听回调，刷新用户显示
 *
 * @param <T> T是数据item的类型
 * @param <V> V是ListView或者RecyclerView之类的用于接收
 */
abstract public class BaseSwipeRefreshFragment<T, V extends View> extends Fragment {
    private static final String TAG = BaseSwipeRefreshFragment.class.getSimpleName();

    private @Nullable Typeface typeface;  // lazy instance and only accessed by getTypeface()

    private @NonNull SwipeRefreshLayout refreshLayout;
    private @NonNull V listView;  // list/grid/recycler view to display a list items

    /***
     * 创建Fragment的root view, 记录下拉刷新的控件
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container,
                                   Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_swipe_refresh_base, container, false);
        refreshLayout = view.findViewById(R.id.swipe_refresh);
        listView = (V) inflater.inflate(getCollectionLayoutResourceId(), null, false);
        if (null == listView) {
            throw new IllegalStateException(TAG + " MUST has not null list or recycler view.");
        }

        refreshLayout.addView(listView);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                doLoadingTask();
            }
        });

        return view;
    }

    protected abstract @LayoutRes int getCollectionLayoutResourceId();

    @Override
    public final void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setViewModel().observe(this, new Observer<List<T>>() {
            @Override
            public void onChanged(@Nullable List<T> dataItems) {
                if (null == dataItems) {
                    Log.w(TAG, "null data comes.");
                } else {
                    Log.d(TAG, "data size " + dataItems.size());
                    refreshChangedData(listView, dataItems);
                }
            }
        });
    }

    protected abstract void refreshChangedData(V lv, List<T> dataItems);
    protected abstract LiveData<List<T>> setViewModel();

    protected abstract void doLoadingTask();

    private void setRefreshing(Boolean aBoolean) {
        refreshLayout.setRefreshing(aBoolean);
    }

    protected final Typeface getTypeface() {
        if (null == typeface) {
            typeface = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Regular.ttf");
        }
        return typeface;
    }

    protected final void startLoading(LiveData<Boolean> refreshState) {
        refreshState.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                Log.d(TAG, "loading value " + aBoolean);
                setRefreshing(aBoolean);
            }
        });

        doLoadingTask();
    }

    protected final <T extends ViewModel> T newViewModel(Class<T> modelClass) {
        return ViewModelProviders.of(this/*, new MultiChartViewModelFactory()*/)
                .get(modelClass);
    }
}