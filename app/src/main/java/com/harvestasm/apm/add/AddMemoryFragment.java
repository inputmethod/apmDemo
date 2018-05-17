package com.harvestasm.apm.add;

import android.graphics.Color;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.harvestasm.apm.sample.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import typany.apm.agent.android.harvest.ApplicationInformation;
import typany.apm.agent.android.measurement.CustomMetricMeasurement;
import typany.apm.agent.android.tracing.Sample;

/**
 * A placeholder fragment containing a simple view.
 */
public class AddMemoryFragment extends AddCharDataFragment {
    @BindView(R.id.title)
    TextView titleView;

    @BindView(R.id.chart)
    LineChart mChart;

    private final List<List<EditText>> editTextListGroup = new ArrayList<>();

    protected String getCategory() {
        return "Memory";
    }

    protected String getName() {
        return "Used";
    }

    @Override
    protected int getFragmentLayoutResId() {
        return R.layout.fragment_add_memory;
    }

    @Override
    protected void initChart() {
        titleView.setText(getActivity().getTitle());
        mChart.setOnChartValueSelectedListener(this);

        // no description text
        mChart.getDescription().setEnabled(false);

        // enable touch gestures
        mChart.setTouchEnabled(true);

        mChart.setDragDecelerationFrictionCoef(0.9f);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);
        mChart.setHighlightPerDragEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(true);

        // set an alternative background color
        mChart.setBackgroundColor(Color.LTGRAY);

        // add data
//        setData(20, 30);

        mChart.animateX(2500);

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
//        l.setTypeface(mTfLight);
        l.setTextSize(11f);
        l.setTextColor(Color.WHITE);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
//        l.setYOffset(11f);

        XAxis xAxis = mChart.getXAxis();
//        xAxis.setTypeface(mTfLight);
        xAxis.setTextSize(11f);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);

        YAxis leftAxis = mChart.getAxisLeft();
