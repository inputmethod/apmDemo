package com.harvestasm.apm.add;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;

import com.harvestasm.apm.sample.R;
import com.harvestasm.chart.BaseChartActivity;

public class AddSubOptionActivity extends BaseChartActivity {
    private static final String TAG = AddSubOptionActivity.class.getSimpleName();

    private int containerId;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        this.containerId = R.id.container;
        this.fragmentManager = getSupportFragmentManager();

        String title = getIntent().getStringExtra("title");
        if (!TextUtils.isEmpty(title)) {
            setTitle(title);
        }

        int id = getIntent().getIntExtra("actionId", 0);
        final String tag = "action" + id;
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (null == fragment) {
            if (id == R.id.electric_current_average) {
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

        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft = ft.replace(containerId, fragment, tag);
        ft.commitAllowingStateLoss();
    }

    public static void startByAction(Context context, int id, int textId) {
        Intent intent = new Intent(context, AddSubOptionActivity.class);
        intent.putExtra("actionId", id);
        intent.putExtra("title", context.getString(textId));
        context.startActivity(intent);
    }

}
