package com.harvestasm.apm.browser;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.harvestasm.apm.base.BaseFragmentActivity;
import com.harvestasm.apm.filter.FilterFragment;
import com.harvestasm.apm.sample.R;

public class BrowserActivity extends BaseFragmentActivity {
    @Override
    protected void onCreateComplete() {
        super.onCreateComplete();

        DataStorage.get().currentState.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean filter) {
                navigateToHome(filter);
            }
        });

        navigateToHome(false);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_preview;
    }

    private void navigateToHome(boolean filter) {
        String tag = filter ? FilterFragment.class.getSimpleName() : BrowserFragment.class.getSimpleName();
        Fragment fragment = getFragmentWithTag(tag);
        if (null == fragment) {
            fragment = newFragment(filter);
        }
        replaceFragment(fragment, tag);
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
