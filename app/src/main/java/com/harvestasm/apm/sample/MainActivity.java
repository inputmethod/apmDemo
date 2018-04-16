package com.harvestasm.apm.sample;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.harvestasm.apm.APMHelper;
import com.harvestasm.chart.multibar.MultiBarChartActivity;
import com.harvestasm.chart.multilist.MultiChartActivity;
import com.harvestasm.common.NavigationController;

public class MainActivity extends AppCompatActivity {
    private MainViewModel mainViewModel;

    private NavigationController navigationController;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    navigateHomeFragment();
                    return true;
                case R.id.navigation_dashboard:
                    navigateDashboardFragment();
                    return true;
                case R.id.navigation_notifications:
                    navigateNotificationFragment();
                    return true;
            }
            return false;
        }
    };

    private void navigateNotificationFragment() {
        navigationController.navigateToNotification();
    }

    private void navigateDashboardFragment() {
        navigationController.navigateToDashboard();
    }

    private void navigateHomeFragment() {
        navigationController.navigateToHome();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        APMHelper.instance(this);
        setContentView(R.layout.activity_main);

        // initialising, todo: inject with dagger2
        navigationController = new NavigationController(this);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mainViewModel = new MainViewModel();
//        mainViewModel.getNaviationLiveData().observe(this, navigationObserver);
        if (!APMHelper.checkSelfPermission(getApplicationContext())) {
            requestApmPermission();
        }

        APMHelper.instance(getApplicationContext());

        navigateHomeFragment();
    }

    private void startMultiChartActivity() {
        Intent intent = new Intent(getBaseContext(), MultiChartActivity.class);
        startActivity(intent);
    }

    private void startMultiBarActivity() {
        Intent intent = new Intent(getBaseContext(), MultiBarChartActivity.class);
        startActivity(intent);
    }

    @TargetApi(23)
    private void requestApmPermission() {
        if(Build.VERSION.SDK_INT >= 23) {
            String[] permissions = new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"};
            this.requestPermissions(permissions, 42);
        }
    }
}
