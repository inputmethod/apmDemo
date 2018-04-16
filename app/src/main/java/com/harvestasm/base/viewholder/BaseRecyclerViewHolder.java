package com.harvestasm.base.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

abstract public class BaseRecyclerViewHolder<T> extends RecyclerView.ViewHolder {
    public BaseRecyclerViewHolder(View itemView) {
        super(itemView);
    }
    abstract public void bind(T homeItem);
}
