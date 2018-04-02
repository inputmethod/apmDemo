package com.harvestasm.apm.repository.model.connect;

/**
 * Created by yangfeng on 2018/3/16.
 */

public class ApmConnectResponse {
    /*"_index": "mobile",
            "_type": "connect",
            "_id": "AWItEIdXMk_E8GOU-NTd",
            "_version": 1,
            "result": "created",
            "_shards": {
        "total": 2,
                "successful": 1,
                "failed": 0
    },
            "created": true
            */
    private String _index;
    private String _type;
    private String _id;
    private int _reversion;
    private String result;
    private Shards _shardds;
    private boolean created;

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

    public int get_reversion() {
        return _reversion;
    }

    public void set_reversion(int _reversion) {
        this._reversion = _reversion;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Shards get_shardds() {
        return _shardds;
    }

    public void set_shardds(Shards _shardds) {
        this._shardds = _shardds;
    }

    public boolean isCreated() {
        return created;
    }

    public void setCreated(boolean created) {
        this.created = created;
    }

    private static class Shards {
        private int total;
        private int successful;
        private int failed;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getSuccessful() {
            return successful;
        }

        public void setSuccessful(int successful) {
            this.successful = successful;
        }

        public int getFailed() {
            return failed;
        }

        public void setFailed(int failed) {
            this.failed = failed;
        }
    }
}
