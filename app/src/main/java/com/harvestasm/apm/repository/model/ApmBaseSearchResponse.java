package com.harvestasm.apm.repository.model;

/**
 * Created by yangfeng on 2018/3/21.
 */

public abstract class ApmBaseSearchResponse <T extends ApmBaseWrapper> {

    private int took;
    private boolean timed_out;

    private T hits;

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

    public T getHits() {
        return hits;
    }

    public void setHits(T hits) {
        this.hits = hits;
    }

}
