package com.harvestasm.apm.add;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;

import com.harvestasm.apm.repository.ApmRepository;
import com.harvestasm.apm.repository.model.connect.ApmConnectResponse;
import com.harvestasm.apm.sample.R;
import com.harvestasm.apm.utils.IMEHelper;

import java.util.ArrayList;
import java.util.List;

import typany.apm.agent.android.Agent;
import typany.apm.agent.android.harvest.ApplicationInformation;
import typany.apm.agent.android.harvest.ConnectInformation;
import typany.apm.agent.android.harvest.DeviceInformation;
import typany.apm.agent.android.util.IMEApplicationHelper;

public class ScrollingActivity extends AppCompatActivity implements ItemListDialogFragment.Listener {

    private int containerId;
    private FragmentManager fragmentManager;

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

        showImeMethods();
    }

    private final ApmRepository repository = new ApmRepository();
    private void showImeMethods() {

        Log.d("mft","当前已经安装的输入法有");
        List<ConnectInformation> informationList = new ArrayList<>();

        DeviceInformation deviceInformation = Agent.getDeviceInformation();
        for (String name : IMEHelper.getInstallImePackageList(this)) {
            Log.d("mft", name);
            try {
                ApplicationInformation applicationInformation = IMEApplicationHelper.parseInstallImePackage(this, name);
                ConnectInformation connectInformation = new ConnectInformation(applicationInformation, deviceInformation);
                informationList.add(connectInformation);
                if ("com.touchtype.swiftkey".equals(name)) {
                    testConnect(connectInformation);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        Log.d("mft","已经勾选的输入法有");
//        String enable = Settings.Secure.getString(getContentResolver(),
//                Settings.Secure.ENABLED_INPUT_METHODS);
//        Log.d("mft", enable.replace(":","\n"));
        for (String ime : IMEHelper.getCheckedImeList(this)) {
            Log.d("mft", ime);
        }


        Log.d("mft","当前默认输入法是");
//        String currentInputmethod = Settings.Secure.getString(getContentResolver(),
//                Settings.Secure.DEFAULT_INPUT_METHOD);
//        Log.d("mft", currentInputmethod);
        Log.d("mft", IMEHelper.getCurrentIme(this));
    }

    private void testConnect(final ConnectInformation connectInformation) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ApmConnectResponse response = repository.apmTestConnect(connectInformation.asELKJson());
                    Log.d("mft", "That is it " + response.get_id());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onItemClicked(int position) {
        // todo: response to item click
    }
}
