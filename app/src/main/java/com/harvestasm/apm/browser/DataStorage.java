package com.harvestasm.apm.browser;

import android.support.annotation.NonNull;
import android.util.Log;

import com.harvestasm.apm.reporter.ApmConnectSourceIndex;
import com.harvestasm.apm.reporter.ApmDataSourceIndex;
import com.harvestasm.apm.reporter.ApmSourceGroup;
import com.harvestasm.apm.repository.model.ApmSourceData;
import com.harvestasm.apm.repository.model.search.ApmBaseUnit;
import com.harvestasm.apm.repository.model.search.ApmConnectSearchResponse;
import com.harvestasm.apm.repository.model.search.ApmDataSearchResponse;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DataStorage {
    private static final String TAG = DataStorage.class.getSimpleName();

    private static DataStorage _instance;
    // 原始的和结构化的连接设备和上报数据
    private ApmConnectSearchResponse connectResponse;
    private ApmDataSearchResponse dataResponse;

    private ApmConnectSourceIndex connectSourceIndex;
    private ApmDataSourceIndex dataSourceIndex;
    private List<ApmSourceGroup> deviceGroupList;

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

    private void updateData() {
        if (null == dataSourceIndex || null == connectSourceIndex) {
            Log.i(TAG, "updateData, skip and wait until all connect and data loaded.");
        } else {
            deviceGroupList = ApmSourceGroup.parseSourceGroup(dataSourceIndex, connectSourceIndex);
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

    @NonNull
    public Map<String, List<ApmBaseUnit<ApmSourceData>>> queryByOption() {
        if (null == dataSourceIndex) {
            Log.i(TAG, "queryByOption, return empty map while it may not be loaded completely.");
            return Collections.emptyMap();
        }

        return dataSourceIndex.getMeasureNameIndexMap();
    }
}
