package com.harvestasm.apm.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.harvestasm.chart.listviewitems.ChartItem;

import java.util.List;

/** adapter that supports 3 different item types */
public class BaseChartAdapter<T extends BaseChartViewModel> extends ArrayAdapter<ChartItem> {
    private final T chartViewModel;

    public BaseChartAdapter(Context context, List<ChartItem> objects, T chartViewModel) {
        super(context, 0, objects);
        this.chartViewModel = chartViewModel;
    }

    @Override
    public @NonNull View getView(final int position, @Nullable View convertView, @Nullable ViewGroup parent) {
        View view = getItem(position).getView(position, convertView, getContext());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chartViewModel.performClick(getItem(position));
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
