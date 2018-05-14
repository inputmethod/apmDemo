package com.harvestasm.apm.reporter;

import com.harvestasm.apm.repository.model.ApmActivityItem;
import com.harvestasm.apm.repository.model.ApmMeasurementItem;
import com.harvestasm.apm.repository.model.ApmSourceData;
import com.harvestasm.apm.repository.model.ApmTransactionItem;
import com.harvestasm.apm.repository.model.search.ApmBaseSearchResponse;
import com.harvestasm.apm.repository.model.search.ApmBaseUnit;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ApmDataSourceIndex extends ApmBaseSourceIndex {
    // 数组字段的个数
    private Set<Integer> transactionLengthSet = new HashSet<>();
    private Set<Integer> measurementLengthSet = new HashSet<>();
    private Set<Integer> httpErrorLengthSet = new HashSet<>();
    private Set<Integer> activityLengthSet = new HashSet<>();
    private Set<Integer> healthLengthSet = new HashSet<>();
    private Set<Integer> eventsLengthSet = new HashSet<>();

    // 字段与数据映射
    private HashMap<String, List<ApmBaseUnit<ApmSourceData>>> deviceIdIndexMap = new HashMap<>();
    private HashMap<String, List<ApmBaseUnit<ApmSourceData>>> timestampIndexMap = new HashMap<>();
    private HashMap<String, List<ApmBaseUnit<ApmSourceData>>> appIndexMap = new HashMap<>();

    private HashMap<String, List<ApmBaseUnit<ApmSourceData>>> transactionUrlIndexMap = new HashMap<>();
    private HashMap<String, List<ApmBaseUnit<ApmSourceData>>> measureNameIndexMap = new HashMap<>();
    private HashMap<String, List<ApmBaseUnit<ApmSourceData>>> measureScopeIndexMap = new HashMap<>();
    private HashMap<String, List<ApmBaseUnit<ApmSourceData>>> activityTypeIndexMap = new HashMap<>();
    private HashMap<String, List<ApmBaseUnit<ApmSourceData>>> activityDisplayNameIndexMap = new HashMap<>();
    private HashMap<String, List<ApmBaseUnit<ApmSourceData>>> activityVitalsIndexMap = new HashMap<>();
    private HashMap<String, List<ApmBaseUnit<ApmSourceData>>> sessionIndexMap = new HashMap<>();

    public ApmDataSourceIndex(ApmBaseSearchResponse<ApmSourceData> apmData) {
        super(apmData);
        selfInit(apmData);
    }

    private void selfInit(ApmBaseSearchResponse<ApmSourceData> apmData) {
        ApmBaseSearchResponse.ApmBaseWrapper<ApmSourceData> wrapper = null == apmData ? null : apmData.getHits();
        List<ApmBaseUnit<ApmSourceData>> unitList  = null == wrapper ?
                Collections.<ApmBaseUnit<ApmSourceData>>emptyList() : wrapper.getHits();

        for (ApmBaseUnit<ApmSourceData> unit : unitList) {
            ApmSourceData ctc = unit.get_source();

            List<String> apps = ctc.getApp();
            String deviceId = ctc.getDeviceId();
            String timestamp = ctc.getTimestamp();

            addListSizeSet(transactionLengthSet, ctc.getTransaction());
            addListSizeSet(measurementLengthSet, ctc.getMeasurement());
            addListSizeSet(httpErrorLengthSet, ctc.getHttpError());
            addListSizeSet(activityLengthSet, ctc.getActivity());
            addListSizeSet(healthLengthSet, ctc.getHealth());
            // todo: event need to be check and parse
//            eventsLengthSet.add(ctc.getEvents().size());

            String appText = null == apps ? "" : apps.toString();
            addToMap(appIndexMap, unit, appText);

            addToMap(deviceIdIndexMap, unit, deviceId);
            addToMap(timestampIndexMap, unit, timestamp);

            // todo: build map.
            if (null != ctc.getTransaction()) {
                for (ApmTransactionItem item : ctc.getTransaction()) {
                    addToMap(transactionUrlIndexMap, unit, item.getUrl());
                }
            }
            if (null != ctc.getMeasurement()) {
                for (ApmMeasurementItem item : ctc.getMeasurement()) {
                    addToMap(measureNameIndexMap, unit, item.getName());
                    addToMap(measureScopeIndexMap, unit, item.getScope());
                }
            }

            if (null != ctc.getActivity()) {
                for (ApmActivityItem item : ctc.getActivity()) {
                    addToMap(activityTypeIndexMap, unit, item.getType());
                    addToMap(activityDisplayNameIndexMap, unit, item.getDisplayName());
                    addToMap(activityVitalsIndexMap, unit, item.getVitals().toString());
                    parseActivityRootTrace(unit, item.getRootTrace(), true);
                }
            }

            addToMap(sessionIndexMap, unit, ctc.getSession());
        }
    }

    private Set<String> paramTypeSet = new HashSet<>();
    private Set<String> typeSet = new HashSet<>();
    private Set<Integer> threadIdSet = new HashSet<>();
    private Set<String> threadNameSet = new HashSet<>();
    private void parseActivityRootTrace(ApmBaseUnit<ApmSourceData> unit, ApmActivityItem.RootTrace rootTrace, boolean root) {
        if (null == rootTrace) {
            return;
        }

        paramTypeSet.add(rootTrace.getParamType());
        typeSet.add(rootTrace.getType());
        threadIdSet.add(rootTrace.getTrdId());
        threadNameSet.add(rootTrace.getTrdName());

        if (null != rootTrace.getChild()) {
            for (ApmActivityItem.Child child : rootTrace.getChild()) {
                parseActivityRootTrace(unit, child, false);
            }
        }
    }

    public Set<Integer> getTransactionLengthSet() {
        return transactionLengthSet;
    }

    public void setTransactionLengthSet(Set<Integer> transactionLengthSet) {
        this.transactionLengthSet = transactionLengthSet;
    }

    public Set<Integer> getMeasurementLengthSet() {
        return measurementLengthSet;
    }

    public void setMeasurementLengthSet(Set<Integer> measurementLengthSet) {
        this.measurementLengthSet = measurementLengthSet;
    }

    public Set<Integer> getHttpErrorLengthSet() {
        return httpErrorLengthSet;
    }

    public void setHttpErrorLengthSet(Set<Integer> httpErrorLengthSet) {
        this.httpErrorLengthSet = httpErrorLengthSet;
    }

    public Set<Integer> getActivityLengthSet() {
        return activityLengthSet;
    }

    public void setActivityLengthSet(Set<Integer> activityLengthSet) {
        this.activityLengthSet = activityLengthSet;
    }

    public Set<Integer> getHealthLengthSet() {
        return healthLengthSet;
    }

    public void setHealthLengthSet(Set<Integer> healthLengthSet) {
        this.healthLengthSet = healthLengthSet;
    }

    public Set<Integer> getEventsLengthSet() {
        return eventsLengthSet;
    }

    public void setEventsLengthSet(Set<Integer> eventsLengthSet) {
        this.eventsLengthSet = eventsLengthSet;
    }

    public HashMap<String, List<ApmBaseUnit<ApmSourceData>>> getDeviceIdIndexMap() {
        return deviceIdIndexMap;
    }

    public void setDeviceIdIndexMap(HashMap<String, List<ApmBaseUnit<ApmSourceData>>> deviceIdIndexMap) {
        this.deviceIdIndexMap = deviceIdIndexMap;
    }

    public HashMap<String, List<ApmBaseUnit<ApmSourceData>>> getAppIndexMap() {
        return appIndexMap;
    }

    public void setAppIndexMap(HashMap<String, List<ApmBaseUnit<ApmSourceData>>> appIndexMap) {
        this.appIndexMap = appIndexMap;
    }

    public HashMap<String, List<ApmBaseUnit<ApmSourceData>>> getTimestampIndexMap() {
        return timestampIndexMap;
    }

    public void setTimestampIndexMap(HashMap<String, List<ApmBaseUnit<ApmSourceData>>> timestampIndexMap) {
        this.timestampIndexMap = timestampIndexMap;
    }

    public HashMap<String, List<ApmBaseUnit<ApmSourceData>>> getTransactionUrlIndexMap() {
        return transactionUrlIndexMap;
    }

    public void setTransactionUrlIndexMap(HashMap<String, List<ApmBaseUnit<ApmSourceData>>> transactionUrlIndexMap) {
        this.transactionUrlIndexMap = transactionUrlIndexMap;
    }

    public HashMap<String, List<ApmBaseUnit<ApmSourceData>>> getMeasureNameIndexMap() {
        return measureNameIndexMap;
    }

    public void setMeasureNameIndexMap(HashMap<String, List<ApmBaseUnit<ApmSourceData>>> measureNameIndexMap) {
        this.measureNameIndexMap = measureNameIndexMap;
    }

    public HashMap<String, List<ApmBaseUnit<ApmSourceData>>> getMeasureScopeIndexMap() {
        return measureScopeIndexMap;
    }

    public void setMeasureScopeIndexMap(HashMap<String, List<ApmBaseUnit<ApmSourceData>>> measureScopeIndexMap) {
        this.measureScopeIndexMap = measureScopeIndexMap;
    }

    public HashMap<String, List<ApmBaseUnit<ApmSourceData>>> getActivityTypeIndexMap() {
        return activityTypeIndexMap;
    }

    public void setActivityTypeIndexMap(HashMap<String, List<ApmBaseUnit<ApmSourceData>>> activityTypeIndexMap) {
        this.activityTypeIndexMap = activityTypeIndexMap;
    }

    public HashMap<String, List<ApmBaseUnit<ApmSourceData>>> getActivityDisplayNameIndexMap() {
        return activityDisplayNameIndexMap;
    }

    public void setActivityDisplayNameIndexMap(HashMap<String, List<ApmBaseUnit<ApmSourceData>>> activityDisplayNameIndexMap) {
        this.activityDisplayNameIndexMap = activityDisplayNameIndexMap;
    }

    public HashMap<String, List<ApmBaseUnit<ApmSourceData>>> getActivityVitalsIndexMap() {
        return activityVitalsIndexMap;
    }

    public void setActivityVitalsIndexMap(HashMap<String, List<ApmBaseUnit<ApmSourceData>>> activityVitalsIndexMap) {
        this.activityVitalsIndexMap = activityVitalsIndexMap;
    }

    public HashMap<String, List<ApmBaseUnit<ApmSourceData>>> getSessionIndexMap() {
        return sessionIndexMap;
    }

    public void setSessionIndexMap(HashMap<String, List<ApmBaseUnit<ApmSourceData>>> sessionIndexMap) {
        this.sessionIndexMap = sessionIndexMap;
    }
}
