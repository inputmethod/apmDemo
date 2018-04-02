package com.harvestasm.apm.sample;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.harvestasm.apm.APMHelper;
import com.harvestasm.chart.ListViewMultiChartActivity;

public class MainActivity extends AppCompatActivity {
    private TextView mTextMessage;
    private MainViewModel mainViewModel;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        APMHelper.instance(this);
        setContentView(R.layout.activity_main);

        mTextMessage = findViewById(R.id.message);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mainViewModel = new MainViewModel();
//        mainViewModel.getNaviationLiveData().observe(this, navigationObserver);
        if (!APMHelper.checkSelfPermission(getApplicationContext())) {
            requestApmPermission();
        }

        APMHelper.instance(getApplicationContext());

        mTextMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ListViewMultiChartActivity.class);
                startActivity(intent);
            }
        });
    }

    @TargetApi(23)
    private void requestApmPermission() {
        if(Build.VERSION.SDK_INT >= 23) {
            String[] permissions = new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"};
            this.requestPermissions(permissions, 42);
        }
    }
}
