package com.harvestasm.options;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.harvestasm.apm.sample.R;
import com.harvestasm.options.item.OptionItemAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/** adapter that supports 3 different item types */
public class OptionAdapter extends RecyclerView.Adapter<OptionAdapter.ViewHolder> {
    private final List<OptionCategoryModel> itemModelList = new ArrayList<>();
    private final OptionViewModel optionViewModel;

    public OptionAdapter(List<OptionCategoryModel> itemModels, OptionViewModel optionViewModel) {
        this.optionViewModel = optionViewModel;
        itemModelList.addAll(itemModels);
    }


    public void notifyAdapter(List<OptionCategoryModel> myLiveList, boolean isAdd) {
        if (!isAdd) {
            this.itemModelList.clear();
        }
            this.itemModelList.addAll(myLiveList);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_filter, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public int getItemCount() {
        return itemModelList.size();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final OptionCategoryModel myLive = itemModelList.get(holder.getAdapterPosition());
        holder.bind(myLive, optionViewModel);
    }

    public void setNetworkState(Integer integer) {

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_title)
        TextView titleTextView;
        @BindView(R.id.list)
        RecyclerView recyclerView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            Context context = itemView.getContext();
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        }

        public void bind(OptionCategoryModel model, OptionViewModel optionViewModel) {
            titleTextView.setText(model.getTitle() + " : " + model.getCandidates().size());

            OptionItemAdapter itemAdapter = new OptionItemAdapter(model.getCandidates(), optionViewModel);
            itemAdapter.setEditMode(1);
            recyclerView.setAdapter(itemAdapter);
        }
    }
}
