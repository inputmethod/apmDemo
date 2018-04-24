package com.harvestasm.apm.add;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

/**
 * A placeholder fragment containing a simple view.
 */
public class AddSubElectricCurrentFragment extends AddKeyTimeFragment {
    private boolean offscreen;

    protected String getCategory() {
        return "electriccurrent";
    }

    // todo: 判断是息屏还是平均的电流数据
    @Override
    protected void inflateChildrenView(LayoutInflater inflater, View view) {
        super.inflateChildrenView(inflater, view);
        Bundle bundle = getArguments();
        if (null != bundle) {
            offscreen = bundle.getBoolean("offscreen");
        }
    }
}
