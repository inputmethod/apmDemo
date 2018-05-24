package com.harvestasm.apm.filter.item;

import com.harvestasm.apm.base.pikcer.PickerBaseAdapter;
import com.harvestasm.apm.base.pikcer.ActionModelInterface;

import java.util.List;

public class FilterItemAdapter extends PickerBaseAdapter<FilterItemModel> {
    public FilterItemAdapter(List<FilterItemModel> itemList, ActionModelInterface pickerModel) {
        super(itemList, pickerModel);
        hideIcon();
    }
}
