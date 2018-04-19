package com.harvestasm.apm.add;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;

import com.harvestasm.apm.home.HomeDeviceItem;
import com.harvestasm.apm.sample.R;

import java.util.List;

public class ScrollingActivity extends AppCompatActivity implements ItemListDialogFragment.Listener {
    private static final String TAG = ScrollingActivity.class.getSimpleName();

    private int containerId;
    private FragmentManager fragmentManager;
    private AddViewModel addViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.containerId = R.id.container;
        this.fragmentManager = getSupportFragmentManager();

        String title = getIntent().getStringExtra("title");
        if (!TextUtils.isEmpty(title)) {
            setTitle(title);
        }

        int id = getIntent().getIntExtra("actionId", 0);
        final String tag = "action" + id;
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (null == fragment) {
            if (id == R.id.action_add_memory) {
                fragment = new AddMemoryFragment();
            } else if (id == R.id.action_add_key_time) {
                fragment = new AddKeyTimeFragment();
            } else if (id == R.id.action_add_keyboard_hide) {
                fragment = new AddKbHideFragment();
            } else if (id == R.id.action_add_electric_current) {
                fragment = new AddElectricCurrentFragment();
            } else if (id == R.id.action_add_frame) {
                fragment = new AddFrameFragment();
            } else if (id == R.id.action_add_cpu) {
                fragment = new AddCpuFragment();
            } else {
                // unknown cases.
                return;
            }
        }

        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft = ft.replace(containerId, fragment, tag);
        ft.commitAllowingStateLoss();

        addViewModel = ViewModelProviders.of(this).get(AddViewModel.class);
        addViewModel.getImeAppLiveData().observe(this, new Observer<List<HomeDeviceItem.AppItem>>() {
            @Override
            public void onChanged(@Nullable List<HomeDeviceItem.AppItem> appItems) {
                if (null == appItems) {
                    Log.e(TAG, "get null app items.");
                }

                for (HomeDeviceItem.AppItem item : appItems) {
                    Log.i(TAG, "item " + item.getAppName());
                }
            }
        });

        addViewModel.loadImeMethods();
    }

//    public void loadImeMethods() {
//        Log.d("mft","当前已经安装的输入法有");
//        List<ConnectInformation> informationList = new ArrayList<>();
//
//        DeviceInformation deviceInformation = Agent.getDeviceInformation();
//        for (String name : IMEHelper.getInstallImePackageList(this)) {
//            Log.d("mft", name);
//            try {
//                ApplicationInformation applicationInformation = IMEApplicationHelper.parseInstallImePackage(this, name);
//                ConnectInformation connectInformation = new ConnectInformation(applicationInformation, deviceInformation);
//                informationList.add(connectInformation);
//                if ("com.touchtype.swiftkey".equals(name)) {
//                    testConnect(connectInformation);
//                    testData(new HarvestData(applicationInformation, deviceInformation));
//                }
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//        }
//
//        Log.d("mft","已经勾选的输入法有");
////        String enable = Settings.Secure.getString(getContentResolver(),
////                Settings.Secure.ENABLED_INPUT_METHODS);
////        Log.d("mft", enable.replace(":","\n"));
//        for (String ime : IMEHelper.getCheckedImeList(this)) {
//            Log.d("mft", ime);
//        }
//
//
//        Log.d("mft","当前默认输入法是");
////        String currentInputmethod = Settings.Secure.getString(getContentResolver(),
////                Settings.Secure.DEFAULT_INPUT_METHOD);
////        Log.d("mft", currentInputmethod);
//        Log.d("mft", IMEHelper.getCurrentIme(this));
//    }
//
//    private void testData(final HarvestData harvestData) {
//        runInThread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    // todo: build harvest data
//                    CustomMetricMeasurement metric = CustomMetricProducer.makeMeasurement("Typany", "keypop", 1, 170.83, 0, MetricUnit.OPERATIONS, MetricUnit.MS);
//                    harvestData.getMetrics().addMetric(metric.getCustomMetric());
//                    ApmConnectResponse response = repository.apmTestData(harvestData.toJsonOutput());
//                    Log.d("mft", "That is it " + response.get_id());
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }
//            }
//        }, "testData");
//    }
//
//    private void testConnect(final ConnectInformation connectInformation) {
//        runInThread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    ApmConnectResponse response = repository.apmTestConnect(connectInformation.asELKJson());
//                    Log.d("mft", "That is it " + response.get_id());
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }
//            }
//        }, "testConnect");
//    }
//
//    private void runInThread(Runnable runnable, String threadName) {
//        new Thread(runnable, threadName).start();
//    }

    @Override
    public void onItemClicked(int position) {
        // todo: response to item click
    }
}
