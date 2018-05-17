package com.harvestasm.chart;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.WorkerThread;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.harvestasm.apm.repository.model.ApmActivityItem;
import com.harvestasm.chart.custom.MyValueFormatter;
import com.harvestasm.chart.listviewitems.BarChartItem;
import com.harvestasm.chart.listviewitems.ChartItem;
import com.harvestasm.chart.listviewitems.LineChartItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import typany.apm.com.google.gson.Gson;

public class ChartItemHelper {
    @WorkerThread
    public static void buildEntry(List<BarEntry> entries, float value, int index) {
        entries.add(new BarEntry(index, value));
    }

    public static void buildEntry(List<BarEntry> entries, double max, double min, long size, int index) {
//        yVals.add(new BarEntry(
//                index,
//                new double[]{ min / size, (max - min) / size},
//                getResources().getDrawable(R.drawable.ic_home_black_24dp)));
        entries.add(new BarEntry(index, new float[]{ (float)min / size, (float) (max - min) / size}));
    }
    @WorkerThread
    public static final BarChartItem generateDataBar(List<BarEntry> entries, String key,
                                                     String title, Typeface typeface) {
        if (key.startsWith("cpu/")) {
//            BarDataSet set1 = new BarDataSet(entries, key);
//            set1.setDrawIcons(false);
//            set1.setColors(getColors());
//            set1.setStackLabels(new String[]{"平均", "最大"});
//
//            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
//            dataSets.add(set1);
//
//            BarData data = new BarData(dataSets);
//            data.setValueFormatter(new MyValueFormatter());
//            data.setValueTextColor(Color.WHITE);
            BarData data = generateStackedBarData(entries, key);
            return new BarChartItem(data, title, ChartItem.ID.STASTIC_PREVIEW, typeface);
        } else {
            BarDataSet d = new BarDataSet(entries, key);
            d.setColors(ColorTemplate.VORDIPLOM_COLORS);
            d.setHighLightAlpha(255);
            BarData cd = new BarData(d);
            cd.setBarWidth(0.9f);
            return new BarChartItem(cd, title, ChartItem.ID.STASTIC_PREVIEW, typeface);
        }
    }

    public static int[] getColors() {
        int stacksize = 2;

        // have as many colors as stack-values per entry
        int[] colors = new int[stacksize];

        for (int i = 0; i < colors.length; i++) {
            colors[i] = ColorTemplate.MATERIAL_COLORS[i];
        }

        return colors;
    }

    public static BarData generateStackedBarData(List<BarEntry> yVals, String key) {
        BarDataSet set1 = new BarDataSet(yVals, key);
        set1.setDrawIcons(false);
        set1.setColors(ChartItemHelper.getColors());
        set1.setStackLabels(new String[]{"平均", "最大"});

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(dataSets);
        data.setValueFormatter(new MyValueFormatter());
        data.setValueTextColor(Color.WHITE);
        return data;
    }

    @WorkerThread
    public static final LineChartItem generateDataLine(List<Entry> entries, String label,
                                                       String title, Typeface typeface) {
        LineDataSet d = new LineDataSet(entries, label);
        d.setColors(ColorTemplate.VORDIPLOM_COLORS);
        d.setLineWidth(2.5f);
        d.setCircleRadius(4.5f);
        d.setHighLightColor(Color.rgb(244, 117, 117));
//        d.setDrawValues(false);

        LineData cd = new LineData(d);
        return new LineChartItem(cd, title, ChartItem.ID.STASTIC_PREVIEW, typeface);
    }

    public static void parseVitalUnitList(Map<String, List<ApmActivityItem.VitalUnit>> memoryByVitals,
                                          String str, String mapKey, boolean isMemoryChart) {
        ApmActivityItem.Vitals[] vitals = new Gson().fromJson(str, ApmActivityItem.Vitals[].class);
        if (null != vitals) {
            for (ApmActivityItem.Vitals v : vitals) {
                List<ApmActivityItem.VitalUnit> vitalItems = isMemoryChart ? v.getMemory() : v.getCpu();
                if (null != vitalItems && !vitalItems.isEmpty()) {
                    List<ApmActivityItem.VitalUnit> unitList = memoryByVitals.get(mapKey);
                    if (null == unitList) {
                        unitList = new ArrayList<>();
                        memoryByVitals.put(mapKey, unitList);
                    }
                    unitList.addAll(vitalItems);
                }
            }
        }
    }

}
