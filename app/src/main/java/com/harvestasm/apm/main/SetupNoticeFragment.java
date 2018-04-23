package com.harvestasm.apm.main;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.harvestasm.apm.home.HomeDeviceItem;
import com.harvestasm.apm.imepicker.ImePickerAdapter;
import com.harvestasm.apm.imepicker.ImePickerViewModel;
import com.harvestasm.apm.sample.R;
import com.harvestasm.base.viewholder.SwipeRefreshBaseFragment;

import java.util.List;

import typany.apm.agent.android.harvest.DeviceInformation;

/**
 * A placeholder fragment containing a simple view.
 */
public class SetupNoticeFragment extends SwipeRefreshBaseFragment<DeviceInformation, RelativeLayout> {
    private static final String TAG = SetupNoticeFragment.class.getSimpleName();

    private View noticeTips;
    private TextView tvDevice;
    private TextView tvOk;

    private RecyclerView recyclerView;

    private SetupNoticeViewModel setupNoticeViewModel;

    private ImePickerAdapter cda;

    public SetupNoticeFragment() {
    }

    @Override
    protected int getCollectionLayoutResourceId() {
        return R.layout.fragment_notice;
    }

    @Override
    protected void refreshChangedData(RelativeLayout lv, DeviceInformation item) {
//        String info = dataItem.getHwVendor() + "\n" + dataItem.getHwModel() + "\n" + dataItem.getHwId();
        String info = getString(R.string.setup_notice, item.getManufacturer(),
                item.getModel(), item.getDeviceId(), item.getOsName(), item.getOsVersion(), item.getOsBuild());

        Log.d(TAG, "refreshChangedData, " + info);
        tvDevice.setText(info);
        tvOk.setEnabled(true);
    }


    protected void onChildViewReady(RelativeLayout relativeLayout) {
        recyclerView = relativeLayout.findViewById(R.id.list);
        tvDevice = relativeLayout.findViewById(R.id.tv_notice_title);
        tvOk = relativeLayout.findViewById(R.id.tv_go);
        tvOk.setEnabled(false);

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImeApp();
            }
        });

        noticeTips = relativeLayout.findViewById(R.id.notice_tips_layout);
    }

    @Override
    protected LiveData<DeviceInformation> setViewModel() {
        setupNoticeViewModel = newViewModel(SetupNoticeViewModel.class);

        setupNoticeViewModel.networkState.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                Log.d(TAG, "networking value " + integer);
            }
        });

        startLoading(setupNoticeViewModel.refreshState);

        return setupNoticeViewModel.items;
    }

    @Override
    protected void doLoadingTask() {
        setupNoticeViewModel.load();
    }

    private void pickImeApp() {
        final ImePickerViewModel imePickerViewModel = ViewModelProviders.of(this).get(ImePickerViewModel.class);
        imePickerViewModel.items.
                observe(this, new Observer<List<HomeDeviceItem.AppItem>>() {
            @Override
            public void onChanged(@Nullable List<HomeDeviceItem.AppItem> chartItems) {
                if (null == chartItems) {
                    Log.w(TAG, "null data comes.");
                } else {
                    Log.d(TAG, "data size " + chartItems.size());
                    if (null == cda) {
                        cda = new ImePickerAdapter(getContext(), chartItems, imePickerViewModel);
                        cda.setEditMode(1);
                        recyclerView.setAdapter(cda);
                    } else {
                        cda.notifyAdapter(chartItems, false);
                    }
                }
            }
        });

        imePickerViewModel.refreshState.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                Log.d(TAG, "loading value " + aBoolean);
                setRefreshing(aBoolean);
            }
        });

        imePickerViewModel.load(false);
        noticeTips.setVisibility(View.GONE);
    }
}