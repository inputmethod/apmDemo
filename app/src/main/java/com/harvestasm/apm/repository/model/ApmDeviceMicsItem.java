package com.harvestasm.apm.repository.model;

/**
 * Created by yangfeng on 2018/3/21.
 */

public class ApmDeviceMicsItem {
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
