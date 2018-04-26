package com.harvestasm.apm.add;

import android.view.LayoutInflater;

import com.harvestasm.apm.sample.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class AddCpuFragment  extends AddCategoryFragment {
    @Override
    protected void initCategoryItem(LayoutInflater inflater) {
        addOptionView(inflater, R.id.cpu_idle, R.string.cpu_idle);
        addOptionView(inflater, R.id.cpu_medium, R.string.cpu_medium);
        addOptionView(inflater, R.id.cpu_long, R.string.cpu_long);
    }

}
