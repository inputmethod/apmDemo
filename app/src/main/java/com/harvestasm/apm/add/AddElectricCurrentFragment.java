package com.harvestasm.apm.add;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.harvestasm.apm.sample.R;

import butterknife.BindView;

/**
 * A placeholder fragment containing a simple view.
 */
public class AddElectricCurrentFragment extends BaseAddFragment {
    private static final String TAG = AddElectricCurrentFragment.class.getSimpleName();

    @BindView(R.id.chart)
    LineChart mChart;

    @BindView(R.id.options_layout)
    LinearLayout optionsLayout;

    public AddElectricCurrentFragment() {
    }

    private final View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            clickOptionView(v.getId(), (int)v.getTag());
        }
    };

    private void clickOptionView(int viewId, int textId) {
        AddSubOptionActivity.startByAction(getContext(), viewId, textId);
    }

    @Override
    protected void inflateChildrenView(LayoutInflater inflater, View view) {
        addOptionView(inflater, R.id.electric_current_average, R.string.electric_current_average);
        addOptionView(inflater, R.id.electric_current_offscreen, R.string.electric_current_offscreen);
    }

    protected void addOptionView(LayoutInflater inflater, int layoutId, int textId) {
        TextView view = (TextView) inflater.inflate(R.layout.including_add_option_item, null, false);
        view.setId(layoutId);
        view.setText(textId);
        view.setTag(textId);
        view.setOnClickListener(listener);
        optionsLayout.addView(view);

    }

    @Override
    protected int getFragmentLayoutResId() {
        return R.layout.fragment_add_electric_current;
    }


    @Override
    protected boolean nextStep() {
        return false;
    }
}
