package com.harvestasm.apm.repository.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by yangfeng on 2018/1/14.
 */

public class ApmDataSearchResponse /*extends JSonBaseResponse<JSonListData<JSonSkinItem>>*/ {
    private int took;
    private boolean timed_out;
    private DataWrapper hits;

    public int getTook() {
        return took;
    }

    public void setTook(int took) {
        this.took = took;
    }

    public boolean isTimed_out() {
        return timed_out;
    }

    public void setTimed_out(boolean timed_out) {
        this.timed_out = timed_out;
    }

    public DataWrapper getHits() {
        return hits;
    }

    public void setHits(DataWrapper hits) {
        this.hits = hits;
    }

    public static class DataWrapper {
        private int total;
        private int max_score;
        private List<DataUnit> hits;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getMax_score() {
            return max_score;
        }

        public void setMax_score(int max_score) {
            this.max_score = max_score;
        }

        public List<DataUnit> getHits() {
            return hits;
        }

        public void setHits(List<DataUnit> hits) {
            this.hits = hits;
        }
    }

    public static class DataUnit {
        private String _index;
        private String _type;
        private String _id;
        private int _score;
        private SourceType _source;

        public String get_index() {
            return _index;
        }

        public void set_index(String _index) {
            this._index = _index;
        }

        public String get_type() {
            return _type;
        }

        public void set_type(String _type) {
            this._type = _type;
        }

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public int get_score() {
            return _score;
        }

        public void set_score(int _score) {
            this._score = _score;
        }

        public SourceType get_source() {
            return _source;
        }

        public void set_source(SourceType _source) {
            this._source = _source;
        }
    }

    public static class SourceType {
        private String deviceId;
        @SerializedName("@timestamp")
        private String timestamp;
        private int tmDelta;
        private List<ApmTransactionItem> transaction;
        private List<ApmMeasurementItem> measurement;
        // todo: httpError还没有数据，暂时以String来解析，需要修改与确定字段和数据相匹配的json格式
//    private List<HttpErrorItem> httpError;
        private List<String> httpError;
    private List<ApmActivityItem> activity;
//    private List<HealthItem> health;
        private List<String> health;
//    private SessionType session;
        private String session;
        private String sessionId;
//    private List<EventItem> events;

        public List<String> getHttpError() {
            return httpError;
        }

        public void setHttpError(List<String> httpError) {
            this.httpError = httpError;
        }

        public List<ApmActivityItem> getActivity() {
            return activity;
        }

        public void setActivity(List<ApmActivityItem> activity) {
            this.activity = activity;
        }

        public List<String> getHealth() {
            return health;
        }

        public void setHealth(List<String> health) {
            this.health = health;
        }

        public String getSession() {
            return session;
        }

        public void setSession(String session) {
            this.session = session;
        }

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

        public int getTmDelta() {
            return tmDelta;
        }

        public void setTmDelta(int tmDelta) {
            this.tmDelta = tmDelta;
        }

        public List<ApmTransactionItem> getTransaction() {
            return transaction;
        }

        public void setTransaction(List<ApmTransactionItem> transaction) {
            this.transaction = transaction;
        }

        public List<ApmMeasurementItem> getMeasurement() {
            return measurement;
        }

        public void setMeasurement(List<ApmMeasurementItem> measurement) {
            this.measurement = measurement;
        }

        public String getSessionId() {
            return sessionId;
        }

        public void setSessionId(String sessionId) {
            this.sessionId = sessionId;
        }
    }
}
