package com.harvestasm.apm.filter;

import com.harvestasm.apm.filter.item.FilterItemModel;

import java.util.List;

public class FilterCategoryModel {
    private String title;
    private List<FilterItemModel> candidates;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<FilterItemModel> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<FilterItemModel> candidates) {
        this.candidates = candidates;
    }
}
