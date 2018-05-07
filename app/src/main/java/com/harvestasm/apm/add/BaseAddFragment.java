package com.harvestasm.apm.add;

import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.harvestasm.apm.sample.R;

import butterknife.ButterKnife;

abstract public class BaseAddFragment extends Fragment {
    private Toast nextToast;
    private MenuItem menuItem;


    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(getFragmentLayoutResId(), container, false);
        ButterKnife.bind(this, view);
        inflateChildrenView(inflater, view);
        return view;
    }

    protected abstract void inflateChildrenView(LayoutInflater inflater, View view);

    protected abstract int getFragmentLayoutResId();

    @Override
    public void onPause() {
        super.onPause();

        if (null != nextToast) {
            nextToast.cancel();
            nextToast = null;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_next, menu);
        menuItem = menu.findItem(R.id.action_next);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_next) {
            if (onNextStep()) {
                getActivity().onBackPressed();
            } else {
                showNextStepTips();
            }
        } else {
            return super.onOptionsItemSelected(item);
        }

        return true;
    }

    private boolean onNextStep() {
        try {
            performNextTask();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private void showNextStepTips() {
        if (null != nextToast) {
            nextToast.cancel();
            nextToast.cancel();
        }
        nextToast = Toast.makeText(getContext(), "请正确填写全部数据后再提交.", Toast.LENGTH_SHORT);
        nextToast.show();
    }

    protected void enableNextMenu(boolean enabled) {
        menuItem.setEnabled(enabled);
    }

    protected void setMenuLabel(@StringRes int labelId) {
        if (null != menuItem) {
            menuItem.setTitle(labelId);
        }
    }

    protected abstract void performNextTask();
}
