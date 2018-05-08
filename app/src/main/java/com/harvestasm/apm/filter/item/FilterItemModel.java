package com.harvestasm.apm.filter.item;

import com.harvestasm.apm.base.pikcer.ItemModelInterface;

public class FilterItemModel implements ItemModelInterface {
    private String category;
    private String name;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getTitle() {
        return category;
    }

    @Override
    public String getSubTitle() {
        return name;
    }
}
