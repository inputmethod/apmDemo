package com.harvestasm.apm.repository.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yangfeng on 2018/3/21.
 */
public abstract class ApmBaseSourceType {
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
