package com.harvestasm.apm.imepicker;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.harvestasm.apm.home.HomeDeviceItem;
import com.harvestasm.apm.sample.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/** adapter that supports 3 different item types */
class ImePickerAdapter extends RecyclerView.Adapter<ImePickerAdapter.ViewHolder> {
    private static final int MYLIVE_MODE_CHECK = 0;

    int mEditMode = MYLIVE_MODE_CHECK;

    private int secret = 0;
    private String title = "";
    private Context context;
    private List<HomeDeviceItem.AppItem> mMyLiveList;
    private OnItemClickListener mOnItemClickListener;

    private final ImePickerViewModel viewMultiChartModel;

    public ImePickerAdapter(Context context, List<HomeDeviceItem.AppItem> chartItems, ImePickerViewModel viewMultiChartModel) {
        this.context = context;
        mMyLiveList = chartItems;
        this.viewMultiChartModel = viewMultiChartModel;
    }


    public void notifyAdapter(List<HomeDeviceItem.AppItem> myLiveList, boolean isAdd) {
        if (!isAdd) {
            this.mMyLiveList = myLiveList;
        } else {
            this.mMyLiveList.addAll(myLiveList);
        }
        notifyDataSetChanged();
    }

    public List<HomeDeviceItem.AppItem> getMyLiveList() {
        if (mMyLiveList == null) {
            mMyLiveList = new ArrayList<>();
        }
        return mMyLiveList;
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
        final HomeDeviceItem.AppItem myLive = mMyLiveList.get(holder.getAdapterPosition());
        holder.bind(myLive, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClickListener(position, mMyLiveList);
            }
        }, mEditMode, viewMultiChartModel.isSelect(myLive));
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setNetworkState(Integer integer) {

    }

    public interface OnItemClickListener {
        void onItemClickListener(int pos,List<HomeDeviceItem.AppItem> myLiveList);
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

        public void bind(HomeDeviceItem.AppItem myLive, View.OnClickListener clickListener, int mEditMode, boolean select) {
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

    public static class MyLiveList extends HomeDeviceItem.AppItem {
        public boolean isSelect() {
            // todo: check and uncheck
            return false;
        }
    }
}
