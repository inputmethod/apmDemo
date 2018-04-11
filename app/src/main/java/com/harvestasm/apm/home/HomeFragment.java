package com.harvestasm.apm.home;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.harvestasm.apm.sample.R;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class HomeFragment extends Fragment {

    private static final String TAG = HomeFragment.class.getSimpleName();

    private HomeModel homeModel;

    private RecyclerView lv;
    private HomeAdapter cda;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        lv = view.findViewById(R.id.list);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setViewModel();
        startLoading();
    }


    private void startLoading() {
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Regular.ttf");
        homeModel.load(typeface);
    }

    private void setViewModel() {
        homeModel = getViewModel();

        homeModel.items.observe(this, new Observer<List<HomeItem>>() {
            @Override
            public void onChanged(@Nullable List<HomeItem> chartItems) {
                if (null == chartItems) {
                    Log.w(TAG, "null data comes.");
                } else {
                    Log.d(TAG, "data size " + chartItems.size());
                    cda = new HomeAdapter(getContext(), chartItems, homeModel);
                    lv.setAdapter(cda);
                }
            }
        });
    }

    private HomeModel getViewModel() {
        return ViewModelProviders.of(this)
                .get(HomeModel.class);
    }
}
