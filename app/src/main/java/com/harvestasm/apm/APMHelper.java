package com.harvestasm.apm;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;

import typany.apm.agent.android.TypanyAPM;
import typany.apm.agent.android.instrumentation.Trace;
import typany.apm.agent.android.logging.AgentLog;

/**
 * Created by yangfeng on 2018/3/8.
 */

public class APMHelper {
    private static APMHelper _instance;

    private final TypanyAPM nr;
    public APMHelper(Context applicationContext, String remoteHost, String remoteIndex) {
        nr = TypanyAPM.fromApplication().usingCollectorAddress(remoteHost);
        nr.usingIndex(remoteIndex);
        nr.usingSsl(false);
        nr.withLogLevel(AgentLog.DEBUG).start(applicationContext);
    }

    @Trace
    public static void instance(Context activityContext) {
        if (!checkSelfPermission(activityContext)) {
            return;
        }

        if (null == _instance) {
            String remoteHost = "10.135.71.155:9200";
            String remoteIndex = "apidemo";
            _instance = new APMHelper(activityContext, remoteHost, remoteIndex);
        }
    }

    @TargetApi(23)
    public static boolean checkSelfPermission(Context context) {
        boolean writeExternalStorageGranted = context.checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == PackageManager.PERMISSION_GRANTED;
        return writeExternalStorageGranted;
    }
}
