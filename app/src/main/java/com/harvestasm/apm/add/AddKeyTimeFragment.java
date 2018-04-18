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

import com.harvestasm.apm.sample.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class AddKeyTimeFragment extends Fragment {
    private static final int[] companyIdList = {
            R.string.com_typany,
            R.string.com_swiftkey,
            R.string.com_touchpal,
            R.string.com_gboard,
            R.string.com_facemoji,
    };

    public AddKeyTimeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LinearLayout view = (LinearLayout) inflater.inflate(R.layout.fragment_add_key_time, container, false);
        for (int id : companyIdList) {
            View v = inflater.inflate(R.layout.fragment_add_entry, null, false);
            ((TextView) v.findViewById(R.id.entry_key)).setText(getText(id) + "(ms)");
            int inputType = InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL | InputType.TYPE_NUMBER_FLAG_DECIMAL;
            ((EditText) v.findViewById(R.id.entry_value)).setInputType(inputType);
            view.addView(v);
        }
        return view;
    }
}
