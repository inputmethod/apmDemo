package com.harvestasm.apm.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.harvestasm.apm.sample.R;
import com.harvestasm.base.viewholder.BaseRecyclerViewAdapter;
import com.harvestasm.base.viewholder.BaseRecyclerViewHolder;

import java.util.List;

public class HomeAdapter extends BaseRecyclerViewAdapter {
    private static final int DEVICE_ITEM = 0;

    private final Context context;
    private final List<HomeItem> homeItemList;
    private final HomeModel homeModel;

    public HomeAdapter(Context context, List<HomeItem> homeItems, HomeModel homeModel) {
        super();
        this.context = context;
        homeItemList = homeItems;
        this.homeModel = homeModel;
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (DEVICE_ITEM == viewType) {
            BaseRecyclerViewHolder holder = new HomeDeviceItemViewHolder(LayoutInflater.from(
                    context).inflate(R.layout.item_home_device, parent,
                    false));
            return holder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position) {
        holder.bind(homeItemList.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        if (homeItemList.get(position) instanceof HomeDeviceItem) {
            return DEVICE_ITEM;
        }

        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return homeItemList.size();
    }
}
