package com.harvestasm.apm.transaction;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.harvestasm.chart.listviewitems.ChartItem;

import java.util.List;

/** adapter that supports 3 different item types */
class TransactionAdapter extends ArrayAdapter<ChartItem> {
    private final TransactionViewModel viewMultiChartModel;

    public TransactionAdapter(Context context, List<ChartItem> objects, TransactionViewModel viewMultiChartModel) {
        super(context, 0, objects);
        this.viewMultiChartModel = viewMultiChartModel;
    }

    @Override
    public @NonNull View getView(final int position, @Nullable View convertView, @Nullable ViewGroup parent) {
        View view = getItem(position).getView(position, convertView, getContext());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewMultiChartModel.performClick(getItem(position));
            }
        });
        return view;
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