//        leftAxis.setTypeface(mTfLight);
        leftAxis.setTextColor(ColorTemplate.getHoloBlue());
        leftAxis.setAxisMaximum(200f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularityEnabled(true);

        YAxis rightAxis = mChart.getAxisRight();
//        rightAxis.setTypeface(mTfLight);
        rightAxis.setTextColor(Color.RED);
        rightAxis.setAxisMaximum(900);
        rightAxis.setAxisMinimum(-200);
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawZeroLine(false);
        rightAxis.setGranularityEnabled(false);
    }

    @Override
    protected void refreshWithChangedText() {
        checkMenuState();
        mChart.invalidate();
    }

    @Override
    protected void checkMenuState() {
        refreshChart();

        for (List<EditText> editTextList : editTextListGroup) {
            if (hasEmptyValue(editTextList)) {
                enableNextMenu(false);
                return;
            }
        }

        enableNextMenu(true);
    }

    @Override
    protected View initViewsForApp(LayoutInflater inflater, ApplicationInformation item,
                                   Map<ApplicationInformation, CustomMetricMeasurement> dataMap) {
        View v = inflater.inflate(R.layout.fragment_add_list_entry, null, false);

        setEntityTitle(v, R.id.entry_key, item);
        List<EditText> rowEditTexts = new ArrayList<>();
        setEntityValueGroup(rowEditTexts, v, R.id.topRow, dataMap, item, watcher);
//        setEntityValueGroup(rowEditTexts, v, R.id.middleRow, dataMap, item, watcher);
//        setEntityValueGroup(rowEditTexts, v, R.id.bottomRow, dataMap, item, watcher);
        editTextListGroup.add(rowEditTexts);
        return v;
    }

    private void setEntityValueGroup(List<EditText> rowEditTexts, View v, int rowId, Map<ApplicationInformation, CustomMetricMeasurement> dataMap, ApplicationInformation item, TextWatcher watcher) {
        View rowView = v.findViewById(rowId);
        rowEditTexts.add(initEntityValue(rowView, R.id.entry_one, dataMap, item, watcher));
        rowEditTexts.add(initEntityValue(rowView, R.id.entry_two, dataMap, item, watcher));
        rowEditTexts.add(initEntityValue(rowView, R.id.entry_three, dataMap, item, watcher));
        rowEditTexts.add(initEntityValue(rowView, R.id.entry_four, dataMap, item, watcher));
        rowEditTexts.add(initEntityValue(rowView, R.id.entry_five, dataMap, item, watcher));
        rowEditTexts.add(initEntityValue(rowView, R.id.entry_six, dataMap, item, watcher));
    }

    @Override
    protected void parseArguments() {
        // todo: nothing???
    }

    private List<Entry> generateEntryList(int count, float mult, int seed) {
        ArrayList<Entry> yVals = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            float val = (float) (Math.random() * mult) + seed;
            yVals.add(new Entry(i, val));
        }
        return yVals;
    }

    private List<List<Entry>> generateSampleData() {
        int count = editTextListGroup.get(0).size();
        float range = 10;
        List<List<Entry>> entryListGroup = new ArrayList<>();
        for (int i = 0; i < editTextListGroup.size(); i += 3) {
            entryListGroup.add(generateEntryList(count, range / 2f, 50 + 10 * i));
            entryListGroup.add(generateEntryList(count, range, 100 + 10 * i));
            entryListGroup.add(generateEntryList(count, range, 200 + 10 * i));
        }
        return entryListGroup;
    }
    private void refreshChart() {
//        List<List<Entry>> entryListGroup = generateSampleData();

        List<List<Entry>> entryListGroup = new ArrayList<>();
        for (List<EditText> editTextList : editTextListGroup) {
            List<Entry> yVals = new ArrayList<>();
            int index = 0;
            for (EditText editText : editTextList) {
                yVals.add(new Entry(index++, getFloatValue(editText)));
            }
            entryListGroup.add(yVals);
        }
//        LineDataSet set1, set2, set3;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() == entryListGroup.size()) {
            for (int i = 0; i < entryListGroup.size(); i++) {
                LineDataSet set = (LineDataSet) mChart.getData().getDataSetByIndex(i);
                List<Entry> yVals = entryListGroup.get(i);
                set.setValues(yVals);

            }

            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
//            set1 = new LineDataSet(yVals1, "DataSet 1");
//
//            set1.setAxisDependency(YAxis.AxisDependency.LEFT);
//            set1.setColor(ColorTemplate.getHoloBlue());
//            set1.setCircleColor(Color.WHITE);
//            set1.setLineWidth(2f);
//            set1.setCircleRadius(3f);
//            set1.setFillAlpha(65);
//            set1.setFillColor(ColorTemplate.getHoloBlue());
//            set1.setHighLightColor(Color.rgb(244, 117, 117));
//            set1.setDrawCircleHole(false);
//            //set1.setFillFormatter(new MyFillFormatter(0f));
//            //set1.setDrawHorizontalHighlightIndicator(false);
//            //set1.setVisible(false);
//            //set1.setCircleHoleColor(Color.WHITE);
//
//            // create a dataset and give it a type
//            set2 = new LineDataSet(yVals2, "DataSet 2");
//            set2.setAxisDependency(YAxis.AxisDependency.RIGHT);
//            set2.setColor(Color.RED);
//            set2.setCircleColor(Color.WHITE);
//            set2.setLineWidth(2f);
//            set2.setCircleRadius(3f);
//            set2.setFillAlpha(65);
//            set2.setFillColor(Color.RED);
//            set2.setDrawCircleHole(false);
//            set2.setHighLightColor(Color.rgb(244, 117, 117));
//            //set2.setFillFormatter(new MyFillFormatter(900f));
//
//            set3 = new LineDataSet(yVals3, "DataSet 3");
//            set3.setAxisDependency(YAxis.AxisDependency.RIGHT);
//            set3.setColor(Color.YELLOW);
//            set3.setCircleColor(Color.WHITE);
//            set3.setLineWidth(2f);
//            set3.setCircleRadius(3f);
//            set3.setFillAlpha(65);
//            set3.setFillColor(ColorTemplate.colorWithAlpha(Color.YELLOW, 200));
//            set3.setDrawCircleHole(false);
//            set3.setHighLightColor(Color.rgb(244, 117, 117));
//
//            // create a data object with the datasets
//            LineData data = new LineData(set1, set2, set3);

            List<ILineDataSet> setList = new ArrayList<>();
            for (int i = 0; i < editTextListGroup.size(); i++) {
                LineDataSet set = new LineDataSet(entryListGroup.get(i), "Data Set " + i);
                YAxis.AxisDependency axisDependency = 0 == i ? YAxis.AxisDependency.LEFT : YAxis.AxisDependency.RIGHT;
                final int color;
                if (i % 3 == 0) {
                    color = ColorTemplate.getHoloBlue();
                } else if (i % 3 == 1) {
                    color = Color.RED;
                } else {
                    color = Color.YELLOW;
                }
                final int fillColor = i % 3 == 2 ? ColorTemplate.colorWithAlpha(Color.YELLOW, 200) : color;

                set.setAxisDependency(axisDependency);
                set.setColor(color);
                set.setCircleColor(Color.WHITE);
                set.setLineWidth(2f);
                set.setCircleRadius(3f);
                set.setFillAlpha(65);
                set.setFillColor(fillColor);
                set.setDrawCircleHole(false);
                set.setHighLightColor(Color.rgb(244, 117, 117));
                setList.add(set);
            }
            LineData data = new LineData(setList);
            data.setValueTextColor(Color.WHITE);
            data.setValueTextSize(9f);

            // set data
            mChart.setData(data);
        }
        mChart.invalidate();
    }

    @Override
    protected void performNextTask() {
        if (editTextListGroup.isEmpty()) {
            return;
        }

        String option = parseOptionName();
//        Trace rootTrace = new Trace();
//        rootTrace.displayName = TraceMachine.formatActivityDisplayName(option);
//        rootTrace.metricName = TraceMachine.formatActivityMetricName(rootTrace.displayName);
//        rootTrace.metricBackgroundName = TraceMachine.formatActivityBackgroundMetricName(rootTrace.displayName);
//        rootTrace.entryTimestamp = System.currentTimeMillis();

        for (List<EditText> editTextList : editTextListGroup) {
            ApplicationInformation item = (ApplicationInformation) editTextList.get(0).getTag();
            if (null == item) {
                continue;
            }

            EnumMap<Sample.SampleType, Collection<Sample>> samples = new EnumMap(Sample.SampleType.class);
            List<Sample> memorySamples = new ArrayList<>();
            for (EditText editText : editTextList) {
                Sample sample = new Sample(Sample.SampleType.MEMORY);
                sample.setSampleValue(getFloatValue(editText));
                memorySamples.add(sample);
            }
            samples.put(Sample.SampleType.MEMORY, memorySamples);

//            List<Sample> cpuSamples = new ArrayList<>();for (int i = 0; i < 10; i++) {
//                Sample sample = new Sample(Sample.SampleType.CPU);
//                sample.setSampleValue(Math.random() * 128 * i);
//                cpuSamples.add(sample);
//            }
//            samples.put(Sample.SampleType.CPU, cpuSamples);
            AddDataStorage.get().addCache(option, item, samples);
        }
    }
}
