package com.harvestasm.apm.main;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.harvestasm.apm.sample.R;
import com.harvestasm.chart.BaseChartActivity;

public class SetupActivity extends BaseChartActivity {
    private SetupNavigationController navigationController;

    private void navigateEditFragment() {
        navigationController.navigateToDashboard();
    }

    private void navigateHomeFragment() {
        navigationController.navigateToHome();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        // initialising, todo: inject with dagger2
        navigationController = new SetupNavigationController(this);
        navigateHomeFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_setup, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_edit) {
            navigateEditFragment();
        } else {
            return super.onOptionsItemSelected(item);
        }

        return true;
    }
}
