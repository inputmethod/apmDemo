package com.harvestasm.apm.setup;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

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
        assert (SetupActivityViewModel.SETUP_NOTICE == step || SetupActivityViewModel.SETUP_PREVIEW == step);

        String tag = "TAG" + step;
        Fragment fragment = getFragmentWithTag(tag);
        if (null == fragment) {
            fragment = createFragmentFor(step);
        }
        replaceFragment(fragment, tag);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_next) {
            return setupActivityViewModel.toggleNext();
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

}
