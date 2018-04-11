package com.harvestasm.apm.home;

import android.content.Context;
import android.view.ViewGroup;

import com.harvestasm.base.viewholder.BaseRecyclerViewAdapter;
import com.harvestasm.base.viewholder.BaseRecyclerViewHolder;

import java.util.List;

public class HomeAdapter extends BaseRecyclerViewAdapter {

    public HomeAdapter(Context context, List<HomeItem> chartItems, HomeModel homeModel) {
        super();
    }

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
