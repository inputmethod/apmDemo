package com.harvestasm.apm.add;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;

import com.harvestasm.apm.sample.R;
import com.harvestasm.chart.BaseChartActivity;

public class AddSubOptionActivity extends BaseChartActivity {
    private static final String TAG = AddSubOptionActivity.class.getSimpleName();

    @Override
    protected void onCreateComplete() {
        super.onCreateComplete();

        String title = getIntent().getStringExtra("title");
        if (!TextUtils.isEmpty(title)) {
            setTitle(title);
        }

        int id = getIntent().getIntExtra("actionId", 0);
        final String tag = "action" + id;
        Fragment fragment = getFragmentWithTag(tag);
        if (null == fragment) {
            if (id == R.id.action_add_memory) {
                fragment = new AddMemoryFragment();
            } else if (id == R.id.action_add_key_time) {
                fragment = new AddKeyTimeFragment();
            } else if (id == R.id.action_add_keyboard_hide) {
                fragment = new AddKbHideFragment();
            } else if (id == R.id.electric_current_average) {
                fragment = AddSubElectricCurrentFragment.newInstance(false);
            } else if (id == R.id.electric_current_offscreen) {
                fragment = AddSubElectricCurrentFragment.newInstance(true);
            } else if (id == R.id.cpu_idle) {
                fragment = AddSubCpuFragment.newInstance(0);
            } else if (id == R.id.cpu_medium) {
                fragment = AddSubCpuFragment.newInstance(1);
            } else if (id == R.id.cpu_long) {
                fragment = AddSubCpuFragment.newInstance(2);
            } else if (id == R.id.frame_theme_slide) {
                fragment = AddSubFrameFragment.newInstance(0);
            } else if (id == R.id.frame_emoji_slide) {
                fragment = AddSubFrameFragment.newInstance(1);
            } else if (id == R.id.frame_switch_kb_symbol) {
                fragment = AddSubFrameFragment.newInstance(2);
            } else if (id == R.id.frame_switch_kb_emoji) {
                fragment = AddSubFrameFragment.newInstance(3);
            } else if (id == R.id.frame_switch_kb_setting) {
                fragment = AddSubFrameFragment.newInstance(4);
            } else if (id == R.id.frame_kb_typing) {
                fragment = AddSubFrameFragment.newInstance(5);
            } else {
                // unknown cases.
                Log.e(TAG, "onCreate skip with unknown id = " + id);
                return;
            }
        }

        replaceFragment(fragment, tag);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_scrolling;
    }

    public static void startByAction(Context context, int id, int textId) {
        startByAction(context, id, context.getString(textId));
    }

    public static void startByAction(Context context, int id, String title) {
        Intent intent = new Intent(context, AddSubOptionActivity.class);
        intent.putExtra("actionId", id);
        intent.putExtra("title", title);
        context.startActivity(intent);
    }
}
