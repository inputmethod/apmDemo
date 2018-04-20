package com.harvestasm.apm.main;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.widget.TextView;

import com.harvestasm.apm.sample.R;
import com.harvestasm.base.viewholder.SwipeRefreshBaseFragment;

import butterknife.BindView;
import typany.apm.agent.android.harvest.DeviceInformation;

/**
 * A placeholder fragment containing a simple view.
 */
public class SetupNoticeFragment extends SwipeRefreshBaseFragment<DeviceInformation, NestedScrollView> {
    private static final String TAG = SetupNoticeFragment.class.getSimpleName();

    @BindView(R.id.tv_notice_device)
    TextView tvDevice;

    private SetupNoticeViewModel setupNoticeViewModel;

    public SetupNoticeFragment() {
    }

    @Override
    protected int getCollectionLayoutResourceId() {
        return R.layout.fragment_notice;
    }

    @Override
    protected void refreshChangedData(NestedScrollView lv, DeviceInformation item) {
//        String info = dataItem.getHwVendor() + "\n" + dataItem.getHwModel() + "\n" + dataItem.getHwId();
        String info = getString(R.string.setup_device, item.getManufacturer(),
                item.getModel(), item.getDeviceId(), item.getOsName(), item.getOsVersion(), item.getOsBuild());
        Log.d(TAG, "refreshChangedData, " + info);
        if (null == tvDevice) {
            tvDevice = lv.findViewById(R.id.tv_notice_device);
        }
        if (null != tvDevice) {
            tvDevice.setText(info);
        }
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
}
