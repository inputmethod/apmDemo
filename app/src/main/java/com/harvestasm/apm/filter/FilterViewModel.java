package com.harvestasm.apm.filter;

import com.harvestasm.apm.base.BaseListViewModel;
import com.harvestasm.apm.base.pikcer.ActionModelInterface;
import com.harvestasm.apm.base.pikcer.ItemModelInterface;
import com.harvestasm.apm.browser.DataStorage;

import java.util.ArrayList;
import java.util.List;

// todo: simplest implement without repository to store data item.
public class FilterViewModel extends BaseListViewModel<FilterCategoryModel> implements ActionModelInterface {
    private final static String TAG = FilterViewModel.class.getSimpleName();

//    public final MutableLiveData<List<FilterCategoryModel>> items = new MutableLiveData<>();
//    public final MutableLiveData<Boolean> refreshState = new MutableLiveData<>();
//    public final MutableLiveData<Integer> networkState = new MutableLiveData<>();

    private final List<FilterCategoryModel> list = new ArrayList<>();

    @Override
    protected void resetForLoading() {
        super.resetForLoading();
//        refreshState.setValue(true);
//        networkState.postValue(0);

        list.clear();
    }

//    private void onDataLoaded(List<FilterCategoryModel> list) {
//        items.setValue(list);
//
//        refreshState.setValue(false);
//        networkState.postValue(0);
//
//    }

    public void load(final boolean force) {
        resetForLoading();

        // FilterViewModel在BrowserViewModel完成加载后显示，数据已经加载在DataStorage，直接读
        checkResult();
    }

    private void checkResult() {
        list.addAll(DataStorage.get().queryFilterList());

        // final result list.
        onDataLoaded(list);
    }

    @Override
    public void toggleSelected(ItemModelInterface itemModel) {
        DataStorage.get().toggleSelected(itemModel.getSubTitle(), itemModel.getTitle());
    }

    @Override
    public boolean isSelect(ItemModelInterface itemModel) {
        return DataStorage.get().isSelect(itemModel.getSubTitle(), itemModel.getTitle());
    }
}
