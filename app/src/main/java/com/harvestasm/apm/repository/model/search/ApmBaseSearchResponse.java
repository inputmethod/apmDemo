package com.harvestasm.apm.repository.model.search;

import java.util.List;

/**
 * Created by yangfeng on 2018/3/21.
 */

public abstract class ApmBaseSearchResponse <T extends ApmBaseSourceType> {
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

}
