package com.harvestasm.apm;

import android.content.Context;

import typany.apm.agent.android.TypanyAPM;
import typany.apm.agent.android.instrumentation.Trace;
import typany.apm.agent.android.logging.AgentLog;

/**
 * Created by yangfeng on 2018/3/8.
 */

public class APMHelper {
    private static APMHelper _instance;

    private final TypanyAPM nr;
    public APMHelper(Context applicationContext, String remoteHost) {
        nr = TypanyAPM.fromApplication().usingCollectorAddress(remoteHost);
        nr.usingSsl(false);
        nr.withLogLevel(AgentLog.DEBUG).start(applicationContext);
    }

    @Trace
    public static void instance(Context activityContext) {
        if (null == _instance) {
            String remoteHost = "10.135.71.155:9200";
            _instance = new APMHelper(activityContext, remoteHost);
        }
    }
}
