package com.harvestasm.apm.main;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.harvestasm.apm.preview.PreviewActivity;
import com.harvestasm.apm.sample.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class SetupOptionsFragment extends Fragment {

    private SetupActivityViewModel activityViewModel;
    private SetupActivityViewModel getActivityViewModel() {
        if (null == activityViewModel) {
            activityViewModel = ViewModelProviders.of(this).get(SetupActivityViewModel.class);
        }
        return activityViewModel;
    }

    private final View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getActivityViewModel().clickOption(v.getId());
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LinearLayout view = (LinearLayout) inflater.inflate(R.layout.fragment_apm_options, container, false);
        for(int i = 0; i < view.getChildCount(); i++) {
            view.getChildAt(i).setOnClickListener(listener);
        }

        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_next, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_next) {
            PreviewActivity.start(getActivity());
        } else {
            return super.onOptionsItemSelected(item);
        }

        return true;
    }
}
