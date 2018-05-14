package com.harvestasm.apm.browser;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.harvestasm.apm.filter.FilterCategoryModel;
import com.harvestasm.apm.filter.item.FilterItemModel;
import com.harvestasm.apm.reporter.ApmConnectSourceIndex;
import com.harvestasm.apm.reporter.ApmDataSourceIndex;
import com.harvestasm.apm.reporter.ApmSourceGroup;
import com.harvestasm.apm.repository.ApmRepository;
import com.harvestasm.apm.repository.model.ApmActivityItem;
import com.harvestasm.apm.repository.model.ApmSourceConnect;
import com.harvestasm.apm.repository.model.ApmSourceData;
import com.harvestasm.apm.repository.model.search.ApmBaseUnit;
import com.harvestasm.apm.repository.model.search.ApmConnectSearchResponse;
import com.harvestasm.apm.repository.model.search.ApmDataSearchResponse;
import com.harvestasm.apm.utils.ApmRepositoryHelper;
import com.harvestasm.apm.utils.SimpleFlowableService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import typany.apm.com.google.gson.Gson;

public class DataStorage {
    private static final String TAG = DataStorage.class.getSimpleName();

    private static DataStorage _instance;
    // 原始的和结构化的连接设备和上报数据
    private ApmConnectSearchResponse connectResponse;
    private ApmDataSearchResponse dataResponse;

    private ApmConnectSourceIndex connectSourceIndex;
    private ApmDataSourceIndex dataSourceIndex;
    private List<ApmSourceGroup> deviceGroupList;

    public final MutableLiveData<Integer> currentState = new MutableLiveData<>();

    private final SimpleFlowableService simpleFlowableService = new SimpleFlowableService();
    public Disposable runWithFlowable(Callable callable, Consumer consumer) {
        return simpleFlowableService.runWithFlowable(callable, consumer);
    }

    // 从原始response数据里构建出数据相关的联的设备列表，应用版本列表，数据选项列表，和各次上报时间列表。

    public static DataStorage get() {
        if (null == _instance) {
            _instance = new DataStorage();
        }
        return _instance;
    }

    public ApmConnectSearchResponse getConnectResponse() {
        return connectResponse;
    }

    public void setConnectResponse(ApmConnectSearchResponse connectResponse) {
        this.connectResponse = connectResponse;
        connectSourceIndex = new ApmConnectSourceIndex(connectResponse);
        updateData();
    }

    public ApmDataSearchResponse getDataResponse() {
        return dataResponse;
    }

    public void setDataResponse(ApmDataSearchResponse dataResponse) {
        this.dataResponse = dataResponse;
        dataSourceIndex = new ApmDataSourceIndex(dataResponse);
        updateData();
    }

    private static final String CATEGORY_DEVICE = "Devices";
    private static final String CATEGORY_APP = "Apps";
    private static final String CATEGORY_TS = "TimeStamp";
    private static final String CATEGORY_OPTION = "Options";
    private static final String CATEGORY_DATA_APP = "DataApps";

    private static final String CATEGORY_TRANSACTION_URL = "URL";

    private void updateData() {
        if (null == dataSourceIndex || null == connectSourceIndex) {
            Log.i(TAG, "updateData, skip and wait until all connect and data loaded.");
        } else {
            resetFilterCache();

            deviceGroupList = ApmSourceGroup.parseSourceGroup(dataSourceIndex, connectSourceIndex);

            // 添加measurement的filters
            AddList(CATEGORY_DEVICE, getDeviceList());
            AddList(CATEGORY_APP, getAppList());
            AddList(CATEGORY_TS, getTimeStampSet());
            AddList(CATEGORY_OPTION, getOptionSet());
            AddList(CATEGORY_DATA_APP, getDataAppList());

            // 增加transaction的filters
            AddList(CATEGORY_TRANSACTION_URL, getTransactionUrlList());

            // todo: 统计出所有设备列表，可显示的app及版本列表和可显示数据选项列表
            // 同一台设备x同一app版本上多次数据的过滤，用timestamp??
            // 连接设备列表
            Log.v(TAG, "/n  updateData, device size: " + connectSourceIndex.getDeviceIndexMap().size());
            for (String key : connectSourceIndex.getDeviceIndexMap().keySet()) {
                Log.v(TAG, "    updateData, " + key);
            }

            // 连接应用列表
            Log.v(TAG, "/n  updateData, app(version) size: " + connectSourceIndex.getAppIndexMap().size());
            for (String key : connectSourceIndex.getAppIndexMap().keySet()) {
                Log.v(TAG, "    updateData, " + key);
            }

            // 上报时间戳列表
            Log.v(TAG, "/n  updateData, time stamp size: " + connectSourceIndex.getAppIndexMap().size());
            for (String key : connectSourceIndex.getTimestampIndexMap().keySet()) {
                Log.v(TAG, "    updateData, " + key);
            }

            // 数据显示选项列表
            Log.v(TAG, "/n  updateData, data option size: " + dataSourceIndex.getMeasureNameIndexMap().size());
            for (String key : dataSourceIndex.getMeasureNameIndexMap().keySet()) {
                Log.v(TAG, "    updateData, " + key);
            }

            // 数据关联app(版本)列表
            Log.v(TAG, "/n  updateData, data app size: " + dataSourceIndex.getAppIndexMap().size());
            for (String key : dataSourceIndex.getAppIndexMap().keySet()) {
                Log.v(TAG, "    updateData, " + key);
            }
        }
    }

