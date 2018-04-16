package com.harvestasm.apm.home;

import android.view.View;
import android.widget.TextView;

import com.harvestasm.apm.sample.R;
import com.harvestasm.base.viewholder.BaseRecyclerViewHolder;

public class HomeSubViewHolder<T extends HomeDeviceItem.ItemDisplay> extends BaseRecyclerViewHolder<T> {
    private final TextView deviceIdView;
    private final TextView deviceModelView;
    private final TextView deviceVendorView;

    public HomeSubViewHolder(View itemView) {
        super(itemView);
        deviceIdView = itemView.findViewById(R.id.deviceId_tv);
        deviceModelView = itemView.findViewById(R.id.model_tv);
        deviceVendorView = itemView.findViewById(R.id.vendor_tv);
    }

    @Override
    public void bind(T appItem) {
        deviceIdView.setText(appItem.getDisplayName());
        deviceModelView.setText(appItem.getDisplayVersion());
        deviceVendorView.setText(appItem.getDisplayExtra());
    }
}
