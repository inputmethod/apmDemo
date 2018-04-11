package com.harvestasm.apm.home;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.harvestasm.chart.listviewitems.BarChartItem;
import com.harvestasm.chart.listviewitems.ChartItem;
import com.harvestasm.chart.listviewitems.LineChartItem;
import com.harvestasm.chart.listviewitems.PieChartItem;

import java.util.ArrayList;
import java.util.List;

// todo: simplest implement without repository to store data item.
public class HomeModel extends ViewModel {
    private final static String TAG = HomeModel.class.getSimpleName();

    public final MutableLiveData<List<ChartItem>> items = new MutableLiveData<>();
    public final MutableLiveData<Boolean> loadingState = new MutableLiveData<>();
    public final MutableLiveData<Integer> networkState = new MutableLiveData<>();

    public final MutableLiveData<ChartItem> clickItem = new MutableLiveData<>();

    public void load(Typeface typeface) {
        loadingState.setValue(true);
        startLoadWorker(typeface);
    }

    // todo: load within worker thread.
    private void startLoadWorker(Typeface typeface) {
        List<ChartItem> list = new ArrayList<>();
        fillSampleCharItem(list, typeface);
        items.postValue(list);
        loadingState.postValue(false);
        networkState.postValue(0);
    }

    private void fillSampleCharItem(List<ChartItem> list, Typeface typeface) {
        list.add(new LineChartItem(generateDataLine(), "内存占用", ChartItem.ID.MEMORY, typeface));

        list.add(new BarChartItem(generateDataBar(), "键盘收起时间", ChartItem.ID.KEYBOARD_HIDE, typeface));
        list.add(new BarChartItem(generateDataBar(), "电流", ChartItem.ID.BATTARY, typeface));
        list.add(new BarChartItem(generateDataBar(), "CPU占用", ChartItem.ID.CPU, typeface));

        list.add(new BarChartItem(generateDataBar(), "Theme页滑动", ChartItem.ID.SKIN_SLIP, typeface));
        list.add(new BarChartItem(generateDataBar(), "emoji页滑动", ChartItem.ID.EMOJI_SLIP, typeface));
        list.add(new BarChartItem(generateDataBar(), "主键盘与符号键盘切换", ChartItem.ID.SYMBOL_KB_SWITCH, typeface));
        list.add(new BarChartItem(generateDataBar(), "主键盘与emoji键盘切换", ChartItem.ID.EMOJI_KB_SWITCH, typeface));
        list.add(new BarChartItem(generateDataBar(), "键盘到设置页面", ChartItem.ID.KB_SETTING, typeface));
        list.add(new BarChartItem(generateDataBar(), "键盘打字弹泡", ChartItem.ID.KB_BALLOOM, typeface));

        list.add(new PieChartItem(generateDataPie(), "演示PI", ChartItem.ID.DEMO_PI, typeface));
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
        BarDataSet d1 = generateBarDataSet("Typany", 3, 70, 30, ColorTemplate.VORDIPLOM_COLORS);
//        BarDataSet d2 = generateBarDataSet("Other", 4, 50, 40, ColorTemplate.LIBERTY_COLORS);
//        BarDataSet d3 = generateBarDataSet("竞品", 4, 40, 50, ColorTemplate.MATERIAL_COLORS);
        BarData cd = new BarData(d1);
        cd.setBarWidth(0.9f);
        return cd;
    }

    private BarDataSet generateBarDataSet(String label, int count, int seed, int offset, int[] colors) {
        ArrayList<BarEntry> entries = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            entries.add(new BarEntry(i, (int) (Math.random() * seed) + offset));
        }

        BarDataSet d = new BarDataSet(entries, label);
        d.setColors(colors);
        d.setHighLightAlpha(255);

        return d;
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

    public void performClick(ChartItem item) {
        clickItem.setValue(item);
    }

    public void parsePieChartItem(ChartItem item) {
        ChartData data = item.getChartData();
        if (data instanceof PieData) {
            PieData lineData = (PieData) data;
            List<IPieDataSet> list = lineData.getDataSets();
            Log.v(TAG, "parseLineChartItem, data set size " + list.size());
        }
    }

    public void parseBarChartItem(ChartItem item) {
        ChartData data = item.getChartData();
        if (data instanceof BarData) {
            BarData lineData = (BarData) data;
            List<IBarDataSet> list = lineData.getDataSets();
            Log.v(TAG, "parseLineChartItem, data set size " + list.size());
        }
    }

    public void parseLineChartItem(ChartItem item) {
        ChartData data = item.getChartData();
        if (data instanceof LineData) {
            LineData lineData = (LineData) data;
            List<ILineDataSet> list = lineData.getDataSets();
            Log.v(TAG, "parseLineChartItem, data set size " + list.size());
        }
    }
}
