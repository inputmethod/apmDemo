package com.harvestasm.apm.add;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

/**
 * A placeholder fragment containing a simple view.
 */
public class AddSubFrameFragment extends AddKeyTimeFragment {
    /* type description.
      0 "frame_theme_slide"
      1 "frame_emoji_slide"
      2 "frame_switch_kb_symbol"
      3 "frame_switch_kb_emoji"
      4 "frame_switch_kb_setting"
      5 "frame_switch_kb_typing"
     */
    private int type; // 0 - 5,

    protected String getCategory() {
        return "frame";
    }

    // todo: 判断是息屏还是平均的电流数据
    @Override
    protected void inflateChildrenView(LayoutInflater inflater, View view) {
        super.inflateChildrenView(inflater, view);
        Bundle bundle = getArguments();
        if (null != bundle) {
            type = bundle.getInt("type");
        }
    }
}
