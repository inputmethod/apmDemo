package com.harvestasm.apm.imepicker;

import android.support.annotation.NonNull;

import com.harvestasm.apm.base.pikcer.ItemModelInterface;

import typany.apm.agent.android.harvest.ApplicationInformation;

public class ImeAppModel implements ItemModelInterface {
    private @NonNull final ApplicationInformation applicationInformation;

    public ImeAppModel(@NonNull ApplicationInformation applicationInformation) {
        this.applicationInformation = applicationInformation;
    }

    @Override
    public String getTitle() {
        return applicationInformation.getAppName();
    }

    @Override
    public String getSubTitle() {
        return applicationInformation.getAppVersion();
    }

    @NonNull
    public ApplicationInformation getApplicationInformation() {
        return applicationInformation;
    }
}
