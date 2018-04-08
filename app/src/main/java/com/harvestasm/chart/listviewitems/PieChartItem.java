
package com.harvestasm.chart.listviewitems;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.harvestasm.apm.sample.R;

public class PieChartItem extends ChartItem {

    private Typeface mTf;
    public PieChartItem(ChartData<?> cd, String title, int id, Typeface typeface) {
        super(cd, title, id);

        mTf = typeface;
    }

    @Override
    public int getItemType() {
        return TYPE_PIECHART;
    }

    @Override
    public View getView(int position, View convertView, Context c) {

        ViewHolder holder = null;

        if (convertView == null) {
            convertView = LayoutInflater.from(c).inflate(
                    R.layout.list_item_piechart, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.bind(this);

        return convertView;
    }

    private static class ViewHolder extends BaseViewHolder {
        private final PieChart chart;
        private final SpannableString mCenterText;

        protected ViewHolder(View convertView) {
            super(convertView);
            chart = convertView.findViewById(R.id.chart);
            mCenterText = generateCenterText();
        }

        public void bind(PieChartItem item) {
            // apply styling
            chart.getDescription().setEnabled(false);
            chart.setHoleRadius(52f);
            chart.setTransparentCircleRadius(57f);
            chart.setCenterText(mCenterText);
            chart.setCenterTextTypeface(item.mTf);
            chart.setCenterTextSize(9f);
            chart.setUsePercentValues(true);
            chart.setExtraOffsets(5, 10, 50, 10);

            item.mChartData.setValueFormatter(new PercentFormatter());
            item.mChartData.setValueTypeface(item.mTf);
            item.mChartData.setValueTextSize(11f);
            item.mChartData.setValueTextColor(Color.WHITE);
            // set data
            chart.setData((PieData) item.mChartData);

            Legend l = chart.getLegend();
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
            l.setOrientation(Legend.LegendOrientation.VERTICAL);
            l.setDrawInside(false);
            l.setYEntrySpace(0f);
            l.setYOffset(0f);

            // do not forget to refresh the chart
            // holder.chart.invalidate();
            chart.animateY(900);
        }

        private SpannableString generateCenterText() {
            float[] textSize = { 1.6f, 0.9f, 1.4f };
            int[] textColor = { ColorTemplate.VORDIPLOM_COLORS[0], Color.GRAY, ColorTemplate.getHoloBlue() };
            String[] textArray = {"MPAndroidChart", "created by", "Philipp Jahoda"};
            return generateCenterText(textArray, textSize, textColor);
        }

        private SpannableString generateCenterText(String[] textArray, float[] textSize, int[] textColor) {
            String text = TextUtils.join("\n", textArray);
            SpannableString s = new SpannableString(text);
            for (int start = 0, end = 0, i = 0; i < textArray.length; i++) {
                end = start + textArray[i].length();
                s.setSpan(new RelativeSizeSpan(textSize[i]), start, end, 0);
                s.setSpan(new ForegroundColorSpan(textColor[i]), start, end, 0);
                start = end;
            }
//            s.setSpan(new RelativeSizeSpan(1.6f), 0, 14, 0);
//            s.setSpan(new ForegroundColorSpan(ColorTemplate.VORDIPLOM_COLORS[0]), 0, 14, 0);
//            s.setSpan(new RelativeSizeSpan(.9f), 14, 25, 0);
//            s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, 25, 0);
//            s.setSpan(new RelativeSizeSpan(1.4f), 25, s.length(), 0);
//            s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), 25, s.length(), 0);
            return s;
        }
    }
}
