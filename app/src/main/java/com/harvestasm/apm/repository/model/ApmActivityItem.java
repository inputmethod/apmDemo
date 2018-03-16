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
    private String vitals;

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

    public static class Child extends RootTrace {
        // currently it is the same to RootTrace node.
    }

    public static class Vitals {

    }
}
