package com.harvestasm.apm.add;

import android.view.LayoutInflater;
import android.view.View;

import com.harvestasm.apm.sample.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class AddFrameFragment extends AddElectricCurrentFragment {
    @Override
    protected void inflateChildrenView(LayoutInflater inflater, View view) {
        addOptionView(inflater, R.id.frame_theme_slide, R.string.frame_theme_slide);
        addOptionView(inflater, R.id.frame_emoji_slide, R.string.frame_emoji_slide);
        addOptionView(inflater, R.id.frame_switch_kb_symbol, R.string.frame_switch_kb_symbol);
        addOptionView(inflater, R.id.frame_switch_kb_emoji, R.string.frame_switch_kb_emoji);
        addOptionView(inflater, R.id.frame_switch_kb_setting, R.string.frame_switch_kb_setting);
        addOptionView(inflater, R.id.frame_kb_typing, R.string.frame_switch_kb_typing);
    }

}
