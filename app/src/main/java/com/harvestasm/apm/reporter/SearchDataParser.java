package com.harvestasm.apm.reporter;

import com.harvestasm.apm.repository.model.ApmBaseSearchResponse;
import com.harvestasm.apm.repository.model.ApmConnectSearchResponse;
import com.harvestasm.apm.repository.model.ApmDeviceMicsItem;

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

    public static SearchResult parse(ApmBaseSearchResponse apmData) {
        Map<String, List<String>> unknownData = new HashMap<>();
        SearchResult searchResult = new SearchResult();
//        for (ApmBaseUnit unit : apmData.getHits().getHits()) {
//            String index = unit.get_index();
//            String type = unit.get_type();
//            String id = unit.get_id();
//            int score = unit.get_score();
//
//            if (isEquals(index, INDEX)) {
//                if (isEquals(type, TYPE_CONNECT)) {
//                    // todo: parse connect result
//                } else if (isEquals(type, TYPE_DATA)) {
//                    ApmDataSearchResponse.SourceTypeData sourceTypeData = unit.get_source();
//                    for (ApmTransactionItem item : sourceTypeData.getTransaction()) {
//
//                    }
//                } else {
//                    handleUnknownType(unknownData, index, type);
//                }
//            } else {
//                handleUnknownType(unknownData, index, type);
//            }
//        }
        return searchResult;
    }

    public static void parseConnectionSummary(ApmConnectSearchResponse apmConnect) {
        Set<Integer> appLengthSet = new HashSet<>();
        Set<Integer> deviceLengthSet = new HashSet<>();
        Set<Integer> devicemicsLengthSet = new HashSet<>();

        HashMap<String, List<ApmConnectSearchResponse.ConnectUnit>> deviceIdIndexMap = new HashMap<>();
        HashMap<String, List<ApmConnectSearchResponse.ConnectUnit>> timestampIndexMap = new HashMap<>();
        HashMap<String, List<ApmConnectSearchResponse.ConnectUnit>> appIndexMap = new HashMap<>();
        HashMap<String, List<ApmConnectSearchResponse.ConnectUnit>> deviceIndexMap = new HashMap<>();
        HashMap<ApmDeviceMicsItem, List<ApmConnectSearchResponse.ConnectUnit>> deviceMicsItemListHashMap = new HashMap<>();

        for (ApmConnectSearchResponse.ConnectUnit unit : apmConnect.getHits().getHits()) {
            ApmConnectSearchResponse.SourceTypeConnect ctc = unit.get_source();

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
    }

    private static<K, V> void addPartToMap(HashMap<K, List<V>> indexMap, V unit, List<K> keyList) {
        for (K key : keyList) {
            addToMap(indexMap, unit, key);
        }
    }

    private static<K, V> void addToMap(HashMap<K, List<V>> indexMap, V unit, K key) {
        List<V> valueList = indexMap.get(key);
        if (null == valueList) {
            valueList = new ArrayList<>();
            indexMap.put(key, valueList);
        }
        valueList.add(unit);
    }

    private static boolean isEquals(String text, String other) {
        return null == text && null == other || null != text && text.equals(other);
    }

    private static void handleUnknownType(Map<String, List<String>> unknownData, String index, String type) {
        final List<String> typeList;
        if (unknownData.containsKey(index)) {
            typeList = unknownData.get(index);
        } else {
            typeList = new ArrayList<>();
            unknownData.put(index, typeList);
        }
        typeList.add(type);
    }
}
