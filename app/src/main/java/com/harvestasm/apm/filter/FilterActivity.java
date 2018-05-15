package com.harvestasm.apm.filter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.harvestasm.apm.base.BaseFragmentActivity;
import com.harvestasm.apm.sample.R;

public class FilterActivity extends BaseFragmentActivity {
    @Override
    protected void onCreateComplete() {
        super.onCreateComplete();

        setTitle(R.string.action_filter);

        String tag = FilterActivity.class.getSimpleName();
        Fragment fragment = getFragmentWithTag(tag);
        if (null == fragment) {
            fragment = new FilterListFragment();
        }
        replaceFragment(fragment, tag);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_preview;
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, FilterActivity.class);
        context.startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_next) {
            finish();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
