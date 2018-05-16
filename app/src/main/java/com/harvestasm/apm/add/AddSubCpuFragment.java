package com.harvestasm.apm.add;

import android.annotation.SuppressLint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.harvestasm.apm.sample.R;
import com.harvestasm.chart.ChartItemHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import typany.apm.agent.android.harvest.ApplicationInformation;
import typany.apm.agent.android.measurement.CustomMetricMeasurement;

/**
 * A placeholder fragment containing a simple view.
 */
public class AddSubCpuFragment extends AddAbstractBarChartFragment {
    private final List<List<EditText>> editTextListGroup = new ArrayList<>();

    private int type; // 0 - cpu idle, 1 - cpu medium, 2 - cpu long

    @Override
    protected void refreshWithChangedText() {
        checkMenuState();
        mChart.invalidate();
    }

    // 按键响应时间
    protected String getCategory() {
        return "cpu";
    }

    protected String getName() {
        if (0 == type) {
            return "idle";
        } else if (1 == type) {
            return "medium";
        } else if (2 == type) {
            return "long";
        } else {
            return "unknown";
        }
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
    protected void parseArguments() {
        Bundle bundle = getArguments();
        if (null != bundle) {
            type = bundle.getInt("type");
        }
    }

    @Override
    protected View initViewsForApp(LayoutInflater inflater, ApplicationInformation item,
                                   Map<ApplicationInformation, CustomMetricMeasurement> dataMap) {
        View v = inflater.inflate(R.layout.fragment_add_two_entry, null, false);

        setEntityTitle(v, R.id.entry_key, item);
        List<EditText> rowEditTexts = new ArrayList<>();
        rowEditTexts.add(initEntityValue(v, R.id.entry_value, dataMap, item, watcher));
        rowEditTexts.add(initEntityValue(v, R.id.entry_other_value, dataMap, item, watcher));
        editTextListGroup.add(rowEditTexts);
        return v;
    }

    @Override
    protected int getFragmentLayoutResId() {
        return R.layout.fragment_vertical_linear_container;
    }

    protected void refreshChart() {
        int xProgress = editTextListGroup.size();

        List<BarEntry> yVals = new ArrayList<BarEntry>();
        for (int i = 0; i < xProgress; i++) {
            float val1 = getFloatValue(editTextListGroup.get(i).get(0));
            float val2 = getFloatValue(editTextListGroup.get(i).get(1));

            ChartItemHelper.buildEntry(yVals, val2, val1, 1, i);
        }

        BarDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
//            set1 = new BarDataSet(yVals, parseOptionName());
//            set1.setDrawIcons(false);
//            set1.setColors(ChartItemHelper.getColors());
//            set1.setStackLabels(new String[]{"平均", "最大"});
//
//            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
//            dataSets.add(set1);
//
//            BarData data = new BarData(dataSets);
//            data.setValueFormatter(new MyValueFormatter());
//            data.setValueTextColor(Color.WHITE);

            BarData data = ChartItemHelper.generateStackedBarData(yVals, parseOptionName());
            mChart.setData(data);
        }

        mChart.setFitBars(true);
        mChart.invalidate();
    }

    @Override
    protected void performNextTask() {
        String option = parseOptionName();

        for (int i = 0; i < editTextListGroup.size(); i++) {
            EditText editText = editTextListGroup.get(i).get(0);
            EditText otherEditText = editTextListGroup.get(i).get(1);
            addDataItem(option, editText, otherEditText);
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

    public static Fragment newInstance(int type) {
        Fragment fragment = new AddSubCpuFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }
}
