package com.harvestasm.chart.listviewitems;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.data.ChartData;
import com.harvestasm.apm.sample.R;

/**
 * baseclass of the chart-listview items
 * @author philipp
 *
 */
public abstract class ChartItem {
    
    protected static final int TYPE_BARCHART = 0;
    protected static final int TYPE_LINECHART = 1;
    protected static final int TYPE_PIECHART = 2;
    
    protected ChartData<?> mChartData;

    private final String title;
    
    public ChartItem(ChartData<?> cd) {
        this(cd, "");
    }

    public ChartItem(ChartData<?> cd, String title) {
        this.mChartData = cd;
        this.title = title;
    }
    
    public abstract int getItemType();
    
    public abstract View getView(int position, View convertView, Context c);

    protected static class BaseViewHolder {
         private TextView title;

        protected BaseViewHolder(View convertView) {
            title = convertView.findViewById(R.id.title);
        }

        protected void bind(ChartItem item) {
            if (null != title) {
                title.setText(item.title);
            }
        }
    }
}
