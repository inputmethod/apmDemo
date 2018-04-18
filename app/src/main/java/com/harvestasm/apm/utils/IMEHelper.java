package com.harvestasm.apm.utils;

import android.content.Context;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IMEHelper {
    /***
     * 已经安装的输入法包名列表，每一项为"com包名"
     * @param context
     * @return
     */
    public static List<String> getInstallImePackageList(@NonNull Context context) {
        List<String> imePackageList = new ArrayList<>();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        List<InputMethodInfo> methodList = imm.getInputMethodList();
        for(InputMethodInfo mi:methodList ) {
            String name = mi.getPackageName();
            imePackageList.add(name);
        }
        return imePackageList;
    }

    /***
     * 已经勾选的ime列表，每项格式为"com.包名/service名"
     * @param context
     * @return
     */
    public static List<String> getCheckedImeList(@NonNull Context context) {
        String enable = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ENABLED_INPUT_METHODS);
        String[] imeList = enable.split(":");
        return Arrays.asList(imeList);
    }

    /***
     * 当前默认的ime，格式为"com.包名/service名"
     * @param context
     * @return
     */
    public static String getCurrentIme(@NonNull Context context) {
        return Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.DEFAULT_INPUT_METHOD);
    }
}
