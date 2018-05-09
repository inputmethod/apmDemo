package com.harvestasm.apm.main;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.harvestasm.apm.APMHelper;
import com.harvestasm.apm.add.AddDataStorage;

/**
 * Created by yangfeng on 2018/3/8.
 */

public class ApmApplication extends Application {
    public void onCreate() {
        super.onCreate();

        AddDataStorage.init(this);
        APMHelper.instance(getApplicationContext());
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
