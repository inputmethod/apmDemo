package com.harvestasm.apm.repository.model;

import java.util.Arrays;

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

    private String getString() {
        return platform + platformVersion + size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApmDeviceMicsItem that = (ApmDeviceMicsItem) o;

        return getString() == null && that.getString() == null ||
                getString() != null && getString().equals(that.getString());
    }

    @Override
    public int hashCode() {
        String objects[] = {platformVersion, platform, size};
        return Arrays.hashCode(objects);
    }
}
