package com.harvestasm.apm.home;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.harvestasm.apm.sample.R;
import com.harvestasm.base.viewholder.BaseRecyclerViewHolder;

import java.util.List;

public class HomeDeviceItemViewHolder extends BaseRecyclerViewHolder<HomeDeviceItem> {
    private final TextView deviceIdView;
    private final TextView deviceModelView;
    private final TextView deviceVendorView;
    private final TextView osView;
//    private final TextView osCountView;
    private final RecyclerView osRecyclerView;
    private final TextView appView;
//    private final TextView appCountView;
    private final RecyclerView appRecyclerView;

    public HomeDeviceItemViewHolder(View itemView) {
        super(itemView);
        deviceIdView = itemView.findViewById(R.id.deviceId_tv);
        deviceModelView = itemView.findViewById(R.id.model_tv);
        deviceVendorView = itemView.findViewById(R.id.vendor_tv);

        osView = itemView.findViewById(R.id.os_tv);
//        osCountView = itemView.findViewById(R.id.os_count_tv);
        osRecyclerView = itemView.findViewById(R.id.os_recyclerView);
        osRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));

        appView = itemView.findViewById(R.id.app_tv);
//        appCountView = itemView.findViewById(R.id.app_count_tv);
        appRecyclerView = itemView.findViewById(R.id.app_recyclerView);
        appRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
    }

    @Override
    public void bind(HomeDeviceItem homeItem) {
        String headline = String.format("%s:(d%s|c%s)", homeItem.getId(), homeItem.getDataCount(), homeItem.getConnectCount());
        deviceIdView.setText(headline);
        List<HomeDeviceItem.HardwareItem> hwItemList = homeItem.getHardwareItemList();
        HomeDeviceItem.HardwareItem firstItem = hwItemList.get(0);
        deviceModelView.setText(firstItem.getHwModel());
        deviceVendorView.setText(firstItem.getHwVendor() + "(" + hwItemList.size() + ")");

        List<HomeDeviceItem.OsItem> osItemList = homeItem.getOsItemList();
        osView.setText("Os Versions : " + osItemList.size());

        List<HomeDeviceItem.AppItem> appItemList = homeItem.getAppItemList();
        appView.setText("App Versions : " + appItemList.size());

        HomeSubAdapter appAdapter = new HomeSubAdapter(itemView.getContext(), homeItem.getAppItemList());
        appRecyclerView.setAdapter(appAdapter);

        HomeSubAdapter osAdapter = new HomeSubAdapter(itemView.getContext(), homeItem.getOsItemList());
        osRecyclerView.setAdapter(osAdapter);
    }
}
