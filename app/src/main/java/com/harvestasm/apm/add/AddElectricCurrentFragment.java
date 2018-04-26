package com.harvestasm.apm.add;

import android.view.LayoutInflater;
import android.view.View;

import com.harvestasm.apm.sample.R;

public class AddElectricCurrentFragment extends AddCategoryFragment {
    @Override
    protected void inflateChildrenView(LayoutInflater inflater, View view) {
        addOptionView(inflater, R.id.electric_current_average, R.string.electric_current_average);
        addOptionView(inflater, R.id.electric_current_offscreen, R.string.electric_current_offscreen);
    }

}
