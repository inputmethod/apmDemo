
package com.harvestasm.chart;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ListView;

import com.harvestasm.apm.sample.R;
import com.harvestasm.chart.listviewitems.ChartItem;

import java.util.ArrayList;

/**
 * Demonstrates the use of charts inside a ListView. IMPORTANT: provide a
 * specific height attribute for the chart inside your listview-item
 * 
 * @author Philipp Jahoda
 */
public class MultiMultiChartActivity extends MultiChartBaseActivity {
    private static final String TAG = MultiMultiChartActivity.class.getSimpleName();

    private MultiChartViewModel viewMultiChartModel;

    private ListView lv;
    private MultiChartAdapter cda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_multichart);
        
        lv = findViewById(R.id.listView1);

        viewMultiChartModel = getViewModel();

//        cda = new MultiChartAdapter(getApplicationContext(), list);
//        lv.setAdapter(cda);
        viewMultiChartModel.items.observe(this, new Observer<ArrayList<ChartItem>>() {
            @Override
            public void onChanged(@Nullable ArrayList<ChartItem> chartItems) {
                if (null == chartItems) {
                    Log.w(TAG, "null data comes.");
                } else {
                    Log.d(TAG, "data size " + chartItems.size());
                    cda = new MultiChartAdapter(getApplicationContext(), chartItems);
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

        Typeface typeface = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
        viewMultiChartModel.load(typeface);
    }

    private MultiChartViewModel getViewModel() {
        return ViewModelProviders.of(this/*, new MultiChartViewModelFactory()*/)
                .get(MultiChartViewModel.class);
    }

}
