package com.harvestasm.chart;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.harvestasm.apm.sample.R;
import com.harvestasm.chart.listviewitems.ChartItem;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MultiChartFragment extends Fragment {

    private static final String TAG = MultiChartFragment.class.getSimpleName();

    private MultiChartViewModel viewMultiChartModel;

    private ListView lv;
    private MultiChartAdapter cda;

    public MultiChartFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_multichart, container, false);
        lv = view.findViewById(R.id.listView1);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setViewModel();
        startLoading();
    }


    private void startLoading() {
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Regular.ttf");
        viewMultiChartModel.load(typeface);
    }

    private void setViewModel() {
        viewMultiChartModel = getViewModel();

        viewMultiChartModel.items.observe(this, new Observer<ArrayList<ChartItem>>() {
            @Override
            public void onChanged(@Nullable ArrayList<ChartItem> chartItems) {
                if (null == chartItems) {
                    Log.w(TAG, "null data comes.");
                } else {
                    Log.d(TAG, "data size " + chartItems.size());
                    cda = new MultiChartAdapter(getContext(), chartItems);
                    lv.setAdapter(cda);
                }
            }
        });

        viewMultiChartModel.loadingState.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                Log.d(TAG, "loading value " + aBoolean);
                if (null != cda) {
                    cda.setLoading(aBoolean);
                }
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
    }

    private MultiChartViewModel getViewModel() {
        return ViewModelProviders.of(this/*, new MultiChartViewModelFactory()*/)
                .get(MultiChartViewModel.class);
    }
}
