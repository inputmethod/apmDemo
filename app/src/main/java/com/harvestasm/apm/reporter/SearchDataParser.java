package com.harvestasm.apm.reporter;

import com.harvestasm.apm.repository.model.ApmSourceConnect;
import com.harvestasm.apm.repository.model.ApmSourceData;
import com.harvestasm.apm.repository.model.search.ApmBaseSearchResponse;
import com.harvestasm.apm.repository.model.search.ApmBaseSourceType;
import com.harvestasm.apm.repository.model.search.ApmBaseUnit;
import com.harvestasm.apm.repository.model.search.ApmCommonSearchResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by yangfeng on 2018/3/20.
 */

public class SearchDataParser extends ApmBaseSourceIndex {
    private static final String INDEX = "mobile";
    private static final String TYPE_DATA = "data";
    private static final String TYPE_CONNECT = "connect";
    private static final String TAG = SearchDataParser.class.getSimpleName();

    public <T extends ApmBaseSourceType> SearchDataParser() {
        super(null);
    }

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

            ApmBaseSourceType sourceTypeData = unit.get_source();
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

    public static void parseDataSummary(ApmBaseSearchResponse<ApmSourceData> apmData) {
        ApmDataSourceIndex dataSourceIndex = new ApmDataSourceIndex(apmData);
    }

    public static void parseConnectionSummary(ApmBaseSearchResponse<ApmSourceConnect> apmConnect) {
        ApmConnectSourceIndex connectSourceIndex = new ApmConnectSourceIndex(apmConnect);
    }

    private static void handleUnknownType(Map<String, List<String>> unknownData, String index, String type) {
        addToMap(unknownData, type, index);
    }

    public static List<ApmSourceGroup> parseIndexGroup(ApmDataSourceIndex dataSourceIndex,
                                                       ApmConnectSourceIndex connectSourceIndex) {
        HashMap<String, List<ApmBaseUnit<ApmSourceData>>> dataUnits = dataSourceIndex.getDeviceIdIndexMap();
        HashMap<String, List<ApmBaseUnit<ApmSourceConnect>>> connectUnits = connectSourceIndex.getDeviceIdIndexMap();
        Set<String> devicesOfData = dataUnits.keySet();
        Set<String> devicesOfConnect = connectUnits.keySet();

        List<ApmSourceGroup> groups = new ArrayList<>();
        for (String device : devicesOfData) {
            ApmSourceGroup g = new ApmSourceGroup();
            g.setGroupId(device);
            g.setDataSource(dataUnits.get(device));
            g.setConnectSource(connectUnits.get(device));
            devicesOfConnect.remove(device);
            groups.add(g);
        }

        for (String device : devicesOfConnect) {
            ApmSourceGroup g = new ApmSourceGroup();
            g.setGroupId(device);
            g.setDataSource(null);
            g.setConnectSource(connectUnits.get(device));
            groups.add(g);
        }

        return groups;
    }
}
