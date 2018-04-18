package com.harvestasm.apm.add;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;

import com.harvestasm.apm.sample.R;

public class ScrollingActivity extends AppCompatActivity implements ItemListDialogFragment.Listener {

    private int containerId;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.containerId = R.id.container;
        this.fragmentManager = getSupportFragmentManager();

        String title = getIntent().getStringExtra("title");
        if (!TextUtils.isEmpty(title)) {
            toolbar.setTitle(title);
        }

        int id = getIntent().getIntExtra("actionId", 0);
        final String tag = "action" + id;
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (null == fragment) {
            if (id == R.id.action_add_memory) {
                fragment = ItemListDialogFragment.newInstance();
            } else if (id == R.id.action_add_key_time) {
                fragment = ItemListDialogFragment.newInstance();
            } else if (id == R.id.action_add_keyboard_hide) {
                fragment = ItemListDialogFragment.newInstance();
            } else if (id == R.id.action_add_electric_current) {
                fragment = ItemListDialogFragment.newInstance();
            } else if (id == R.id.action_add_frame) {
                fragment = ItemListDialogFragment.newInstance();
            } else if (id == R.id.action_add_cpu) {
                fragment = ItemListDialogFragment.newInstance();
            } else {
                // unknown cases.
                return;
            }
        }

        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft = ft.replace(containerId, fragment, tag);
        ft.commitAllowingStateLoss();
    }

    @Override
    public void onItemClicked(int position) {
        // todo: response to item click
    }
}
