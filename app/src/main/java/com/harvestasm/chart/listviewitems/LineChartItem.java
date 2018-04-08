
package com.harvestasm.chart.listviewitems;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.LineData;
import com.harvestasm.apm.sample.R;

public class LineChartItem extends ChartItem {

    private Typeface mTf;

    public LineChartItem(ChartData<?> cd, String title, Typeface typeface) {
        super(cd, title);

        mTf = typeface;
    }

    @Override
    public int getItemType() {
        return TYPE_LINECHART;
    }

    @Override
    public View getView(int position, View convertView, Context c) {
        ViewHolder holder = null;

        if (convertView == null) {
            convertView = LayoutInflater.from(c).inflate(
                    R.layout.list_item_linechart, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.bind(this);

        return convertView;
    }

    private static class ViewHolder extends BaseViewHolder {
        private LineChart chart;

        public ViewHolder(View convertView) {
            super(convertView);
            chart = convertView.findViewById(R.id.chart);
        }

        public void bind(LineChartItem lineChartItem) {
            super.bind(lineChartItem);

            // apply styling
            // holder.chart.setValueTypeface(mTf);
            chart.getDescription().setEnabled(false);
            chart.setDrawGridBackground(false);

            XAxis xAxis = chart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setTypeface(lineChartItem.mTf);
            xAxis.setDrawGridLines(false);
            xAxis.setDrawAxisLine(true);

            YAxis leftAxis = chart.getAxisLeft();
            leftAxis.setTypeface(lineChartItem.mTf);
            leftAxis.setLabelCount(5, false);
            leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

            YAxis rightAxis = chart.getAxisRight();
            rightAxis.setTypeface(lineChartItem.mTf);
            rightAxis.setLabelCount(5, false);
            rightAxis.setDrawGridLines(false);
            rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

            // set data
            chart.setData((LineData) lineChartItem.mChartData);

            // do not forget to refresh the chart
            // holder.chart.invalidate();
            chart.animateX(750);
        }
    }
}
