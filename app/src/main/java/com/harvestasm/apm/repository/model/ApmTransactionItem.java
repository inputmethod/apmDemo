package com.harvestasm.apm.repository.model;

/**
 * Created by yangfeng on 2018/3/16.
 */

public class ApmTransactionItem {
    private String url;
    private String httpMethod;
    private String carrier;
    private String wanType;
    private double totalTime;
    private int statusCode;
    private int errorCode;
    private int bytesSent;
    private int bytesReceived;
    private String appData;
    private long timestamp;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getWanType() {
        return wanType;
    }

    public void setWanType(String wanType) {
        this.wanType = wanType;
    }

    public double getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(double totalTime) {
        this.totalTime = totalTime;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getBytesSent() {
        return bytesSent;
    }

    public void setBytesSent(int bytesSent) {
        this.bytesSent = bytesSent;
    }

    public int getBytesReceived() {
        return bytesReceived;
    }

    public void setBytesReceived(int bytesReceived) {
        this.bytesReceived = bytesReceived;
    }

    public String getAppData() {
        return appData;
    }

    public void setAppData(String appData) {
        this.appData = appData;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
