package com.harvestasm.chart.listviewitems;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.ChartData;
import com.harvestasm.apm.sample.R;

public class BarChartItem extends ChartItem {
    
    private Typeface mTf;
    
    public BarChartItem(ChartData<?> cd, String title, Typeface typeface) {
        super(cd, title);

        mTf = typeface;
    }

    @Override
    public int getItemType() {
        return TYPE_BARCHART;
    }

    @Override
    public View getView(int position, View convertView, Context c) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(c).inflate(
                    R.layout.list_item_barchart, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.bind(this);
        return convertView;
    }
    
    private static class ViewHolder extends BaseViewHolder{
        private BarChart chart;

        public ViewHolder(View convertView) {
            super(convertView);
            chart = convertView.findViewById(R.id.chart);
        }

        public void bind(BarChartItem item) {
            super.bind(item);

            // apply styling
            chart.getDescription().setEnabled(false);
            chart.setDrawGridBackground(false);
            chart.setDrawBarShadow(false);

            XAxis xAxis = chart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setTypeface(item.mTf);
            xAxis.setDrawGridLines(false);
            xAxis.setDrawAxisLine(true);

            YAxis leftAxis = chart.getAxisLeft();
            leftAxis.setTypeface(item.mTf);
            leftAxis.setLabelCount(5, false);
            leftAxis.setSpaceTop(20f);
            leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

            YAxis rightAxis = chart.getAxisRight();
            rightAxis.setTypeface(item.mTf);
            rightAxis.setLabelCount(5, false);
            rightAxis.setSpaceTop(20f);
            rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

            item.mChartData.setValueTypeface(item.mTf);

            // set data
            chart.setData((BarData) item.mChartData);
            chart.setFitBars(true);

            // do not forget to refresh the chart
//        chart.invalidate();
            chart.animateY(700);

        }
    }
}
