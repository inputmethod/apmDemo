package com.harvestasm.apm.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.harvestasm.apm.sample.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class SetupNoticeFragment extends Fragment {

    public SetupNoticeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notice, container, false);
    }
}
