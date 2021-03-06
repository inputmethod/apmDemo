package com.harvestasm.apm.sample;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.harvestasm.apm.APMHelper;

/**
 * Created by yangfeng on 2018/3/8.
 */

public class DemoApplication extends Application {
    public void onCreate() {
        super.onCreate();
        APMHelper.instance(getApplicationContext());
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
