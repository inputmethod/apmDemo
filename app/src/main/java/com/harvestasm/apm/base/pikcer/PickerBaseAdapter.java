package com.harvestasm.apm.base.pikcer;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.harvestasm.apm.sample.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/***
 * PickerBaseAdapter源于这个简单的开源工程：https://github.com/guohaosir/RecyclerDemo.git，它封装了
 * RecyclerView的非编辑状态和编辑状态下的选中和非选中操作，具体的派生类只需要实现两个接口：
 * 1. ItemModelInterface接口查询标题和副标题文本。 todo: 文本的图标
 * 2. ActionModelInterface接口查询一个item是否选中，和响应点击事件修改选中状态
 * 然后几行简单代码就派生出具体类，如，FilterItemAdapter，ImePickerAdapter
 *
 */

/** adapter that supports 3 different item types */
public class PickerBaseAdapter<T extends ItemModelInterface> extends RecyclerView.Adapter<PickerBaseAdapter.ViewHolder> {
    private static final int MYLIVE_MODE_CHECK = 0;

    int mEditMode = MYLIVE_MODE_CHECK;

    private final List<T> itemList = new ArrayList<>();

    private final ActionModelInterface<T> viewMultiChartModel;

    public PickerBaseAdapter(List<T> itemList, ActionModelInterface pickerModel) {
        this.itemList.addAll(itemList);
        this.viewMultiChartModel = pickerModel;
    }


    public void notifyAdapter(List<T> itemList, boolean isAdd) {
        if (!isAdd) {
            this.itemList.clear();
        }
            this.itemList.addAll(itemList);
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
        return itemList.size();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final T item = itemList.get(holder.getAdapterPosition());
        holder.bind(item, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewMultiChartModel.toggleSelected(item);
                notifyItemChanged(position);
            }
        }, mEditMode, viewMultiChartModel.isSelect(item));
    }

    public void setNetworkState(Integer integer) {

    }

    public void setEditMode(int editMode) {
        mEditMode = editMode;
        notifyDataSetChanged();
    }

    public static class ViewHolder<T extends ItemModelInterface> extends RecyclerView.ViewHolder {
//        @BindView(R.id.root_view)
//        RelativeLayout mRootView;
        @BindView(R.id.check_box)
        ImageView checkboxIv;
        @BindView(R.id.avatar_img)
        ImageView avatarIv;

        @BindView(R.id.tv_title)
        TextView titleTv;
        @BindView(R.id.tv_sub_title)
        TextView subTitleTv;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }

        public void bind(T item, View.OnClickListener clickListener, int mEditMode, boolean select) {
            titleTv.setText(item.getTitle());
            subTitleTv.setText(item.getSubTitle());

            if (null == item.getIcon()) {
                avatarIv.setImageResource(R.mipmap.ic_launcher);
            } else {
                avatarIv.setImageDrawable(item.getIcon());
            }

            if (mEditMode == MYLIVE_MODE_CHECK) {
                checkboxIv.setVisibility(View.GONE);
            } else {
                checkboxIv.setVisibility(View.VISIBLE);

                if (select) {
                    checkboxIv.setImageResource(R.mipmap.ic_checked);
                } else {
                    checkboxIv.setImageResource(R.mipmap.ic_uncheck);
                }
            }
            itemView.setOnClickListener(clickListener);
        }
    }
}
