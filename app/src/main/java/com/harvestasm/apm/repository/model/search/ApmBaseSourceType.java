package com.harvestasm.apm.repository.model.search;

import typany.apm.com.google.gson.annotations.SerializedName;

/**
 * Data和Connect上报的数据基类，都有时间戳和设备id字段
 * TODO:有不少数据的设备id为空,如果能保证设备id唯一且非空，data和connect可以用它关联
 */
public class ApmBaseSourceType {
    @SerializedName("deviceID")
    private String deviceId;
    @SerializedName("@timestamp")
    private String timestamp;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
