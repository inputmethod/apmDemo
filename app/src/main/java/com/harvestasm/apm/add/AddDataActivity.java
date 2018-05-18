package com.harvestasm.apm.add;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;

import com.harvestasm.apm.sample.R;
import com.harvestasm.chart.BaseChartActivity;

public class AddDataActivity extends BaseChartActivity {
    private static final String TAG = AddDataActivity.class.getSimpleName();

    @Override
    protected void onCreateComplete() {
        super.onCreateComplete();

        String category = getIntent().getStringExtra("category");
        String name = getIntent().getStringExtra("name");
        if (!TextUtils.isEmpty(name)) {
            setTitle(name);
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
                fragment = new AddKeyTimeFragment();
            } else if (id == R.id.electric_current_average) {
                fragment = new AddKeyTimeFragment();
            } else if (id == R.id.electric_current_offscreen) {
                fragment = new AddKeyTimeFragment();
            } else if (id == R.id.cpu_idle) {
                fragment = AddSubCpuFragment.newInstance(0);
            } else if (id == R.id.cpu_medium) {
                fragment = AddSubCpuFragment.newInstance(1);
            } else if (id == R.id.cpu_long) {
                fragment = AddSubCpuFragment.newInstance(2);
            } else if (id == R.id.frame_theme_slide) {
                fragment = new AddKeyTimeFragment();
            } else if (id == R.id.frame_emoji_slide) {
                fragment = new AddKeyTimeFragment();
            } else if (id == R.id.frame_switch_kb_symbol) {
                fragment = new AddKeyTimeFragment();
            } else if (id == R.id.frame_switch_kb_emoji) {
                fragment = new AddKeyTimeFragment();
            } else if (id == R.id.frame_switch_kb_setting) {
                fragment = new AddKeyTimeFragment();
            } else if (id == R.id.frame_kb_typing) {
                fragment = new AddKeyTimeFragment();
            } else {
                // unknown cases.
                Log.e(TAG, "onCreate skip with unknown id = " + id);
                return;
            }
        }

        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        bundle.putString("category", category);
        fragment.setArguments(bundle);

        replaceFragment(fragment, tag);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_scrolling;
    }

    public static void startByAction(Context context, int id, String name,  String category) {
        Intent intent = new Intent(context, AddDataActivity.class);
        intent.putExtra("actionId", id);
        intent.putExtra("name", name);
        intent.putExtra("category", category);
        context.startActivity(intent);
    }
}
