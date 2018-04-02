package com.harvestasm.apm.reporter;

import com.harvestasm.apm.repository.model.ApmDeviceMicsItem;
import com.harvestasm.apm.repository.model.ApmSourceConnect;
import com.harvestasm.apm.repository.model.ApmSourceData;
import com.harvestasm.apm.repository.model.search.ApmBaseSearchResponse;
import com.harvestasm.apm.repository.model.search.ApmBaseSearchResponse.ApmBaseUnit;
import com.harvestasm.apm.repository.model.search.ApmCommonSearchResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by yangfeng on 2018/3/20.
 */

public class SearchDataParser {
    private static final String INDEX = "mobile";
    private static final String TYPE_DATA = "data";
    private static final String TYPE_CONNECT = "connect";
    private static final String TAG = SearchDataParser.class.getSimpleName();

    public static SearchResult parse(ApmCommonSearchResponse apmData) {
        Map<String, List<String>> unknownDataMap = new HashMap<>();

        Map<String, List<ApmBaseUnit>> idAllIndexMap = new HashMap<>();
        Map<String, List<ApmBaseUnit>> idConnectIndexMap = new HashMap<>();
        Map<String, List<ApmBaseUnit>> idDataIndexMap = new HashMap<>();

        Map<String, List<ApmBaseUnit>> deviceIdAllIndexMap = new HashMap<>();
        Map<String, List<ApmBaseUnit>> deviceIdConnectIndexMap = new HashMap<>();
        Map<String, List<ApmBaseUnit>> deviceIdDataIndexMap = new HashMap<>();
        Map<String, List<ApmBaseUnit>> timestampAllIndexMap = new HashMap<>();
        Map<String, List<ApmBaseUnit>> timestampConnectIndexMap = new HashMap<>();
        Map<String, List<ApmBaseUnit>> timestampDataIndexMap = new HashMap<>();

        SearchResult searchResult = new SearchResult();
        for (ApmBaseUnit unit : apmData.getHits().getHits()) {
            String index = unit.get_index();
            String type = unit.get_type();

            String id = unit.get_id();
            int score = unit.get_score();

            ApmBaseSearchResponse.ApmBaseSourceType sourceTypeData = unit.get_source();
            addToMap(deviceIdAllIndexMap, unit, sourceTypeData.getDeviceId());
            addToMap(timestampAllIndexMap, unit, sourceTypeData.getTimestamp());

            addToMap(idAllIndexMap, unit, id);

            if (isEquals(index, INDEX)) {
                if (isEquals(type, TYPE_CONNECT)) {
                    addToMap(idConnectIndexMap, unit, id);
                    addToMap(deviceIdConnectIndexMap, unit, sourceTypeData.getDeviceId());
                    addToMap(timestampConnectIndexMap, unit, sourceTypeData.getTimestamp());
                } else if (isEquals(type, TYPE_DATA)) {
                    addToMap(idDataIndexMap, unit, id);
                    addToMap(deviceIdDataIndexMap, unit, sourceTypeData.getDeviceId());
                    addToMap(timestampDataIndexMap, unit, sourceTypeData.getTimestamp());
                } else {
                    handleUnknownType(unknownDataMap, index, type);
                }
            } else {
                handleUnknownType(unknownDataMap, index, type);
            }
        }

        return searchResult;
    }

    public static void parseDataSummary(ApmBaseSearchResponse<ApmSourceData> result) {

    }

    public static void parseConnectionSummary(ApmBaseSearchResponse<ApmSourceConnect> apmConnect) {
        Set<Integer> appLengthSet = new HashSet<>();
        Set<Integer> deviceLengthSet = new HashSet<>();
        Set<Integer> devicemicsLengthSet = new HashSet<>();

        HashMap<String, List<ApmBaseUnit<ApmSourceConnect>>> deviceIdIndexMap = new HashMap<>();
        HashMap<String, List<ApmBaseUnit<ApmSourceConnect>>> timestampIndexMap = new HashMap<>();
        HashMap<String, List<ApmBaseUnit<ApmSourceConnect>>> appIndexMap = new HashMap<>();
        HashMap<String, List<ApmBaseUnit<ApmSourceConnect>>> deviceIndexMap = new HashMap<>();
        HashMap<ApmDeviceMicsItem, List<ApmBaseUnit<ApmSourceConnect>>> deviceMicsItemListHashMap = new HashMap<>();

        for (ApmBaseUnit<ApmSourceConnect> unit : apmConnect.getHits().getHits()) {
            ApmSourceConnect ctc = unit.get_source();

            String deviceId = ctc.getDeviceId();
            String timestamp = ctc.getTimestamp();

            List<String> apps = ctc.getApp();
            List<String> devices = ctc.getDevice();
            List<ApmDeviceMicsItem> deviceMicsItems = ctc.getDevicemics();

            appLengthSet.add(apps.size());
            deviceLengthSet.add(devices.size());
            devicemicsLengthSet.add(deviceMicsItems.size());

            addToMap(deviceIdIndexMap, unit, deviceId);
            addToMap(timestampIndexMap, unit, timestamp);

            addToMap(appIndexMap, unit, apps.toString());
            addToMap(deviceIndexMap, unit, devices.toString());
            addPartToMap(deviceMicsItemListHashMap, unit, deviceMicsItems);
        }

        apmConnect.isTimed_out();
    }

    private static<K, V> void addPartToMap(Map<K, List<V>> indexMap, V unit, List<K> keyList) {
        for (K key : keyList) {
            addToMap(indexMap, unit, key);
        }
    }

    private static<K, V> void addToMap(Map<K, List<V>> indexMap, V value, K key) {
        List<V> valueList = indexMap.get(key);
        if (null == valueList) {
            valueList = new ArrayList<>();
            indexMap.put(key, valueList);
        }
        valueList.add(value);
    }

    private static boolean isEquals(String text, String other) {
        return null == text && null == other || null != text && text.equals(other);
    }

    private static void handleUnknownType(Map<String, List<String>> unknownData, String index, String type) {
        addToMap(unknownData, type, index);
    }
}
