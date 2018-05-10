package com.harvestasm.apm.browser;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;

import com.harvestasm.apm.base.BaseFragmentActivity;
import com.harvestasm.apm.filter.FilterListFragment;
import com.harvestasm.apm.sample.R;
import com.harvestasm.apm.transaction.TransactionListFragment;

public class BrowserActivity extends BaseFragmentActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = BrowserActivity.class.getSimpleName();

    private BrowserActivityViewModel activityViewModel;

    @Override
    protected void onCreateComplete() {
        super.onCreateComplete();

        setupDrawer();

        activityViewModel = ViewModelProviders.of(this).get(BrowserActivityViewModel.class);
        activityViewModel.startObserve(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                navigateToHome(integer);
            }
        });
    }

    private void setupDrawer() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    private void navigateToHome(int id) {
        String tag = TAG + id;
        Fragment fragment = getFragmentWithTag(tag);
        if (null == fragment) {
            fragment = newFragment(id);
        }
        replaceFragment(fragment, tag);
    }

    private Fragment newFragment(int id) {
        if (1 == id) {
            return new FilterListFragment();
        } else if (0 == id) {
            return new BrowserListFragment();
        } else {
            return new TransactionListFragment();
        }
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, BrowserActivity.class);
        context.startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_next) {
            return activityViewModel.toggleNext();
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_manual_add) {
            // Handle the camera action
        } else if (id == R.id.nav_manual_chart) {

        } else if (id == R.id.nav_auto_activity) {

//        } else if (id == R.id.nav_manage) {
//
        } else if (id == R.id.nav_auto_measurement) {

        } else if (id == R.id.nav_auto_networking) {

        }

        activityViewModel.navigateBy(id);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
