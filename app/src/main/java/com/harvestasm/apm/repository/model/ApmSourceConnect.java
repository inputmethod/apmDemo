package com.harvestasm.apm.repository.model;


import com.harvestasm.apm.repository.model.search.ApmBaseSourceType;

import java.util.List;

public class ApmSourceConnect extends ApmBaseSourceType {
//    private List<String> app;
    private List<String> device;
    private List<ApmDeviceMicsItem> devicemics;

//    public List<String> getApp() {
//        return app;
//    }
//
//    public void setApp(List<String> app) {
//        this.app = app;
//    }

    public List<String> getDevice() {
        return device;
    }

    public void setDevice(List<String> device) {
        this.device = device;
    }

    public List<ApmDeviceMicsItem> getDevicemics() {
        return devicemics;
    }

    public void setDevicemics(List<ApmDeviceMicsItem> devicemics) {
        this.devicemics = devicemics;
    }
}
