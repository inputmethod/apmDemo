package com.harvestasm.apm.preview;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.harvestasm.apm.base.BaseFragmentActivity;
import com.harvestasm.apm.sample.R;

public class PreviewActivity extends BaseFragmentActivity {
    @Override
    protected void onCreateComplete() {
        super.onCreateComplete();

        setTitle(R.string.action_preview);

        String tag = PreviewActivity.class.getSimpleName();
        Fragment fragment = getFragmentWithTag(tag);
        if (null == fragment) {
            fragment = new PreviewFragment();
        }
        replaceFragment(fragment, tag);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_preview;
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, PreviewActivity.class);
        context.startActivity(intent);
    }
}
