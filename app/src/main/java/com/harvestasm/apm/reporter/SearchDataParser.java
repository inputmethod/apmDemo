package com.harvestasm.apm.reporter;

import com.harvestasm.apm.repository.model.ApmDataSearchResponse;

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

    public static SearchData parse(ApmDataSearchResponse result) {
        Map<String, List<String>> unknownData = new HashMap<>();
        SearchData searchData = new SearchData();
        for (ApmDataSearchResponse.DataUnit unit : result.getHits().getHits()) {
            String index = unit.get_index();
            String type = unit.get_type();
            String id = unit.get_id();
            int score = unit.get_score();

            if (isEquals(index, INDEX)) {
                if (isEquals(type, TYPE_CONNECT)) {
                    // todo: parse connect result
                } else if (isEquals(type, TYPE_DATA)) {
                    ApmDataSearchResponse.SourceType sourceType = unit.get_source();
                } else {
                    handleUnknownType(unknownData, index, type);
                }
            } else {
                handleUnknownType(unknownData, index, type);
            }
        }
        return searchData;
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
