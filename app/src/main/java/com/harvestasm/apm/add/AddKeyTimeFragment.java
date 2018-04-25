package com.harvestasm.apm.add;

import android.annotation.SuppressLint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.harvestasm.apm.sample.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import typany.apm.agent.android.harvest.ApplicationInformation;
import typany.apm.agent.android.measurement.CustomMetricMeasurement;
import typany.apm.agent.android.measurement.producer.CustomMetricProducer;
import typany.apm.agent.android.metric.MetricUnit;

/**
 * A placeholder fragment containing a simple view.
 *
 */
public class AddKeyTimeFragment extends BaseAddFragment implements OnChartValueSelectedListener {
    @BindView(R.id.title)
    TextView titleView;

    @BindView(R.id.chart)
    BarChart mChart;

    private final List<EditText> editTextList = new ArrayList<>();

    public AddKeyTimeFragment() {
    }

    // 按键响应时间
    protected String getCategory() {
        return "Time";
    }
    protected String getName() {
        return "key_press";
    }

    protected MetricUnit getValueUnit() {
        return MetricUnit.MS;
    }

    protected MetricUnit getCountUnit() {
        return MetricUnit.OPERATIONS;
    }

    private final TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (TextUtils.isEmpty(s)) {
                enableNextMenu(false);
            } else {
                checkMenuState();
                mChart.invalidate();
            }
        }
    };

    private void checkMenuState() {
        refreshBarChart();
        for (EditText editText : editTextList) {
            Editable editable = editText.getText();
            if (TextUtils.isEmpty(editable)) {
                enableNextMenu(false);
                return;
            }
        }

        enableNextMenu(true);
    }

    @Override
    protected void inflateChildrenView(LayoutInflater inflater, View view) {
        initChart();
//        parseChartCache();

        Map<ApplicationInformation, CustomMetricMeasurement> dataMap = AddDataStorage.get()
                .getMeasurementByOption(parseOptionName());
        for (ApplicationInformation item : AddDataStorage.get().selectedImeAppList) {
            View v = inflater.inflate(R.layout.fragment_add_entry, null, false);
            TextView textView = v.findViewById(R.id.entry_key);
            textView.setText(item.getAppName() + "(" + getValueUnit() + ")");
            int inputType = InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL | InputType.TYPE_NUMBER_FLAG_DECIMAL;
            EditText editText = v.findViewById(R.id.entry_value);
            editText.setInputType(inputType);
            editText.setTag(item);
            peakEditText(editText, item, dataMap);
            editTextList.add(editText);
            ((ViewGroup)view).addView(v);
            editText.addTextChangedListener(watcher);
        }

        setHasOptionsMenu(true);
        view.post(new Runnable() {
            @Override
            public void run() {
                checkMenuState();
            }
        });
    }

    private void peakEditText(@NonNull EditText editText, @NonNull ApplicationInformation information,
                              @Nullable Map<ApplicationInformation, CustomMetricMeasurement> dataMap) {
        if (null == dataMap) {
            return;
        }

        CustomMetricMeasurement metricMeasurement = dataMap.get(information);
        if (null != editText && null != metricMeasurement) {
            float val = (float) metricMeasurement.getCustomMetric().getMax();
            editText.setText(String.valueOf(val));
        }
    }

    @Override
    protected int getFragmentLayoutResId() {
        return R.layout.fragment_vertical_linear_container;
    }

    private void initChart() {
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

    protected CustomMetricMeasurement newMetricMeasurement(double value) {
        CustomMetricMeasurement metric = CustomMetricProducer.makeMeasurement(getName(),
                getCategory(), 1, value, 0, getCountUnit(), getValueUnit());
        metric.setScope("manual");
        return metric;
    }

    @Override
    protected boolean nextStep() {
        try {
//            DeviceInformation deviceInformation = Agent.getDeviceInformation();
            // one harvest data per application
            String option = parseOptionName();
            for (EditText editText : editTextList) {
                ApplicationInformation item = (ApplicationInformation) editText.getTag();
//                HarvestData harvestData = new HarvestData(item, deviceInformation);
                double value = Double.parseDouble(editText.getText().toString());
                CustomMetricMeasurement measurement = newMetricMeasurement(value);
//                harvestData.getMetrics().addMetric(measurement.getCustomMetric());
//                AddDataStorage.get().testData(harvestData);
                AddDataStorage.get().addCache(option, item, measurement);
            }
            return true;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    protected RectF mOnValueSelectedRectF = new RectF();

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

    @Override
    public void onNothingSelected() { }

//    private void parseChartCache() {
//        Map<ApplicationInformation, CustomMetricMeasurement> dataMap = AddDataStorage.get()
//                .getMeasurementByOption(parseOptionName());
//        if (null != dataMap) {
//            ArrayList<BarEntry> yVals1 = new ArrayList<>();
//            float index = 1f;
//            for (EditText editText : editTextList) {
//                ApplicationInformation item = (ApplicationInformation) editText.getTag();
//                CustomMetricMeasurement metricMeasurement = dataMap.get(item);
//                if (null == metricMeasurement) {
//                } else {
//                    float val = (float) metricMeasurement.getCustomMetric().getMax();
//                    yVals1.add(new BarEntry(index, val));
//                    editText.setText(String.valueOf(val));
//                }
//                index += 1f;
//            }
//            fillDataSet(yVals1);
//            mChart.invalidate();
//        }
//    }

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

    protected final String parseOptionName() {
        return getCategory() + "/" + getName();
    }
}
