package com.harvestasm.options;

import com.harvestasm.options.item.OptionItemModel;

import java.util.List;

public class OptionCategoryModel {
    private String title;
    private List<OptionItemModel> candidates;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<OptionItemModel> getCandidates() {
        return candidates;
    }

    public void setCandidates(List<OptionItemModel> candidates) {
        this.candidates = candidates;
    }
}
