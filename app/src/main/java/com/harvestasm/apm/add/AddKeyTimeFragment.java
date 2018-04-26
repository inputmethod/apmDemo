package com.harvestasm.apm.add;

import android.annotation.SuppressLint;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.harvestasm.apm.sample.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import typany.apm.agent.android.harvest.ApplicationInformation;
import typany.apm.agent.android.measurement.CustomMetricMeasurement;

/**
 * A placeholder fragment containing a simple view.
 *
 */
public class AddKeyTimeFragment extends AddCharDataFragment {
    @BindView(R.id.title)
    TextView titleView;

    @BindView(R.id.chart)
    BarChart mChart;

    private final List<EditText> editTextList = new ArrayList<>();

    @Override
    protected void refreshWithChangedText() {
        checkMenuState();
        mChart.invalidate();
    }

    // 按键响应时间
    protected String getCategory() {
        return "Time";
    }
    protected String getName() {
        return "key_press";
    }


    @Override
    protected void checkMenuState() {
        refreshBarChart();

        if (hasEmptyValue(editTextList)) {
            enableNextMenu(false);
            return;
        }

        enableNextMenu(true);
    }

    @Override
    protected View initViewsForApp(LayoutInflater inflater, ApplicationInformation item,
                                 Map<ApplicationInformation, CustomMetricMeasurement> dataMap) {
        View v = inflater.inflate(R.layout.fragment_add_entry, null, false);
        setEntityTitle(v, R.id.entry_key, item);
        setEntityValue(v, R.id.entry_value, dataMap, item, editTextList, watcher);
        return v;
    }

    @Override
    protected void parseArguments() {
        // do nothing.
    }

    @Override
    protected int getFragmentLayoutResId() {
        return R.layout.fragment_vertical_linear_container;
    }


    @Override
    protected void initChart() {
//        mChart = (BarChart) findViewById(R.id.chart1);
        titleView.setText(getActivity().getTitle());
        mChart.setOnChartValueSelectedListener(this);

        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);

        mChart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        mChart.setDrawGridBackground(false);
        // mChart.setDrawYLabels(false);


        // todo: axis formatter
//        IAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(mChart);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        // todo: font type
//        xAxis.setTypeface(mTfLight);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
//        xAxis.setValueFormatter(xAxisFormatter);

        // todo: axis formatter
//        IAxisValueFormatter custom = new MyAxisValueFormatter();

        YAxis leftAxis = mChart.getAxisLeft();
//        leftAxis.setTypeface(mTfLight);
        leftAxis.setLabelCount(8, false);
//        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
//        rightAxis.setTypeface(mTfLight);
        rightAxis.setLabelCount(8, false);
//        rightAxis.setValueFormatter(custom);
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);
        // l.setExtra(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
        // "def", "ghj", "ikl", "mno" });
        // l.setCustom(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
        // "def", "ghj", "ikl", "mno" });

        // todo: set marker with axis formmater
//        XYMarkerView mv = new XYMarkerView(getContext(), xAxisFormatter);
//        mv.setChartView(mChart); // For bounds control
//        mChart.setMarker(mv); // Set the marker to the chart
    }

    @Override
    protected void performNextTask() {
        String option = parseOptionName();
        for (EditText editText : editTextList) {
            addDataItem(option, editText);
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onValueSelected(Entry e, Highlight h) {

        if (e == null)
            return;

        RectF bounds = mOnValueSelectedRectF;
        mChart.getBarBounds((BarEntry) e, bounds);
        MPPointF position = mChart.getPosition(e, YAxis.AxisDependency.LEFT);

        Log.i("bounds", bounds.toString());
        Log.i("position", position.toString());

        Log.i("x-index",
                "low: " + mChart.getLowestVisibleX() + ", high: "
                        + mChart.getHighestVisibleX());

        MPPointF.recycleInstance(position);
    }

    private void refreshBarChart() {
        int count = editTextList.size();

        ArrayList<BarEntry> yVals1 = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            String text = editTextList.get(i).getText().toString();
            float val = TextUtils.isEmpty(text) ? 0f : Float.parseFloat(text);

            if (val < 25) {
                yVals1.add(new BarEntry(i + 1f, val, getResources().getDrawable(R.drawable.ic_home_black_24dp)));
            } else {
                yVals1.add(new BarEntry(i + 1f, val));
            }
        }
        fillDataSet(yVals1);
    }

    private void fillDataSet(ArrayList<BarEntry> yVals1) {
        BarDataSet set1;
        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, getActivity().getTitle().toString());

            set1.setDrawIcons(false);

            set1.setColors(ColorTemplate.MATERIAL_COLORS);

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            // todo: set font
//            data.setValueTypeface(mTfLight);
            data.setBarWidth(0.9f);

            mChart.setData(data);
        }
    }
}
