package com.harvestasm.apm.preview;

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
import com.harvestasm.apm.reporter.ApmConnectSourceIndex;
import com.harvestasm.apm.reporter.ApmDataSourceIndex;
import com.harvestasm.apm.reporter.ApmSourceGroup;
import com.harvestasm.apm.repository.ApmRepository;
import com.harvestasm.apm.repository.model.ApmSourceConnect;
import com.harvestasm.apm.repository.model.ApmSourceData;
import com.harvestasm.apm.repository.model.search.ApmBaseSearchResponse;
import com.harvestasm.apm.repository.model.search.ApmBaseUnit;
import com.harvestasm.apm.repository.model.search.ApmConnectSearchResponse;
import com.harvestasm.apm.repository.model.search.ApmDataSearchResponse;
import com.harvestasm.apm.utils.ApmRepositoryHelper;
import com.harvestasm.chart.listviewitems.BarChartItem;
import com.harvestasm.chart.listviewitems.ChartItem;
import com.harvestasm.chart.listviewitems.LineChartItem;
import com.harvestasm.chart.listviewitems.PieChartItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// todo: simplest implement without repository to store data item.
public class PreviewViewModel extends ViewModel {
    private final static String TAG = PreviewViewModel.class.getSimpleName();

    private final ApmRepository repository = new ApmRepository();

    public final MutableLiveData<List<ChartItem>> items = new MutableLiveData<>();
    public final MutableLiveData<Boolean> refreshState = new MutableLiveData<>();
    public final MutableLiveData<Integer> networkState = new MutableLiveData<>();

    public final MutableLiveData<ChartItem> clickItem = new MutableLiveData<>();

    private ApmConnectSearchResponse connectResponse = null;
    private ApmBaseSearchResponse<ApmSourceData> dataResponse = null;

    private void resetForLoading() {
        refreshState.setValue(true);
        networkState.postValue(0);

        connectResponse = null;
        dataResponse = null;
    }

    private void onDataLoaded(List<ChartItem> list) {
        items.setValue(list);

        refreshState.setValue(false);
        networkState.postValue(0);

    }

    public void load(final Typeface typeface) {
        resetForLoading();

        final ApmRepositoryHelper.CallBack callBack = new ApmRepositoryHelper.CallBack() {
            @Override
            public void onConnectResponse(ApmConnectSearchResponse responseBody) {
                connectResponse = responseBody;
                checkResult(typeface);
            }

            @Override
            public void onDataResponse(ApmDataSearchResponse responseBody) {
                dataResponse = responseBody;
                checkResult(typeface);
            }
        };

        ApmRepositoryHelper.doLoadTask(repository, callBack);
    }

    private void checkResult(Typeface typeface) {
        if (null == connectResponse || null == dataResponse) {
            Log.i(TAG, "checkResult, skip and wait until all data loaded.");
            return;
        }

        ApmConnectSourceIndex connectSourceIndex = new ApmConnectSourceIndex(connectResponse);
        ApmDataSourceIndex dataSourceIndex = new ApmDataSourceIndex(dataResponse);
        List<ApmSourceGroup> deviceGroupList = ApmSourceGroup.parseSourceGroup(dataSourceIndex, connectSourceIndex);

        List<ChartItem> list = new ArrayList<>();

        // parse devices
        list.add(new LineChartItem(generateDataLine(deviceGroupList), "设备分布图", ChartItem.ID.STASTIC_BY_DEVICE, typeface));

        // parse apps
        HashMap<String, List<ApmBaseUnit<ApmSourceConnect>>> connectUnits = connectSourceIndex.getAppIndexMap();
        list.add(new BarChartItem(generateDataBar(connectUnits), "App分布", ChartItem.ID.STASTIC_BY_APP, typeface));
        list.add(new PieChartItem(generateDataPie(connectUnits), "App分布",  ChartItem.ID.STASTIC_BY_APP, typeface));

        // final result list.
        onDataLoaded(list);
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

    private ChartData<?> generateDataLine(List<ApmSourceGroup> deviceGroupList) {
        int count = deviceGroupList.size();
        int highLightColor = Color.rgb(244, 117, 117);

        String label1 = "Connect";
        String label2 = "Data";

        int normalColor = ColorTemplate.VORDIPLOM_COLORS[0];
        int circleColor = ColorTemplate.VORDIPLOM_COLORS[0];

        ArrayList<Entry> e1 = new ArrayList<>();
        ArrayList<Entry> e2 = new ArrayList<>();
        for(int i = 0; i < count; i++) {
            ApmSourceGroup group = deviceGroupList.get(i);
            addEntry(e1, i, group.getConnectSource());
            addEntry(e2, i, group.getDataSource());
        }

        LineDataSet d1 = generateLineDataSet(label1, e1, highLightColor);
        LineDataSet d2 = generateLineDataSet(label2, e2, highLightColor);
        d2.setColor(normalColor);
        d2.setCircleColor(circleColor);

        ArrayList<ILineDataSet> sets = new ArrayList<>();
        sets.add(d1);
        sets.add(d2);

        LineData cd = new LineData(sets);
        return cd;
    }

    private static void addEntry(List<Entry> list, int i, List dataList) {
        int count = null == dataList ? 0 : dataList.size();
        list.add(new Entry(i, count));
    }

    private LineDataSet generateLineDataSet(String label, int count, int highLightColor, int seed, int offset) {
        ArrayList<Entry> e1 = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            e1.add(new Entry(i, (int) (Math.random() * seed) + offset));
        }
        return generateLineDataSet(label, e1, highLightColor);
    }

    private LineDataSet generateLineDataSet(String label, ArrayList<Entry> e1, int highLightColor) {
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

    private ChartData<?> generateDataBar(HashMap<String, List<ApmBaseUnit<ApmSourceConnect>>> connectUnits) {
        ArrayList<BarEntry> entries = new ArrayList<>();

        ArrayList<String> keySet = new ArrayList<>(connectUnits.keySet());
        for (int i = 0; i < keySet.size(); i++) {
            entries.add(new BarEntry(i, connectUnits.get(keySet.get(i)).size()));
        }

        BarDataSet d = new BarDataSet(entries, "版本");
        d.setColors(ColorTemplate.VORDIPLOM_COLORS);
        d.setHighLightAlpha(255);
        BarData cd = new BarData(d);
        cd.setBarWidth(0.9f);
        return cd;
    }

    /**
     * generates a random ChartData object with just one DataSet
     *
     * @return
     * @param connectUnits
     */
    private PieData generateDataPie(HashMap<String, List<ApmBaseUnit<ApmSourceConnect>>> connectUnits) {

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        List<String> keySet = new ArrayList<>(connectUnits.keySet());
        for (int i = 0; i < keySet.size(); i++) {
            String key = keySet.get(i);
//            String label = null == key ? "" : key.substring(1, Math.min(16, key.length()));
            entries.add(new PieEntry((float) (connectUnits.get(key).size()), key/*label*/));
        }

        PieDataSet d = new PieDataSet(entries, "App,版本,包名");

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
