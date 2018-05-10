package com.harvestasm.apm.base;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.harvestasm.apm.sample.R;

import butterknife.BindView;
import butterknife.ButterKnife;

abstract public class BaseFragmentActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    protected Toolbar toolbar;

    private int containerId;
    private FragmentManager fragmentManager;

    protected Fragment getFragmentWithTag(String tag) {
        return fragmentManager.findFragmentByTag(tag);
    }

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentLayoutId());

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        this.containerId = R.id.container;
        this.fragmentManager = getSupportFragmentManager();

        onCreateComplete();
    }

    @CallSuper
    protected void onCreateComplete() {
    }

    protected abstract @LayoutRes int getContentLayoutId();

    // todo: 给你BottomNavigation一起使用时，back按键与正常行为（都直接退出？）addToBackStack
    protected void replaceFragment(Fragment fragment, String tag) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft = ft.replace(containerId, fragment, tag);
        ft.commitAllowingStateLoss();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
    }
}
