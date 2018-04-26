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
 * 带2级子类的电流，帧率和CPU数据页面
 */
abstract public class AddCategoryFragment extends BaseAddFragment {
    private static final String TAG = AddCategoryFragment.class.getSimpleName();

    @BindView(R.id.chart)
    LineChart mChart;

    @BindView(R.id.options_layout)
    LinearLayout optionsLayout;

    private final View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            clickOptionView(v.getId(), (int)v.getTag());
        }
    };

    private void clickOptionView(int viewId, int textId) {
        AddSubOptionActivity.startByAction(getContext(), viewId, textId);
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
