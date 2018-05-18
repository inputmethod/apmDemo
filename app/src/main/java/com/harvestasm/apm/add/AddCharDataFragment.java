package com.harvestasm.apm.add;

import android.annotation.SuppressLint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.List;
import java.util.Map;

import typany.apm.agent.android.harvest.ApplicationInformation;
import typany.apm.agent.android.measurement.CustomMetricMeasurement;
import typany.apm.agent.android.measurement.producer.CustomMetricProducer;
import typany.apm.agent.android.metric.MetricUnit;

/**
 * A placeholder fragment containing a simple view.
 *
 */
abstract public class AddCharDataFragment extends BaseAddFragment implements OnChartValueSelectedListener {
    protected MetricUnit getValueUnit() {
        if (TextUtils.equals(category, "CPU负载")) {
            return MetricUnit.PERCENT;
        } else if (TextUtils.equals(category, "流畅度(帧率)")) {
            return MetricUnit.FPS;
        } else if (TextUtils.equals(category, "电流功耗")) {
            return MetricUnit.MA;
        } else {
            return MetricUnit.MS;
        }
    }
    protected MetricUnit getCountUnit() {
        return MetricUnit.OPERATIONS;
    }

    private String name;
    private String category;

    protected final TextWatcher watcher = new TextWatcher() {
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
                refreshWithChangedText();
            }
        }
    };

    @Override
    protected final void inflateChildrenView(LayoutInflater inflater, View view) {
        parseArguments();
        initChart();

        Map<ApplicationInformation, CustomMetricMeasurement> dataMap = AddDataStorage.get()
                .getMeasurementByOption(parseOptionName());
        for (ApplicationInformation item : AddDataStorage.get().selectedImeAppList) {
            View v = initViewsForApp(inflater, item, dataMap);
            ((ViewGroup)view).addView(v);
        }

        setHasOptionsMenu(true);
        view.post(new Runnable() {
            @Override
            public void run() {
                checkMenuState();
            }
        });
    }

    protected abstract void checkMenuState();
    protected abstract View initViewsForApp(LayoutInflater inflater, ApplicationInformation item,
                                            Map<ApplicationInformation, CustomMetricMeasurement> dataMap);

    protected final void parseArguments() {
        Bundle bundle = getArguments();
        name = bundle.getString("name");
        category = bundle.getString("category");
    }

    protected abstract void initChart();

    protected abstract void refreshWithChangedText();

    protected void addDataItem(String option, EditText editText) {
        addDataItem(option, editText, null);
    }

    protected void addDataItem(String option, EditText editText, EditText otherEditText) {
        ApplicationInformation item = (ApplicationInformation) editText.getTag();
        double value = Double.parseDouble(editText.getText().toString());
        CustomMetricMeasurement measurement = CustomMetricProducer.makeMeasurement(getName(),
                getCategory(), 1, value, 0, getCountUnit(), getValueUnit());

        measurement.setScope("manual");

        if (null != otherEditText) {
            double otherValue = Double.parseDouble(otherEditText.getText().toString());
            measurement.getCustomMetric().setMax(otherValue);
        }

        AddDataStorage.get().addCache(option, item, measurement);
    }

    protected final String getCategory() {
        return category;
    }

    protected final String getName() {
        return name;
    }

    protected RectF mOnValueSelectedRectF = new RectF();

    @SuppressLint("NewApi")
    @Override
    public void onValueSelected(Entry e, Highlight h) {
    }

    @Override
    public void onNothingSelected() { }

    protected final String parseOptionName() {
        return getCategory() + "/" + getName();
    }


    protected EditText initEntityValue(View v, int valueId, Map<ApplicationInformation, CustomMetricMeasurement> dataMap,
                                       ApplicationInformation item, TextWatcher watcher) {
        int inputType = InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL | InputType.TYPE_NUMBER_FLAG_DECIMAL;
        EditText editText = v.findViewById(valueId);
        editText.setInputType(inputType);
        editText.setTag(item);
        peakEditText(editText, item, dataMap);
        editText.addTextChangedListener(watcher);
        return editText;
    }

    private static void peakEditText(@NonNull EditText editText, @NonNull ApplicationInformation information,
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

    protected void setEntityTitle(View v, int titleId, ApplicationInformation item) {
        TextView textView = v.findViewById(titleId);
        textView.setText(item.getAppName() + "(" + getValueUnit() + ")");
    }

    protected static boolean hasEmptyValue(@NonNull List<EditText> editTextList) {
        for (EditText editText : editTextList) {
            Editable editable = editText.getText();
            if (TextUtils.isEmpty(editable)) {
                return true;
            }
        }

        return false;
    }

    protected static float getFloatValue(EditText editText) {
        String text = editText.getText().toString();
        return TextUtils.isEmpty(text) ? 0f : Float.parseFloat(text);
    }
}
