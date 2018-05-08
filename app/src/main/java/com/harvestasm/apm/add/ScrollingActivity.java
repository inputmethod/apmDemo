package com.harvestasm.apm.add;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;

import com.harvestasm.apm.sample.R;
import com.harvestasm.chart.BaseChartActivity;

import java.util.List;

import typany.apm.agent.android.harvest.ApplicationInformation;

public class ScrollingActivity extends BaseChartActivity implements ItemListDialogFragment.Listener {
    private static final String TAG = ScrollingActivity.class.getSimpleName();

    private int containerId;
    private FragmentManager fragmentManager;
    private AddViewModel addViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

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
        addViewModel.getImeAppLiveData().observe(this, new Observer<List<ApplicationInformation>>() {
            @Override
            public void onChanged(@Nullable List<ApplicationInformation> appItems) {
                if (null == appItems) {
                    Log.e(TAG, "get null app items.");
                }

                for (ApplicationInformation item : appItems) {
                    Log.i(TAG, "item " + item.getAppName());
                }
            }
        });

        addViewModel.loadImeMethods();
//        loadImeMethods();
    }

    @Override
    public void onItemClicked(int position) {
        // todo: response to item click
    }

    public static void startByAction(Context context, int id) {
        int textId;
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_memory) {
            textId = R.string.memory;
        } else if (id == R.id.action_add_key_time) {
            textId = R.string.pressing_key_time;
        } else if (id == R.id.action_add_keyboard_hide) {
            textId = R.string.keyboard_hide;
        } else if (id == R.id.action_add_electric_current) {
            textId = R.string.electric_current;
        } else if (id == R.id.action_add_frame) {
            textId = R.string.frame;
        } else if (id == R.id.action_add_cpu) {
            textId = R.string.cpu;
        } else {
            textId = 0;
        }

//        startActivity(ScrollingActivity.class);
        Intent intent = new Intent(context, ScrollingActivity.class);
        intent.putExtra("actionId", id);
        intent.putExtra("title", context.getString(textId));
        context.startActivity(intent);
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
//                    AddDataStorage.get().testConnect(connectInformation);
//
//                    String pinYin = HanziToPinyin.getInstance().getPinYin(applicationInformation.getTitle());
//                    HarvestData harvestData = new HarvestData(applicationInformation, deviceInformation);
//                    // todo: build harvest data
//                    CustomMetricMeasurement metric = CustomMetricProducer.makeMeasurement("Switfkey",
//                            "keypop", 1, 170.83, 0, MetricUnit.OPERATIONS, MetricUnit.MS);
//                    harvestData.getMetrics().addMetric(metric.getCustomMetric());
//                    AddDataStorage.get().testData(harvestData);
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
}
