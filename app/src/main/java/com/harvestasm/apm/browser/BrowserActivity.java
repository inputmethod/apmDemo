package com.harvestasm.apm.browser;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.harvestasm.apm.filter.FilterFragment;
import com.harvestasm.apm.sample.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BrowserActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private int containerId;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        this.containerId = R.id.container;
        this.fragmentManager = getSupportFragmentManager();

        DataStorage.get().currentState.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean filter) {
                navigateToHome(filter);
            }
        });

        navigateToHome(false);
    }

    private void navigateToHome(boolean filter) {
        String tag = filter ? FilterFragment.class.getSimpleName() : BrowserFragment.class.getSimpleName();
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (null == fragment) {
            fragment = newFragment(filter);
        }
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(containerId, fragment, tag);
        ft.commitAllowingStateLoss();
    }

    private Fragment newFragment(boolean filter) {
        if (filter) {
            return new FilterFragment();
        } else {
            return new BrowserFragment();
        }
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, BrowserActivity.class);
        context.startActivity(intent);
    }
}
