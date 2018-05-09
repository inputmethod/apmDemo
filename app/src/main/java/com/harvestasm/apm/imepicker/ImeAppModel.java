package com.harvestasm.apm.imepicker;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.harvestasm.apm.base.pikcer.ItemModelInterface;

import typany.apm.agent.android.harvest.ApplicationInformation;

public class ImeAppModel implements ItemModelInterface {
    private @NonNull final ApplicationInformation applicationInformation;
    private final @Nullable Drawable icon;

    public ImeAppModel(@NonNull ApplicationInformation information) {
        this(information, null);
    }

    public ImeAppModel(@NonNull ApplicationInformation information, @Nullable Drawable icon) {
        this.applicationInformation = information;
        this.icon = icon;
    }

    @Override
    public String getTitle() {
        return applicationInformation.getAppName();
    }

    @Override
    public String getSubTitle() {
        return applicationInformation.getAppVersion();
    }

    @Override
    public @Nullable Drawable getIcon() {
        return icon;
    }

    @NonNull
    public ApplicationInformation getApplicationInformation() {
        return applicationInformation;
    }
}
