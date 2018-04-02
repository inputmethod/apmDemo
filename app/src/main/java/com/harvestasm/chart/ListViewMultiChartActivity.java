
package com.harvestasm.chart;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.harvestasm.apm.sample.R;
import com.harvestasm.chart.listviewitems.ChartItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Demonstrates the use of charts inside a ListView. IMPORTANT: provide a
 * specific height attribute for the chart inside your listview-item
 * 
 * @author Philipp Jahoda
 */
public class ListViewMultiChartActivity extends DemoBase {
    private static final String TAG = ListViewMultiChartActivity.class.getSimpleName();

    private ListViewMultiChartModel viewMultiChartModel;

    private ListView lv;
    private ChartDataAdapter cda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_listview_chart);
        
        lv = findViewById(R.id.listView1);

        viewMultiChartModel = getViewModel();

//        cda = new ChartDataAdapter(getApplicationContext(), list);
//        lv.setAdapter(cda);
        viewMultiChartModel.items.observe(this, new Observer<ArrayList<ChartItem>>() {
            @Override
            public void onChanged(@Nullable ArrayList<ChartItem> chartItems) {
                if (null == chartItems) {
                    Log.w(TAG, "null data comes.");
                } else {
                    Log.d(TAG, "data size " + chartItems.size());
                    cda = new ChartDataAdapter(getApplicationContext(), chartItems);
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

    private ListViewMultiChartModel getViewModel() {
        return ViewModelProviders.of(this/*, new ListViewMultiChartModelFactory()*/)
                .get(ListViewMultiChartModel.class);
    }

    /** adapter that supports 3 different item types */
    private class ChartDataAdapter extends ArrayAdapter<ChartItem> {
        
        public ChartDataAdapter(Context context, List<ChartItem> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getItem(position).getView(position, convertView, getContext());
        }
        
        @Override
        public int getItemViewType(int position) {           
            // return the views type
            return getItem(position).getItemType();
        }
        
        @Override
        public int getViewTypeCount() {
            return 3; // we have 3 different item-types
        }

        public void setNetworkState(Integer integer) {

        }

        public void setLoading(Boolean aBoolean) {

        }
    }
}
