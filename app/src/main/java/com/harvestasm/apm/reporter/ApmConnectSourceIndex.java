package com.harvestasm.apm.reporter;

import com.harvestasm.apm.repository.model.ApmDeviceMicsItem;
import com.harvestasm.apm.repository.model.ApmSourceConnect;
import com.harvestasm.apm.repository.model.search.ApmBaseSearchResponse;
import com.harvestasm.apm.repository.model.search.ApmBaseUnit;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ApmConnectSourceIndex extends ApmBaseSourceIndex {
    private Set<Integer> appLengthSet = new HashSet<>();
    private Set<Integer> deviceLengthSet = new HashSet<>();
    private Set<Integer> devicemicsLengthSet = new HashSet<>();

    private HashMap<String, List<ApmBaseUnit<ApmSourceConnect>>> deviceIdIndexMap = new HashMap<>();
    private HashMap<String, List<ApmBaseUnit<ApmSourceConnect>>> timestampIndexMap = new HashMap<>();
    private HashMap<String, List<ApmBaseUnit<ApmSourceConnect>>> appIndexMap = new HashMap<>();

    private HashMap<String, List<ApmBaseUnit<ApmSourceConnect>>> deviceIndexMap = new HashMap<>();
    private HashMap<ApmDeviceMicsItem, List<ApmBaseUnit<ApmSourceConnect>>> deviceMicsItemListHashMap = new HashMap<>();

    public ApmConnectSourceIndex(ApmBaseSearchResponse<ApmSourceConnect> apmConnect) {
        super(apmConnect);
        selfInit(apmConnect);
    }

    private void selfInit(ApmBaseSearchResponse<ApmSourceConnect> apmConnect) {

        for (ApmBaseUnit<ApmSourceConnect> unit : apmConnect.getHits().getHits()) {
            ApmSourceConnect ctc = unit.get_source();

            String deviceId = ctc.getDeviceId();
            String timestamp = ctc.getTimestamp();

            List<String> apps = ctc.getApp();
            List<String> devices = ctc.getDevice();
            List<ApmDeviceMicsItem> deviceMicsItems = ctc.getDevicemics();

            addListSizeSet(appLengthSet, apps);
            addListSizeSet(deviceLengthSet, devices);
            addListSizeSet(devicemicsLengthSet, deviceMicsItems);

            addToMap(deviceIdIndexMap, unit, deviceId);
            addToMap(timestampIndexMap, unit, timestamp);
            addToMap(appIndexMap, unit, apps.toString());

            addToMap(deviceIndexMap, unit, devices.toString());
            addPartToMap(deviceMicsItemListHashMap, unit, deviceMicsItems);
        }

        apmConnect.isTimed_out();
    }

    public Set<Integer> getAppLengthSet() {
        return appLengthSet;
    }

    public void setAppLengthSet(Set<Integer> appLengthSet) {
        this.appLengthSet = appLengthSet;
    }

    public Set<Integer> getDeviceLengthSet() {
        return deviceLengthSet;
    }

    public void setDeviceLengthSet(Set<Integer> deviceLengthSet) {
        this.deviceLengthSet = deviceLengthSet;
    }

    public Set<Integer> getDevicemicsLengthSet() {
        return devicemicsLengthSet;
    }

    public void setDevicemicsLengthSet(Set<Integer> devicemicsLengthSet) {
        this.devicemicsLengthSet = devicemicsLengthSet;
    }

    public HashMap<String, List<ApmBaseUnit<ApmSourceConnect>>> getDeviceIdIndexMap() {
        return deviceIdIndexMap;
    }

    public void setDeviceIdIndexMap(HashMap<String, List<ApmBaseUnit<ApmSourceConnect>>> deviceIdIndexMap) {
        this.deviceIdIndexMap = deviceIdIndexMap;
    }

    public HashMap<String, List<ApmBaseUnit<ApmSourceConnect>>> getTimestampIndexMap() {
        return timestampIndexMap;
    }

    public void setTimestampIndexMap(HashMap<String, List<ApmBaseUnit<ApmSourceConnect>>> timestampIndexMap) {
        this.timestampIndexMap = timestampIndexMap;
    }

    public HashMap<String, List<ApmBaseUnit<ApmSourceConnect>>> getAppIndexMap() {
        return appIndexMap;
    }

    public void setAppIndexMap(HashMap<String, List<ApmBaseUnit<ApmSourceConnect>>> appIndexMap) {
        this.appIndexMap = appIndexMap;
    }

    public HashMap<String, List<ApmBaseUnit<ApmSourceConnect>>> getDeviceIndexMap() {
        return deviceIndexMap;
    }

    public void setDeviceIndexMap(HashMap<String, List<ApmBaseUnit<ApmSourceConnect>>> deviceIndexMap) {
        this.deviceIndexMap = deviceIndexMap;
    }

    public HashMap<ApmDeviceMicsItem, List<ApmBaseUnit<ApmSourceConnect>>> getDeviceMicsItemListHashMap() {
        return deviceMicsItemListHashMap;
    }

    public void setDeviceMicsItemListHashMap(HashMap<ApmDeviceMicsItem, List<ApmBaseUnit<ApmSourceConnect>>> deviceMicsItemListHashMap) {
        this.deviceMicsItemListHashMap = deviceMicsItemListHashMap;
    }
}