    private void resetFilterCache() {
        filterOptionMap.clear();
        filterOptionList.clear();
    }

    @Nullable
    public Map<String, List<ApmBaseUnit<ApmSourceData>>> queryTransaction() {
        if (null == dataResponse) {
            Log.i(TAG, "queryTransaction, return empty map while it may not be loaded completely.");
            return null;
        }

        return null == dataSourceIndex ? Collections.<String, List<ApmBaseUnit<ApmSourceData>>>emptyMap()
                : dataSourceIndex.getTransactionUrlIndexMap();
    }
    @Nullable
    public Map<String, List<ApmBaseUnit<ApmSourceData>>> queryByOption() {
        if (null == dataResponse) {
            Log.i(TAG, "queryByOption, return empty map while it may not be loaded completely.");
            return null;
        }

        return null == dataSourceIndex ? Collections.<String, List<ApmBaseUnit<ApmSourceData>>>emptyMap()
                : dataSourceIndex.getMeasureNameIndexMap();
    }


    public Set<String> getDeviceList() {
        if (null == connectSourceIndex) {
            return Collections.emptySet();
        }

        return connectSourceIndex.getDeviceIndexMap().keySet();
    }

    public Set<String> getAppList() {
        if (null == connectSourceIndex) {
            return Collections.emptySet();
        }
        return connectSourceIndex.getAppIndexMap().keySet();
    }

    public Set<String> getTimeStampSet() {
        if (null == connectSourceIndex) {
            return Collections.emptySet();
        }
        return connectSourceIndex.getTimestampIndexMap().keySet();
    }

    public Set<String> getOptionSet() {
        if (null == dataSourceIndex) {
            return Collections.emptySet();
        }

        return dataSourceIndex.getMeasureNameIndexMap().keySet();
    }

    public Set<String> getDataAppList() {
        if (null == dataSourceIndex) {
            return Collections.emptySet();
        }
        return dataSourceIndex.getAppIndexMap().keySet();
    }

    public Set<String> getTransactionUrlList() {
        if (null == dataSourceIndex) {
            return Collections.emptySet();
        }
        return dataSourceIndex.getTransactionUrlIndexMap().keySet();
    }

    private final Map<String, Set<String>> filterOptionMap = new HashMap<>();
    private final List<FilterCategoryModel> filterOptionList = new ArrayList<>();

    private void AddList(String label, Set<String> candidateList) {
        Set<String> set = filterOptionMap.get(label);
        if (null == set) {
            set = new HashSet<>();
        }
        set.addAll(candidateList);
        filterOptionMap.put(label, set);

        FilterCategoryModel itemModel = new FilterCategoryModel();
        itemModel.setTitle(label);
        itemModel.setCandidates(buildItemModels(label, set));
        filterOptionList.add(itemModel);
    }

    public Collection<? extends FilterCategoryModel> queryFilterList() {
        return filterOptionList;
//        List<FilterCategoryModel> list = new ArrayList<>();
//        for (String category : filterOptionMap.keySet()) {
//            FilterCategoryModel itemModel = new FilterCategoryModel();
//            itemModel.setTitle(category);
//            itemModel.setCandidates(buildItemModels(category, filterOptionMap.get(category)));
//            list.add(itemModel);
//        }
//        return list;
    }

    @NonNull
    private List<FilterItemModel> buildItemModels(String category, Set<String> strings) {
        if (null == strings || strings.isEmpty()) {
            return Collections.emptyList();
        }

        List<FilterItemModel> modelList = new ArrayList<>();
        for (String s : strings) {
            FilterItemModel model = new FilterItemModel();
            model.setCategory(category);
            model.setName(s);
            modelList.add(model);
        }
        return modelList;
    }

    public boolean isSelect(String category, String name) {
        if (null != filterOptionMap.get(category)) {
            return filterOptionMap.get(category).contains(name);
        }
        return false;
    }

