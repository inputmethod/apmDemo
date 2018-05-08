package com.harvestasm.apm.filter.item;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.harvestasm.apm.filter.FilterViewModel;
import com.harvestasm.apm.sample.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

/** adapter that supports 3 different item types */
public class FilterItemAdapter extends RecyclerView.Adapter<FilterItemAdapter.ViewHolder> {
    private static final int MYLIVE_MODE_CHECK = 0;

    int mEditMode = MYLIVE_MODE_CHECK;

    private final List<String> optionsList = new ArrayList<>();

    private final String category;
    private final FilterViewModel viewMultiChartModel;

    public FilterItemAdapter(@NonNull String category, Set<String> candidates, @NonNull FilterViewModel viewMultiChartModel) {
        this.category = category;
        optionsList.addAll(candidates);
        this.viewMultiChartModel = viewMultiChartModel;
    }


    public void notifyAdapter(Set<String> myLiveList, boolean isAdd) {
        if (!isAdd) {
            this.optionsList.clear();
        }
            this.optionsList.addAll(myLiveList);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_live, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public int getItemCount() {
        return optionsList.size();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final String name = optionsList.get(holder.getAdapterPosition());
        holder.bind(name, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewMultiChartModel.toggleSelected(category, name);
                notifyItemChanged(position);
            }
        }, mEditMode, viewMultiChartModel.isSelect(category, name));
    }

    public void setNetworkState(Integer integer) {

    }

    public void setEditMode(int editMode) {
        mEditMode = editMode;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.radio_img)
        ImageView mRadioImg;
        @BindView(R.id.tv_title)
        TextView mTvTitle;
        @BindView(R.id.tv_source)
        TextView mTvSource;
        @BindView(R.id.root_view)
        RelativeLayout mRootView;
        @BindView(R.id.check_box)
        ImageView mCheckBox;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }

        public void bind(String myLive, View.OnClickListener clickListener, int mEditMode, boolean select) {
            mTvTitle.setText(myLive);
            mTvSource.setText(myLive);
            if (mEditMode == MYLIVE_MODE_CHECK) {
                mCheckBox.setVisibility(View.GONE);
            } else {
                mCheckBox.setVisibility(View.VISIBLE);

                if (select) {
                    mCheckBox.setImageResource(R.mipmap.ic_checked);
                } else {
                    mCheckBox.setImageResource(R.mipmap.ic_uncheck);
                }
            }
            itemView.setOnClickListener(clickListener);
        }
    }
}
