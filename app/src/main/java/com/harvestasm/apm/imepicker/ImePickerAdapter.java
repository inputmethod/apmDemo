package com.harvestasm.apm.imepicker;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.harvestasm.apm.sample.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import typany.apm.agent.android.harvest.ApplicationInformation;

/** adapter that supports 3 different item types */
public class ImePickerAdapter extends RecyclerView.Adapter<ImePickerAdapter.ViewHolder> {
    private static final int MYLIVE_MODE_CHECK = 0;

    int mEditMode = MYLIVE_MODE_CHECK;

    private final List<ApplicationInformation> mMyLiveList = new ArrayList<>();

    private final ImePickerViewModel viewMultiChartModel;

    public ImePickerAdapter(List<ApplicationInformation> chartItems, ImePickerViewModel viewMultiChartModel) {
        mMyLiveList.addAll(chartItems);
        this.viewMultiChartModel = viewMultiChartModel;
    }


    public void notifyAdapter(List<ApplicationInformation> myLiveList, boolean isAdd) {
        if (!isAdd) {
            this.mMyLiveList.clear();
        }
            this.mMyLiveList.addAll(myLiveList);
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
        return mMyLiveList.size();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ApplicationInformation myLive = mMyLiveList.get(holder.getAdapterPosition());
        holder.bind(myLive, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewMultiChartModel.toggleSelected(myLive);
                notifyItemChanged(position);
            }
        }, mEditMode, viewMultiChartModel.isSelect(myLive));
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

        public void bind(ApplicationInformation myLive, View.OnClickListener clickListener, int mEditMode, boolean select) {
            mTvTitle.setText(myLive.getAppName());
            mTvSource.setText(myLive.getAppVersion());
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
