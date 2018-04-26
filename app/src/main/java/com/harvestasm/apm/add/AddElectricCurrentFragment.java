package com.harvestasm.apm.add;

import android.view.LayoutInflater;

import com.harvestasm.apm.sample.R;

public class AddElectricCurrentFragment extends AddCategoryFragment {
    @Override
    protected void initCategoryItem(LayoutInflater inflater) {
        addOptionView(inflater, R.id.electric_current_average, R.string.electric_current_average);
        addOptionView(inflater, R.id.electric_current_offscreen, R.string.electric_current_offscreen);
    }
}
