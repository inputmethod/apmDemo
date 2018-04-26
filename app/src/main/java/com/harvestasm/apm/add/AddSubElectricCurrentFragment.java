package com.harvestasm.apm.add;

import android.os.Bundle;

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
        return offscreen ? "offscreen" : "arverage";
    }

    @Override
    protected void parseArguments() {
        Bundle bundle = getArguments();
        if (null != bundle) {
            offscreen = bundle.getBoolean("offscreen");
        }
    }
}
