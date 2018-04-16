package com.harvestasm.base.viewholder;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;

import com.harvestasm.apm.sample.R;

abstract public class BaseSwipeRefreshFragment extends Fragment {
    private static final String TAG = BaseSwipeRefreshFragment.class.getSimpleName();

    private Typeface typeface;
    private SwipeRefreshLayout refreshLayout;

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
