package com.harvestasm.apm.browser;

import android.content.Context;

import com.harvestasm.apm.base.BaseChartAdapter;
import com.harvestasm.chart.listviewitems.ChartItem;

import java.util.List;

/** adapter that supports 3 different item types */
class BrowserAdapter extends BaseChartAdapter<BrowserViewModel> {
    public BrowserAdapter(Context context, List<ChartItem> objects, BrowserViewModel viewMultiChartModel) {
        super(context, objects, viewMultiChartModel);
    }
}
