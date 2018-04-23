package com.harvestasm.apm.main;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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

    public SetupOptionsFragment() {
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
        return view;
    }
}
