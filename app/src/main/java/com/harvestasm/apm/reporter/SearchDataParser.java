package com.harvestasm.apm.reporter;

import com.harvestasm.apm.repository.model.ApmConnectSearchResponse;
import com.harvestasm.apm.repository.model.ApmDataSearchResponse;
import com.harvestasm.apm.repository.model.ApmTransactionItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yangfeng on 2018/3/20.
 */

public class SearchDataParser {
    private static final String INDEX = "mobile";
    private static final String TYPE_DATA = "data";
    private static final String TYPE_CONNECT = "connect";

    public static SearchResult parse(ApmDataSearchResponse apmData, ApmConnectSearchResponse apmConnect) {
        parseDeviceId(apmData, apmConnect);
        Map<String, List<String>> unknownData = new HashMap<>();
        SearchResult searchResult = new SearchResult();
        for (ApmDataSearchResponse.DataUnit unit : apmData.getHits().getHits()) {
            String index = unit.get_index();
            String type = unit.get_type();
            String id = unit.get_id();
            int score = unit.get_score();

            if (isEquals(index, INDEX)) {
                if (isEquals(type, TYPE_CONNECT)) {
                    // todo: parse connect result
                } else if (isEquals(type, TYPE_DATA)) {
                    ApmDataSearchResponse.SourceTypeData sourceTypeData = unit.get_source();
                    for (ApmTransactionItem item : sourceTypeData.getTransaction()) {

                    }
                } else {
                    handleUnknownType(unknownData, index, type);
                }
            } else {
                handleUnknownType(unknownData, index, type);
            }
        }
        return searchResult;
    }

    private static void parseDeviceId(ApmDataSearchResponse apmData, ApmConnectSearchResponse apmConnect) {
        HashMap<String, List<ApmConnectSearchResponse.ConnectUnit>> connectMap = new HashMap<>();
        for (ApmConnectSearchResponse.ConnectUnit unit : apmConnect.getHits().getHits()) {
            String deviceId = unit.get_source().getDeviceId();
            List<ApmConnectSearchResponse.ConnectUnit> connectUnitList = connectMap.get(deviceId);
            if (null == connectUnitList) {
                connectUnitList = new ArrayList<>();
                connectMap.put(deviceId, connectUnitList);
            }
            connectUnitList.add(unit);
        }

        HashMap<String, List<ApmDataSearchResponse.DataUnit>> dataMap = new HashMap<>();
        for (ApmDataSearchResponse.DataUnit unit : apmData.getHits().getHits()) {
            String deviceId = unit.get_source().getDeviceId();
            List<ApmDataSearchResponse.DataUnit> dataUnitList = dataMap.get(deviceId);
            if (null == dataUnitList) {
                dataUnitList = new ArrayList<>();
                dataMap.put(deviceId, dataUnitList);
            }
            dataUnitList.add(unit);
        }

        int size = connectMap.keySet().size();
        size = dataMap.keySet().size();
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
