package com.harvestasm.options;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.harvestasm.apm.add.AddSubOptionActivity;
import com.harvestasm.apm.preview.PreviewActivity;
import com.harvestasm.apm.sample.R;
import com.harvestasm.base.viewholder.BaseSwipeRefreshFragment;
import com.harvestasm.options.item.OptionItemModel;

import java.util.List;

/**
 * 替代SetupOptionsFragment, 用类似FilterFragment的样式显示数据录入的2级选项列表，
 * 存储空间
 *     内存占用
 * 响应速度
 *     按键响应时间
 *     键盘收起时间
 * 电流功耗
 *     5分钟平均
 *     息屏电流
 * 流畅度(帧率)
 *     Theme页面滑动
 *     Emoji页面滑动
 *     主键盘切符号键盘
 *     主键盘切Emoji键盘
 *     键盘跳设置页面
 *     键盘打字
 * CPU功耗
 *     空闲状态
 *     中等规模
 *     长时运行
 */
public class OptionFragment extends BaseSwipeRefreshFragment<OptionCategoryModel, RecyclerView> {
    private static final String TAG = OptionFragment.class.getSimpleName();

    private OptionViewModel optionViewModel;
    private OptionAdapter optionAdapter;

    @Override
    protected @LayoutRes int getCollectionLayoutResourceId() {
        return R.layout.including_recyclerview;
    }

    protected void refreshChangedData(@NonNull RecyclerView recyclerView, @NonNull List<OptionCategoryModel> chartItems) {
        if (null == optionAdapter) {
            optionAdapter = new OptionAdapter(chartItems, optionViewModel);
            recyclerView.setAdapter(optionAdapter);
        } else {
            optionAdapter.notifyAdapter(chartItems, false);
        }
        setHasOptionsMenu(true);
    }

    protected void doLoadingTask(boolean force) {
        optionViewModel.load(force);
    }

    @Override
    protected LiveData<List<OptionCategoryModel>> setViewModel() {
        optionViewModel = newViewModel(OptionViewModel.class);

        optionViewModel.networkState.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                Log.d(TAG, "networking value " + integer);
                if (null != optionAdapter) {
                    optionAdapter.setNetworkState(integer);
                }
            }
        });

        optionViewModel.clickItem.observe(this, new Observer<OptionItemModel>() {
            @Override
            public void onChanged(@Nullable OptionItemModel optionItemModel) {
                int viewId = optionItemModel.getId();
                String title = optionItemModel.getTitle();
                AddSubOptionActivity.startByAction(getContext(), viewId, title);
            }
        });

        startLoading(optionViewModel.refreshState);

        return optionViewModel.items;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_preview, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_next) {
            PreviewActivity.start(getActivity());
        } else {
            return super.onOptionsItemSelected(item);
        }

        return true;
    }
}
