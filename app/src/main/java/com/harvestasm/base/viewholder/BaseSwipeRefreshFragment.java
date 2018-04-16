package com.harvestasm.base.viewholder;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
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
 *
 * @param <T> T是数据item的类型
 * @param <V> V是ListView或者RecyclerView之类的用于接收
 */
abstract public class BaseSwipeRefreshFragment<T, V extends View> extends Fragment {
    private static final String TAG = BaseSwipeRefreshFragment.class.getSimpleName();

    private Typeface typeface;
    private SwipeRefreshLayout refreshLayout;

    private V lv;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        refreshLayout = view.findViewById(R.id.swipe_refresh);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                doLoadingTask();
            }
        });

    }

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
                    refreshChangedData(lv, dataItems);
                }
            }
        });
    }

    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutResourceId(), container, false);
        lv = view.findViewById(getCollectionViewId());
        return view;
    }

    protected abstract void refreshChangedData(V lv, List<T> dataItems);
    protected abstract LiveData<List<T>> setViewModel();

    protected abstract int getCollectionViewId();

    protected abstract @LayoutRes int getLayoutResourceId();

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
}
