package com.harvestasm.apm.add;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.harvestasm.apm.home.HomeDeviceItem;
import com.harvestasm.apm.sample.R;

import java.util.ArrayList;
import java.util.List;

import typany.apm.agent.android.Agent;
import typany.apm.agent.android.harvest.ApplicationInformation;
import typany.apm.agent.android.harvest.DeviceInformation;
import typany.apm.agent.android.harvest.HarvestData;
import typany.apm.agent.android.measurement.CustomMetricMeasurement;
import typany.apm.agent.android.measurement.producer.CustomMetricProducer;
import typany.apm.agent.android.metric.MetricUnit;
import typany.apm.agent.android.util.IMEApplicationHelper;

/**
 * A placeholder fragment containing a simple view.
 */
public class AddKeyTimeFragment extends BaseAddFragment {
    private final List<EditText> editTextList = new ArrayList<>();

    public AddKeyTimeFragment() {
    }

    private final TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (TextUtils.isEmpty(s)) {
                enableNextMenu(false);
            } else {
                checkMenuState();
            }
        }
    };

    private void checkMenuState() {
        for (EditText editText : editTextList) {
            Editable editable = editText.getText();
            if (TextUtils.isEmpty(editable)) {
                enableNextMenu(false);
                return;
            }
        }

        enableNextMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LinearLayout view = (LinearLayout) inflater.inflate(R.layout.fragment_vertical_linear_container, container, false);

//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        for (HomeDeviceItem.AppItem item : AddDataStorage.get().selectedImeAppList) {
            View v = inflater.inflate(R.layout.fragment_add_entry, null, false);
            TextView textView = v.findViewById(R.id.entry_key);
            textView.setText(item.getAppName() + "(ms)");
            int inputType = InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL | InputType.TYPE_NUMBER_FLAG_DECIMAL;
            EditText editText = v.findViewById(R.id.entry_value);
            editText.setInputType(inputType);
            editText.addTextChangedListener(watcher);
            editText.setTag(item);
            editTextList.add(editText);
            view.addView(v);
//            view.addView(v, lp);
        }

        setHasOptionsMenu(true);
//        checkMenuState();
        return view;
    }

    @Override
    protected boolean nextStep() {
        try {
            DeviceInformation deviceInformation = Agent.getDeviceInformation();
            ApplicationInformation applicationInformation = IMEApplicationHelper.parseInstallImePackage(getContext(), getActivity().getPackageName());

            HarvestData harvestData = new HarvestData(applicationInformation, deviceInformation);

            // todo: build harvest data
            for (EditText editText : editTextList) {
                HomeDeviceItem.AppItem item = (HomeDeviceItem.AppItem) editText.getTag();
                double value = Double.parseDouble(editText.getText().toString());
                CustomMetricMeasurement metric = CustomMetricProducer.makeMeasurement(item.getAppName(),
                        "keypop", 1, value, 0, MetricUnit.OPERATIONS, MetricUnit.MS);
                harvestData.getMetrics().addMetric(metric.getCustomMetric());

            }
            AddDataStorage.get().testData(harvestData);
            return true;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

//    public void loadImeMethods() {
//        Log.d("mft","当前已经安装的输入法有");
//        List<ConnectInformation> informationList = new ArrayList<>();
//
//        for (String name : IMEHelper.getInstallImePackageList(getContext())) {
//            Log.d("mft", name);
//            try {
//                ApplicationInformation applicationInformation = IMEApplicationHelper.parseInstallImePackage(getContext(), name);
//                ConnectInformation connectInformation = new ConnectInformation(applicationInformation, deviceInformation);
//                informationList.add(connectInformation);
//                if ("com.touchtype.swiftkey".equals(name)) {
//                    AddDataStorage.get().testConnect(connectInformation);
//                    AddDataStorage.get().testData(new HarvestData(applicationInformation, deviceInformation));
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
//        for (String ime : IMEHelper.getCheckedImeList(getContext())) {
//            Log.d("mft", ime);
//        }
//
//
//        Log.d("mft","当前默认输入法是");
////        String currentInputmethod = Settings.Secure.getString(getContentResolver(),
////                Settings.Secure.DEFAULT_INPUT_METHOD);
////        Log.d("mft", currentInputmethod);
//        Log.d("mft", IMEHelper.getCurrentIme(getContext()));
//    }
}
