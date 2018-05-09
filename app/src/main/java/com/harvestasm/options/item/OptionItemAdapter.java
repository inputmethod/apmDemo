package com.harvestasm.options.item;

import com.harvestasm.apm.base.pikcer.ActionModelInterface;
import com.harvestasm.apm.base.pikcer.PickerBaseAdapter;

import java.util.List;

public class OptionItemAdapter extends PickerBaseAdapter<OptionItemModel> {
    public OptionItemAdapter(List<OptionItemModel> itemList, ActionModelInterface pickerModel) {
        super(itemList, pickerModel);
    }
}
