package com.harvestasm.apm.repository.model;

import java.util.List;

/**
 * Created by yangfeng on 2018/3/16.
 */

public class ApmActivityItem {
    private String traceVersion;
    private String type;
    private long entryTimestamp;
    private long exitTimestamp;
    private String displayName;
    private RootTrace rootTrace;
    // it is a string with json format of array data within[], for example:
    // [{\"type\":\"VITALS\",\"MEMORY\":[[1512988020050,54.7373046875],[1512988020183,71.9794921875],[1512988020320,87.638671875]],\"CPU\":[[1512988020184,16.19047619047619],[1512988020321,13.20754716981132]]}]
//    private List<Vitals> vitals;
    private String vitals;

    public String getTraceVersion() {
        return traceVersion;
    }

    public void setTraceVersion(String traceVersion) {
        this.traceVersion = traceVersion;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getEntryTimestamp() {
        return entryTimestamp;
    }

    public void setEntryTimestamp(long entryTimestamp) {
        this.entryTimestamp = entryTimestamp;
    }

    public long getExitTimestamp() {
        return exitTimestamp;
    }

    public void setExitTimestamp(long exitTimestamp) {
        this.exitTimestamp = exitTimestamp;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public RootTrace getRootTrace() {
        return rootTrace;
    }

    public void setRootTrace(RootTrace rootTrace) {
        this.rootTrace = rootTrace;
    }

    public String getVitals() {
        return vitals;
    }

    public void setVitals(String vitals) {
        this.vitals = vitals;
    }

    public static class RootTrace {
        private String type;
        private String paramType;
        private long entryTimestamp;
        private long exitTimestamp;
        private String displayName;
        private int trdId;
        private String trdName;
        private List<Child> child;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getParamType() {
            return paramType;
        }

        public void setParamType(String paramType) {
            this.paramType = paramType;
        }

        public long getEntryTimestamp() {
            return entryTimestamp;
        }

        public void setEntryTimestamp(long entryTimestamp) {
            this.entryTimestamp = entryTimestamp;
        }

        public long getExitTimestamp() {
            return exitTimestamp;
        }

        public void setExitTimestamp(long exitTimestamp) {
            this.exitTimestamp = exitTimestamp;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public int getTrdId() {
            return trdId;
        }

        public void setTrdId(int trdId) {
            this.trdId = trdId;
        }

        public String getTrdName() {
            return trdName;
        }

        public void setTrdName(String trdName) {
            this.trdName = trdName;
        }

        public List<Child> getChild() {
            return child;
        }

        public void setChild(List<Child> child) {
            this.child = child;
        }
    }

    /**
     "paramType": "NETWORK",时有以下字段，没有"type": "TRACE",字段。 "paramType": "TRACE",有"type"字段没有以下字段

     "carrier": "wifi",
     "http_method": "POST",
     "status_code": "200",
     "wan_type": "wifi",
     "uri": "https://graph.facebook.com/v2.9/275932369410927/activities",
     "bytes_sent": "1513",
     "bytes_received":"100",
     */
    public static class Child extends RootTrace {
        // currently it is the same to RootTrace node.
        private String uri;
        private long bytes_sent;
        private long bytes_received;
        private int status_code;

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }

        public long getBytes_sent() {
            return bytes_sent;
        }

        public void setBytes_sent(long bytes_sent) {
            this.bytes_sent = bytes_sent;
        }

        public long getBytes_received() {
            return bytes_received;
        }

        public void setBytes_received(long bytes_received) {
            this.bytes_received = bytes_received;
        }

        public int getStatus_code() {
            return status_code;
        }

        public void setStatus_code(int status_code) {
            this.status_code = status_code;
        }
    }

    public static class Vitals {
        public String type;
        List<String> MEMORY; // todo: 内存数值对含义
        List<String> CPU;    // todo: CPU数值内存对含义
    }
}
