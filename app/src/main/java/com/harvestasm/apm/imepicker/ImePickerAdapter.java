package com.harvestasm.apm.imepicker;

import com.harvestasm.apm.base.pikcer.ActionModelInterface;
import com.harvestasm.apm.base.pikcer.PickerBaseAdapter;

import java.util.List;

/** adapter that supports 3 different item types */
public class ImePickerAdapter extends PickerBaseAdapter<ImeAppModel> {
    public ImePickerAdapter(List<ImeAppModel> itemList, ActionModelInterface pickerModel) {
        super(itemList, pickerModel);
    }
}
