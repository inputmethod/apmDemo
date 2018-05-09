package com.harvestasm.apm.main;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.harvestasm.apm.add.ScrollingActivity;
import com.harvestasm.apm.sample.R;
import com.harvestasm.chart.BaseChartActivity;

public class SetupActivity extends BaseChartActivity {
    private int containerId;
    private FragmentManager fragmentManager;
    private SetupActivityViewModel setupActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        this.containerId = R.id.container;
        this.fragmentManager = getSupportFragmentManager();
        this.setupActivityViewModel = ViewModelProviders.of(this).get(SetupActivityViewModel.class);

        setupActivityViewModel.startObserve(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer step) {
                navigateToHome(step);
            }
        });
//        AddDataStorage.get().nextStepState.observe(this, observer);
//        navigateToHome(0);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_next, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        if (id == R.id.action_next) {
//            BrowserActivity.start(this);
//        } else {
//            return super.onOptionsItemSelected(item);
//        }
//
//        return true;
//    }

    private Fragment createFragmentFor(int id) {
        if (0 == id) {
            return new SetupNoticeFragment();
        } else {
            return new SetupOptionsFragment();
        }
    }

    private void navigateToHome(int step) {
        if (0 == step || 1 == step) {
            String tag = "TAG" + step;
            Fragment fragment = getFragmentWithTag(tag);
            if (null == fragment) {
                fragment = createFragmentFor(step);
            }
            replaceFragment(fragment, tag);
        } else {
            ScrollingActivity.startByAction(this, step);
        }
    }

    private Fragment getFragmentWithTag(String tag) {
        return fragmentManager.findFragmentByTag(tag);
    }

    // todo: 给你BottomNavigation一起使用时，back按键与正常行为（都直接退出？）addToBackStack
    private void replaceFragment(Fragment fragment, String tag) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft = ft.replace(containerId, fragment, tag);
        ft.commitAllowingStateLoss();
    }
}
