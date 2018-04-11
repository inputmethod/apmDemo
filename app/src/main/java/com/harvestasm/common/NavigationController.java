/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.harvestasm.common;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.harvestasm.apm.home.HomeFragment;
import com.harvestasm.apm.sample.R;
import com.harvestasm.chart.multilist.MultiChartFragment;

/**
 * A utility class that handles navigation in {@link FragmentActivity}.
 */
public class NavigationController {
    private static final String TAG = NavigationController.class.getSimpleName();

    private static final String TAG_HOME = "TAG_HOME";
    private static final String TAG_DASHBOARD = "TAG_DASHBOARD";
    private static final String TAG_NOTIFICATION = "TAG_NOTIFICATION";

    private final int containerId;
    private final FragmentManager fragmentManager;

    public NavigationController(FragmentActivity compatActivity) {
        this.containerId = R.id.container;
        this.fragmentManager = compatActivity.getSupportFragmentManager();
    }

    public void navigateToHome() {
        Fragment fragment = getFragmentWithTag(TAG_HOME);
        if (null == fragment) {
            fragment = new HomeFragment();
        }
        replaceFragment(fragment, null);
    }

    public void navigateToDashboard() {
        Fragment fragment = getFragmentWithTag(TAG_DASHBOARD);
        if (null == fragment) {
            fragment = new MultiChartFragment();
        }
        replaceFragment(fragment, TAG_DASHBOARD);
    }

    public void navigateToNotification() {
        Fragment fragment = getFragmentWithTag(TAG_NOTIFICATION);
        if (null == fragment) {
            fragment = new MultiChartFragment();
        }
        replaceFragment(fragment, TAG_NOTIFICATION);
    }

    private Fragment getFragmentWithTag(String tag) {
        return fragmentManager.findFragmentByTag(tag);
    }

    // todo: 给你BottomNavigation一起使用时，back按键与正常行为（都直接退出？）addToBackStack
    private void replaceFragment(Fragment fragment, String tag) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        if (null == tag) {
            ft.replace(containerId, fragment, TAG_HOME);
        } else {
            ft = ft.replace(containerId, fragment, tag);
//                    .addToBackStack(null);
        }
        ft.commitAllowingStateLoss();
    }
}
