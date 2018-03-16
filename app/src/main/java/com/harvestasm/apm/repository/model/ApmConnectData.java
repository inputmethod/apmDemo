package com.harvestasm.apm.repository.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by yangfeng on 2018/3/16.
 */

public class ApmConnectData {
    private List<String> app;
    private List<String> device;
    private String deviceId;
    private List<DeviceMics> devicemics;

    @SerializedName("@timestamp")
    private String timestamp;

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

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public List<DeviceMics> getDevicemics() {
        return devicemics;
    }

    public void setDevicemics(List<DeviceMics> devicemics) {
        this.devicemics = devicemics;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public static class DeviceMics {
        private String platformVersion;
        private String platform;
        private String size;

        public String getPlatformVersion() {
            return platformVersion;
        }

        public void setPlatformVersion(String platformVersion) {
            this.platformVersion = platformVersion;
        }

        public String getPlatform() {
            return platform;
        }

        public void setPlatform(String platform) {
            this.platform = platform;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }
    }
}
