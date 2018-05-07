package com.harvestasm.apm.filter.item;

import java.util.Set;

public class FilterItemModel {
    private String title;
    private Set<String> candidates;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<String> getCandidates() {
        return candidates;
    }

    public void setCandidates(Set<String> candidates) {
        this.candidates = candidates;
    }
}
