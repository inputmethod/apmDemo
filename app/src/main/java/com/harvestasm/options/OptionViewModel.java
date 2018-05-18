package com.harvestasm.options;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.WorkerThread;
import android.util.Log;

import com.harvestasm.apm.add.AddDataStorage;
import com.harvestasm.apm.base.BaseListViewModel;
import com.harvestasm.apm.base.pikcer.ActionModelInterface;
import com.harvestasm.apm.base.pikcer.ItemModelInterface;
import com.harvestasm.apm.sample.R;
import com.harvestasm.options.item.OptionItemModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import io.reactivex.functions.Consumer;

// todo: simplest implement without repository to store data item.
public class OptionViewModel extends BaseListViewModel<OptionCategoryModel> implements ActionModelInterface {
    private final static String TAG = OptionViewModel.class.getSimpleName();

    private final List<OptionCategoryModel> list = new ArrayList<>();

    public final MutableLiveData<OptionItemModel> clickItem = new MutableLiveData<>();

    @Override
    protected void resetForLoading() {
        super.resetForLoading();

        list.clear();
    }

    public void load(final boolean force) {
        resetForLoading();

        Callable<List<OptionCategoryModel>> callable = new Callable<List<OptionCategoryModel>>() {
            @Override
            public List<OptionCategoryModel> call() {
                Log.e(TAG, "Callable.call thread " + Thread.currentThread().getName());
                return buildOptionList();
            }
        };

        Consumer<List<OptionCategoryModel>> consumer = new Consumer<List<OptionCategoryModel>>() {
            @Override
            public void accept(List<OptionCategoryModel> modelList) {
                Log.e(TAG, "Consumer.accept thread " + Thread.currentThread().getName());
                list.addAll(modelList);
                onDataLoaded(list);
            }
        };

        AddDataStorage.get().runWithFlowable(callable, consumer);
    }

    @WorkerThread
    private List<OptionCategoryModel> buildOptionList() {
        List<OptionCategoryModel> modelList = new ArrayList<>();
        modelList.add(buildMemoryOption());
        modelList.add(buildTimeOption());
        modelList.add(buildElectricOption());
        modelList.add(buildFluencyOption());
        modelList.add(buildCpuOption());
        return modelList;
    }

    private OptionCategoryModel buildCpuOption() {
        String category = "CPU负载";
        Map<String, Integer> candidates = new HashMap<>();
        candidates.put("空闲状态", R.id.cpu_idle);
        candidates.put("中等规格状态", R.id.cpu_medium);
        candidates.put("中等长时间运行", R.id.cpu_long);
        return buildOption(category, candidates);
    }

    private OptionCategoryModel buildFluencyOption() {
        String category = "流畅度(帧率)";
        Map<String, Integer> candidates = new HashMap<>();
        candidates.put("Theme页滑动", R.id.frame_theme_slide);
        candidates.put("emoji页滑动", R.id.frame_emoji_slide);
        candidates.put("主键盘与符号键盘切换", R.id.frame_switch_kb_symbol);
        candidates.put("主键盘与emoji键盘切换", R.id.frame_switch_kb_emoji);
        candidates.put("键盘到设置页面", R.id.frame_switch_kb_setting);
        candidates.put("键盘打字弹泡", R.id.frame_kb_typing);
        return buildOption(category, candidates);
    }

    private OptionCategoryModel buildElectricOption() {
        String category = "电流功耗";
        Map<String, Integer> candidates = new HashMap<>();
        candidates.put("5分钟平均电流", R.id.electric_current_average);
        candidates.put("息屏电流", R.id.electric_current_offscreen);
        return buildOption(category, candidates);
    }

    private OptionCategoryModel buildTimeOption() {
        String category = "响应速度";
        Map<String, Integer> candidates = new HashMap<>();
        candidates.put("按键响应时间", R.id.action_add_key_time);
        candidates.put("键盘收起", R.id.action_add_keyboard_hide);
        return buildOption(category, candidates);
    }

    @WorkerThread
    private OptionCategoryModel buildMemoryOption() {
        String category = "存储空间";
        Map<String, Integer> candidates = new HashMap<>();
        candidates.put("内存使用", R.id.action_add_memory);
        return buildOption(category, candidates);
    }

    private OptionCategoryModel buildOption(String category, Map<String, Integer> candidates) {
        OptionCategoryModel model = new OptionCategoryModel();
        model.setTitle(category);
        List<OptionItemModel> itemList = new ArrayList<>();
        for (String name : candidates.keySet()) {
            OptionItemModel item = new OptionItemModel();
            item.setCategory(category);
            item.setName(name);
            item.setId(candidates.get(name));
            itemList.add(item);
        }
        model.setCandidates(itemList);
        return model;
    }

    @Override
    // 点击回调跳转到对应id输入页面
    public void toggleSelected(ItemModelInterface itemModel) {
        assert (itemModel instanceof  OptionItemModel);
        OptionItemModel model = (OptionItemModel) itemModel;
        clickItem.setValue(model);
    }

    @Override
    // 是否显示勾选的checkbox
    public boolean isSelect(ItemModelInterface itemModel) {
        // todo: 查询该option已经输入在缓存里过
        return false;
    }
}
