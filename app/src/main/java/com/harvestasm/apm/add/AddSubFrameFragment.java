package com.harvestasm.apm.add;

import android.os.Bundle;
import android.support.v4.app.Fragment;

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
      5 "frame_kb_typing"
     */
    private int type; // 0 - 5,

    protected String getCategory() {
        return "frame";
    }

    protected String getName() {
        if (0 == type) {
            return "theme_slide";
        } else if (1 == type) {
            return "emoji_slide";
        } else if (2 == type) {
            return "switch_kb_symbol";
        } else if (3 == type) {
            return "switch_kb_emoji";
        } else if (4 == type) {
            return "switch_kb_setting";
        } else if (5 == type) {
            return "kb_typing";
        } else {
            return "unknown";
        }
    }

    @Override
    protected void parseArguments() {
        Bundle bundle = getArguments();
        if (null != bundle) {
            type = bundle.getInt("type");
        }
    }

    public static Fragment newInstance(int type) {
        Fragment fragment = new AddSubFrameFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }
}
