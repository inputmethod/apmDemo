package com.harvestasm.apm.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.harvestasm.apm.sample.R;
import com.harvestasm.base.viewholder.BaseRecyclerViewAdapter;
import com.harvestasm.base.viewholder.BaseRecyclerViewHolder;

import java.util.List;

public class HomeSubAdapter<T extends HomeDeviceItem.ItemDisplay> extends BaseRecyclerViewAdapter {
    private final Context context;
    private final List<T> appItemList;

    public HomeSubAdapter(Context context, List<T> appItems) {
        super();
        this.context = context;
        appItemList = appItems;
    }

    @Override
    public HomeSubViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        HomeSubViewHolder holder = new HomeSubViewHolder<T>(LayoutInflater.from(
                context).inflate(R.layout.item_home_sub_rv, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position) {
        holder.bind(appItemList.get(position));
    }

    @Override
    public int getItemCount() {
        return appItemList.size();
    }
}