    public void toggleSelected(String category, String name) {
        if (null != filterOptionMap && filterOptionMap.containsKey(category)) {
            Set<String> set = filterOptionMap.get(category);
            if (set.contains(name)) {
                set.remove(name);
            } else {
                set.add(name);
            }
            filterOptionMap.put(category, set);
        }
    }

    private @NonNull Set<String> getFilter(String category) {
        if (null == filterOptionMap) {
            return Collections.emptySet();
        }

        return filterOptionMap.get(category);
    }

    public @NonNull Set<String> getFilterOptions() {
        return getFilter(CATEGORY_OPTION);
    }

    public Set<String> getFilterApps() {
        return getFilter(CATEGORY_APP);
    }

    // 读出过滤的设备，在connectionIndex里找这些设备的ID.
    public Set<String> getFilterDeviceIds() {
        HashMap<String, List<ApmBaseUnit<ApmSourceConnect>>> map =  null == connectSourceIndex ?
                null : connectSourceIndex.getDeviceIndexMap();
        if (null == map) {
            return Collections.emptySet();
        }

        Set<String> filterIds = new HashSet<>();
        for (String d : getFilter(CATEGORY_DEVICE)) {
            List<ApmBaseUnit<ApmSourceConnect>> unitList = map.get(d);
            if (null == unitList) {
                // what's happen here?
                continue;
            }
            for (ApmBaseUnit<ApmSourceConnect> connect : unitList) {
                filterIds.add(connect.get_source().getDeviceId());
            }
        }
        return filterIds;
    }

    private final ApmRepository repository = new ApmRepository();
    public void doLoadTask(ApmRepositoryHelper.CallBack callBack, ApmRepositoryHelper.RefreshInterface refreshInterface, boolean force) {
        if (force || null == connectResponse && null == dataResponse) {
            resetFilterCache();
            if (usedAutoChart) {
                ApmRepositoryHelper.doLoadTask(repository.mobileConnectSearch(), repository.mobileDataSearch(), callBack);
            } else {
                ApmRepositoryHelper.doLoadTask(repository.apmTestConnectSearch(), repository.apmTestDataSearch(), callBack);
            }
        } else {
            refreshInterface.onRefresh();
        }
    }

    private boolean usedAutoChart;

    public boolean isUsedAutoChart() {
        return usedAutoChart;
    }

    public void useManualMeasurements() {
        if (usedAutoChart) {
            clearToReload();
        }

        usedAutoChart = false;
    }

    public void useAutoMeasurements() {
        if (!usedAutoChart) {
            clearToReload();
        }
        usedAutoChart = true;
    }

    private void clearToReload() {
        connectResponse = null;
        dataResponse = null;
        resetFilterCache();
        dataSourceIndex = null;
        connectSourceIndex = null;
    }

    public Set<String> getTransactionFilterOptions() {
        return getFilter(CATEGORY_TRANSACTION_URL);
    }


    @Nullable
    public Map<String, List<ApmActivityItem.VitalUnit>> buildApplicationMemoryMap() {
        if (null == dataResponse) {
            Log.i(TAG, "queryTransaction, return empty map while it may not be loaded completely.");
            return null;
        }

        if (null != dataSourceIndex) {
            Map<String, List<ApmActivityItem.VitalUnit>> result = new HashMap<>();
            Map<String, List<ApmBaseUnit<ApmSourceData>>> dataMap = dataSourceIndex.getActivityVitalsIndexMap();
            for (String key : dataMap.keySet()) {
                for (ApmBaseUnit<ApmSourceData> unit : dataMap.get(key)) {
                    ApmSourceData sourceData = unit.get_source();
                    if (null != sourceData) {
                        List<ApmActivityItem> activityList = sourceData.getActivity();
                        if (null != activityList) {
                            for (ApmActivityItem item : activityList) {
                                String mapKey = item.getDisplayName();
                                String str = item.getVitals();
                                ApmActivityItem.Vitals[] vitals = new Gson().fromJson(str, ApmActivityItem.Vitals[].class);
//                                List<ApmActivityItem.Vitals> vitals = item.getVitals();
                                if (null != vitals) {
                                    for (ApmActivityItem.Vitals v : vitals) {
                                        List<ApmActivityItem.VitalUnit> memory = v.getMemory();
                                        if (null != memory && memory.size() == 2) {
                                            List<ApmActivityItem.VitalUnit> unitList = result.get(mapKey);
                                            if (null == unitList) {
                                                unitList = new ArrayList<>();
                                            }
                                            unitList.addAll(memory);
                                            result.put(mapKey, unitList);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return result;
        }

        return Collections.emptyMap();
    }
}
