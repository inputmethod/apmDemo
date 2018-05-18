package com.harvestasm.apm.setup;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.harvestasm.apm.imepicker.ImeAppModel;
import com.harvestasm.apm.imepicker.ImePickerAdapter;
import com.harvestasm.apm.imepicker.ImePickerViewModel;
import com.harvestasm.apm.sample.R;
import com.harvestasm.base.RefreshBaseFragment;

import java.util.ArrayList;
import java.util.List;

import typany.apm.agent.android.harvest.ApplicationInformation;
import typany.apm.agent.android.harvest.DeviceInformation;

/**
 * A placeholder fragment containing a simple view.
 */
public class SetupNoticeFragment extends RefreshBaseFragment<DeviceInformation, RelativeLayout> {
    private static final String TAG = SetupNoticeFragment.class.getSimpleName();

    private View noticeTips;
    private TextView tvDevice;
    private TextView tvOk;

    private RecyclerView recyclerView;

    private SetupFragmentViewModel fragmentViewModel;

    private ImePickerAdapter imePickerAdapter;

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
        fragmentViewModel = newViewModel(SetupFragmentViewModel.class);

        fragmentViewModel.networkState.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                Log.d(TAG, "networking value " + integer);
            }
        });

        startLoading(fragmentViewModel.refreshState);

        return fragmentViewModel.items;
    }

    @Override
    protected void doLoadingTask(boolean force) {
        fragmentViewModel.load();
    }

    private void pickImeApp() {
        final ImePickerViewModel imePickerViewModel = ViewModelProviders.of(this).get(ImePickerViewModel.class);
        imePickerViewModel.items.
                observe(this, new Observer<List<ApplicationInformation>>() {
            @Override
            public void onChanged(@Nullable List<ApplicationInformation> applicationInformationList) {
                List<ImeAppModel> modelList = new ArrayList<>();
                if (null == applicationInformationList) {
                    Log.w(TAG, "null data comes.");
                } else {
                    Log.d(TAG, "data size " + applicationInformationList.size());
                    Context context = getContext();
                    for (ApplicationInformation information : applicationInformationList) {
                        Drawable icon = getAppIcon(context, information.getPackageId());
                        modelList.add(new ImeAppModel(information, icon));
                    }
                }

                if (null == imePickerAdapter) {
                    imePickerAdapter = new ImePickerAdapter(modelList, imePickerViewModel);
                    imePickerAdapter.setEditMode(1);
                    recyclerView.setAdapter(imePickerAdapter);
                } else {
                    imePickerAdapter.notifyAdapter(modelList, false);
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

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_next, menu);
    }

    public static @Nullable Drawable getAppIcon(Context context, String pkgName) {
        try {
            if (null != pkgName) {
                PackageManager pm = context.getPackageManager();
                ApplicationInfo info = pm.getApplicationInfo(pkgName, 0);
                return info.loadIcon(pm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
