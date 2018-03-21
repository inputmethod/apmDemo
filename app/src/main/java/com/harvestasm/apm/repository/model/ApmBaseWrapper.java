package com.harvestasm.apm.repository.model;

import java.util.List;

/**
 * Created by yangfeng on 2018/3/21.
 */

public class ApmBaseWrapper<T> {
    private int total;
    private int max_score;
    private List<T> hits;

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

    public List<T> getHits() {
        return hits;
    }

    public void setHits(List<T> hits) {
        this.hits = hits;
    }
}
