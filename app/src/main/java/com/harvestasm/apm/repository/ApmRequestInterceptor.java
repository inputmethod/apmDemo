package com.harvestasm.apm.repository;

import com.harvestasm.apm.repository.base.RequestInterceptor;

import okhttp3.HttpUrl;

/**
 * Created by yangfeng on 2018/3/16.
 */

public class ApmRequestInterceptor extends RequestInterceptor {
    public ApmRequestInterceptor() {
        super(false);
    }

    @Override
    protected void addQueryParameter(HttpUrl.Builder builder) {
        // do nothing and won't intercept any extra parameters to request.
    }
}
