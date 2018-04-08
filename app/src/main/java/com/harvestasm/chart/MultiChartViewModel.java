package com.harvestasm.chart;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.graphics.Color;
import android.graphics.Typeface;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.harvestasm.chart.listviewitems.BarChartItem;
import com.harvestasm.chart.listviewitems.ChartItem;
import com.harvestasm.chart.listviewitems.LineChartItem;
import com.harvestasm.chart.listviewitems.PieChartItem;

import java.util.ArrayList;

// todo: simplest implement without repository to store data item.
public class MultiChartViewModel extends ViewModel {
    public final MutableLiveData<ArrayList<ChartItem>> items = new MutableLiveData<>();
    public final MutableLiveData<Boolean> loadingState = new MutableLiveData<>();
    public final MutableLiveData<Integer> networkState = new MutableLiveData<>();

    public void load(Typeface typeface) {
        loadingState.setValue(true);
        startLoadWorker(typeface);
    }

    // todo: load within worker thread.
    private void startLoadWorker(Typeface typeface) {
        ArrayList<ChartItem> list = new ArrayList<>();

        // 30 items
        for (int i = 0; i < 30; i++) {
            int mod = i % 5;
            if(mod == 0) {
                list.add(new LineChartItem(generateDataLine(), "内存占用" + i, typeface));
            } else if(mod == 1) {
                list.add(new BarChartItem(generateDataBar(), "键盘收起时间" + i, typeface));
            } else if(mod == 2) {
                list.add(new BarChartItem(generateDataBar(), "电流" + i, typeface));
            } else if (mod == 3) {
                list.add(new BarChartItem(generateDataBar(), "CPU占用" + i, typeface));
            } else if(mod == 4) {
                list.add(new PieChartItem(generateDataPie(), "设备", typeface));
            }
        }

        items.postValue(list);
        loadingState.postValue(false);
        networkState.postValue(0);
    }


    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return
     */
    private LineData generateDataLine() {
        int count = 12;
        int highLightColor = Color.rgb(244, 117, 117);

        String label1 = "Typany";
        LineDataSet d1 = generateLineDataSet(label1, count, highLightColor, 65, 40);

        String label2 = "Others";
        int normalColor = ColorTemplate.VORDIPLOM_COLORS[0];
        int circleColor = ColorTemplate.VORDIPLOM_COLORS[0];
        LineDataSet d2 = generateLineDataSet(label2, count, highLightColor,
                normalColor, circleColor, 40, 10);

        ArrayList<ILineDataSet> sets = new ArrayList<>();
        sets.add(d1);
        sets.add(d2);

        LineData cd = new LineData(sets);
        return cd;
    }

    private LineDataSet generateLineDataSet(String label, int count, int highLightColor,
                                            int normalColor, int circleColor, int seed, int offset) {
        LineDataSet d2 = generateLineDataSet(label, count, highLightColor, seed, offset);
        d2.setColor(normalColor);
        d2.setCircleColor(circleColor);
        return d2;
    }

    private LineDataSet generateLineDataSet(String label, int count, int highLightColor, int seed, int offset) {
        ArrayList<Entry> e1 = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            e1.add(new Entry(i, (int) (Math.random() * seed) + offset));
        }

        LineDataSet d1 = new LineDataSet(e1, label);
        d1.setLineWidth(2.5f);
        d1.setCircleRadius(4.5f);
        d1.setHighLightColor(highLightColor);
        d1.setDrawValues(false);

        return d1;
    }

    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return
     */
    private BarData generateDataBar() {

        ArrayList<BarEntry> entries = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            entries.add(new BarEntry(i, (int) (Math.random() * 70) + 30));
        }

        BarDataSet d = new BarDataSet(entries, "Typany");
        d.setColors(ColorTemplate.VORDIPLOM_COLORS);
        d.setHighLightAlpha(255);

        BarData cd = new BarData(d, d, d);
        cd.setBarWidth(0.9f);
        return cd;
    }

    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return
     */
    private PieData generateDataPie() {

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        for (int i = 0; i < 4; i++) {
            entries.add(new PieEntry((float) ((Math.random() * 70) + 30), "Quarter " + (i+1)));
        }

        PieDataSet d = new PieDataSet(entries, "");

        // space between slices
        d.setSliceSpace(2f);
        d.setColors(ColorTemplate.VORDIPLOM_COLORS);

        PieData cd = new PieData(d);
        return cd;
    }
}
