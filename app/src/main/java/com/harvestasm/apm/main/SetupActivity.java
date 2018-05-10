package com.harvestasm.apm.main;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.harvestasm.apm.sample.R;
import com.harvestasm.chart.BaseChartActivity;
import com.harvestasm.options.OptionListFragment;

public class SetupActivity extends BaseChartActivity {
    private SetupActivityViewModel setupActivityViewModel;

    @Override
    protected void onCreateComplete() {
        super.onCreateComplete();

        this.setupActivityViewModel = ViewModelProviders.of(this).get(SetupActivityViewModel.class);
        setupActivityViewModel.startObserve(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer step) {
                navigateToHome(step);
            }
        });
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_setup;
    }

    private Fragment createFragmentFor(int id) {
        if (0 == id) {
            return new SetupNoticeFragment();
        } else {
            return new OptionListFragment();
        }
    }

    private void navigateToHome(int step) {
        assert (0 == step || 1 == step);

        String tag = "TAG" + step;
        Fragment fragment = getFragmentWithTag(tag);
        if (null == fragment) {
            fragment = createFragmentFor(step);
        }
        replaceFragment(fragment, tag);
    }
}
