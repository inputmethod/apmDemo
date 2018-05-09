package com.harvestasm.options.item;

import android.graphics.drawable.Drawable;
import android.support.annotation.IdRes;

import com.harvestasm.apm.base.pikcer.ItemModelInterface;

public class OptionItemModel implements ItemModelInterface {
    private String category;
    private String name;
    private @IdRes int id;

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
        return name;
    }

    @Override
    public String getSubTitle() {
        return category;
    }

    @Override
    public Drawable getIcon() {
        return null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
