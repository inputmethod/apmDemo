package com.harvestasm.apm.home;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.graphics.Typeface;

import java.util.ArrayList;
import java.util.List;

// todo: simplest implement without repository to store data item.
public class HomeModel extends ViewModel {
    private final static String TAG = HomeModel.class.getSimpleName();

//    private final ApmRepository repository = new ApmRepository();

    public final MutableLiveData<List<HomeItem>> items = new MutableLiveData<>();
    public final MutableLiveData<Boolean> loadingState = new MutableLiveData<>();
    public final MutableLiveData<Integer> networkState = new MutableLiveData<>();

    public final MutableLiveData<HomeItem> clickItem = new MutableLiveData<>();

    public void load(Typeface typeface) {
        loadingState.setValue(true);
        startLoadWorker(typeface);
    }

    // todo: load within worker thread.
    private void startLoadWorker(Typeface typeface) {
        List<HomeItem> list = new ArrayList<>();
        fillSampleCharItem(list, typeface);
        items.postValue(list);
        loadingState.postValue(false);
        networkState.postValue(0);
    }

    private void fillSampleCharItem(List<HomeItem> list, Typeface typeface) {
        try {
//            ApmConnectSearchResponse connectResponse = repository.mobileConnectSearch();
//            ApmConnectSourceIndex connectSourceIndex = new ApmConnectSourceIndex(connectResponse);
//            HashMap<String, List<ApmBaseUnit<ApmSourceConnect>>> connectByDevice = connectSourceIndex.getDeviceIdIndexMap();
//            Set<String> connectDeviceSet = connectByDevice.keySet();
//
//            ApmDataSearchResponse dataResponse = repository.mobileDataSearch();
//            ApmDataSourceIndex dataSourceIndex = new ApmDataSourceIndex(dataResponse);
//            HashMap<String, List<ApmBaseUnit<ApmSourceData>>> dataByDevice = dataSourceIndex.getDeviceIdIndexMap();
//            Set<String> dataDeviceSet = dataByDevice.keySet();
//
//            // verify device id within connect and data source.
//            ArrayList<String> noMatchConnectDevice = new ArrayList<>(connectDeviceSet);
//            noMatchConnectDevice.removeAll(dataDeviceSet);
//
//            ArrayList<String> noMatchDataDevice = new ArrayList<>(dataDeviceSet);
//            noMatchDataDevice.removeAll(connectDeviceSet);
//
//            // parse devices
//            List<ApmSourceGroup> groupList = SearchDataParser.parseSourceGroup(dataSourceIndex, connectSourceIndex);

            // parse apps

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
