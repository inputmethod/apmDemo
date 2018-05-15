package com.harvestasm.apm.activity;

import android.content.Context;

import com.harvestasm.apm.base.BaseChartAdapter;
import com.harvestasm.chart.listviewitems.ChartItem;

import java.util.List;

/** adapter that supports 3 different item types */
class ActivityAdapter extends BaseChartAdapter<ActivityViewModel> {
    public ActivityAdapter(Context context, List<ChartItem> objects, ActivityViewModel viewMultiChartModel) {
        super(context, objects, viewMultiChartModel);
    }
}
