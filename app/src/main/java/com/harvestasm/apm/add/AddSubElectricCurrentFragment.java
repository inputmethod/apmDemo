package com.harvestasm.apm.add;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

/**
 * A placeholder fragment containing a simple view.
 */
public class AddSubElectricCurrentFragment extends AddKeyTimeFragment {
    private boolean offscreen;

    // 按键响应时间
    protected String getCategory() {
        return "electric_current";
    }
    protected String getName() {
        return offscreen ? "offscree" : "arverage";
    }

    @Override
    protected void inflateChildrenView(LayoutInflater inflater, View view) {
        super.inflateChildrenView(inflater, view);
        Bundle bundle = getArguments();
        if (null != bundle) {
            offscreen = bundle.getBoolean("offscreen");
        }
    }
}
