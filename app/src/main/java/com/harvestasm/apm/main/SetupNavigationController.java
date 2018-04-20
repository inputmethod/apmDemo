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

package com.harvestasm.apm.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.harvestasm.apm.dashboard.DashboardFragment;
import com.harvestasm.apm.home.HomeFragment;
import com.harvestasm.apm.sample.R;

/**
 * A utility class that handles navigation in {@link FragmentActivity}.
 */
public class SetupNavigationController {
    private static final String TAG = SetupNavigationController.class.getSimpleName();

    private static final String TAG_HOME = "TAG_HOME";
    private static final String TAG_EDIT = "TAG_EDIT";

    private boolean isCurrentEdit = false;

    private final int containerId;
    private final FragmentManager fragmentManager;

    public SetupNavigationController(FragmentActivity compatActivity) {
        this.containerId = R.id.container;
        this.fragmentManager = compatActivity.getSupportFragmentManager();
        navigateToHome();
    }

    private void navigateToHome() {
        Fragment fragment = getFragmentWithTag(TAG_HOME);
        if (null == fragment) {
            fragment = new HomeFragment();
        }
        replaceFragment(fragment, null);
    }

    private void navigateEditFragment() {
        Fragment fragment = getFragmentWithTag(TAG_EDIT);
        if (null == fragment) {
            fragment = new DashboardFragment();
        }
        replaceFragment(fragment, TAG_EDIT);
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

    public void toggle() {
        isCurrentEdit = !isCurrentEdit;
        if (isCurrentEdit) {
            navigateEditFragment();
        } else {
            navigateToHome();
        }
    }
}
