package com.harvestasm.apm.repository.model.search;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by yangfeng on 2018/3/21.
 */

public abstract class ApmBaseSearchResponse <T extends ApmBaseSearchResponse.ApmBaseSourceType> {
    private int took;
    private boolean timed_out;

    private ApmBaseWrapper<T> hits;

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

    public ApmBaseWrapper<T> getHits() {
        return hits;
    }

    public void setHits(ApmBaseWrapper<T> hits) {
        this.hits = hits;
    }

    public static class ApmBaseWrapper<T extends ApmBaseSourceType> {
        private int total;
        private int max_score;
        private List<ApmBaseUnit<T>> hits;

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

        public List<ApmBaseUnit<T>> getHits() {
            return hits;
        }

        public void setHits(List<ApmBaseUnit<T>> hits) {
            this.hits = hits;
        }
    }

    public static class ApmBaseUnit<T extends ApmBaseSourceType> {
        private String _index;
        private String _type;
        private String _id;
        private int _score;
        private T _source;

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

        public T get_source() {
            return _source;
        }

        public void set_source(T _source) {
            this._source = _source;
        }
    }

    /**
     * Data和Connect上报的数据基类，都有时间戳和设备id字段
     * TODO:有不少数据的设备id为空,如果能保证设备id唯一且非空，data和connect可以用它关联
     */
    public static class ApmBaseSourceType {
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
}
