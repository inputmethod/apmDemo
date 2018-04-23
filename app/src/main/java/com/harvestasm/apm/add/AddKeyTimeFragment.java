package com.harvestasm.apm.add;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.harvestasm.apm.home.HomeDeviceItem;
import com.harvestasm.apm.sample.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class AddKeyTimeFragment extends Fragment {
    private int optionId;

    public AddKeyTimeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        parseArguments();
        LinearLayout view = (LinearLayout) inflater.inflate(R.layout.fragment_vertical_linear_container, container, false);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        for (HomeDeviceItem.AppItem item : AddDataStorage.get().selectedImeAppList) {
            View v = inflater.inflate(R.layout.fragment_add_entry, null, false);
            ((TextView) v.findViewById(R.id.entry_key)).setText(item.getAppName() + "(ms)");
            int inputType = InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL | InputType.TYPE_NUMBER_FLAG_DECIMAL;
            ((EditText) v.findViewById(R.id.entry_value)).setInputType(inputType);
            view.addView(v, lp);
        }
        return view;
    }

    private void parseArguments() {
        Bundle bundle = getArguments();
        if (null == bundle) {
            return;
        }

        optionId = bundle.getInt("addId");
        if (optionId == R.id.action_add_memory) {
            getActivity().setTitle(R.string.memory);
        } else if (optionId == R.id.action_add_key_time) {
            getActivity().setTitle(R.string.pressing_key_time);
        } else if (optionId == R.id.action_add_keyboard_hide) {
            getActivity().setTitle(R.string.keyboard_hide);
        } else if (optionId == R.id.action_add_electric_current) {
            getActivity().setTitle(R.string.electric_current);
        } else if (optionId == R.id.action_add_frame) {
            getActivity().setTitle(R.string.frame);
        } else if (optionId == R.id.action_add_cpu) {
            getActivity().setTitle(R.string.cpu);
        } else {
            getActivity().setTitle(R.string.app_name);
        }
    }
}
