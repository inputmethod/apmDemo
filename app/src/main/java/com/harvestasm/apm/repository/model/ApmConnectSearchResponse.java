package com.harvestasm.apm.repository.model;

import java.util.List;

/**
 * Created by yangfeng on 2018/1/14.
 */

public class ApmConnectSearchResponse extends ApmBaseSearchResponse<ApmConnectSearchResponse.ConnectWrapper> {
    public static class ConnectWrapper extends ApmBaseWrapper<ConnectUnit> {
    }

    public static class ConnectUnit extends ApmBaseUnit<SourceTypeConnect> {
    }

    public static class SourceTypeConnect extends ApmBaseSourceType {
        private List<String> app;
        private List<String> device;
        private List<ApmDeviceMicsItem> devicemics;

        public List<String> getApp() {
            return app;
        }

        public void setApp(List<String> app) {
            this.app = app;
        }

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
}
