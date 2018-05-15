package com.harvestasm.apm.transaction;

import android.content.Context;

import com.harvestasm.apm.base.BaseChartAdapter;
import com.harvestasm.chart.listviewitems.ChartItem;

import java.util.List;

/** adapter that supports 3 different item types */
class TransactionAdapter extends BaseChartAdapter<TransactionViewModel> {
    public TransactionAdapter(Context context, List<ChartItem> objects, TransactionViewModel viewMultiChartModel) {
        super(context, objects, viewMultiChartModel);
    }
}
