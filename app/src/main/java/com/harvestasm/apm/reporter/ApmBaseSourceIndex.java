package com.harvestasm.apm.reporter;

import com.harvestasm.apm.repository.model.search.ApmBaseSearchResponse;
import com.harvestasm.apm.repository.model.search.ApmBaseSourceType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ApmBaseSourceIndex {
    public <T extends ApmBaseSourceType> ApmBaseSourceIndex(ApmBaseSearchResponse<T> source) {
    }

    public static void addListSizeSet(Set<Integer> lengthSet, List list) {
        lengthSet.add(null == list ? 0 : list.size());
    }

    public static<K, V> void addPartToMap(Map<K, List<V>> indexMap, V unit, List<K> keyList) {
        for (K key : keyList) {
            addToMap(indexMap, unit, key);
        }
    }

    public static<K, V> void addToMap(Map<K, List<V>> indexMap, V value, K key) {
        List<V> valueList = indexMap.get(key);
        if (null == valueList) {
            valueList = new ArrayList<>();
            indexMap.put(key, valueList);
        }
        valueList.add(value);
    }

    public static boolean isEquals(String text, String other) {
        return null == text && null == other || null != text && text.equals(other);
    }
}
