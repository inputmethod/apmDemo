package com.harvestasm.apm.add;

import android.annotation.SuppressLint;
import android.graphics.RectF;
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
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.harvestasm.apm.sample.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import typany.apm.agent.android.harvest.ApplicationInformation;
import typany.apm.agent.android.measurement.CustomMetricMeasurement;

/**
 * A placeholder fragment containing a simple view.
 *
 */
public class AddKeyTimeFragment extends AddAbstractBarChartFragment {
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
        refreshChart();

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
        editTextList.add(initEntityValue(v, R.id.entry_value, dataMap, item, watcher));
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

    private void refreshChart() {
        int count = editTextList.size();

        ArrayList<BarEntry> yVals1 = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            float val = getFloatValue(editTextList.get(i));

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
