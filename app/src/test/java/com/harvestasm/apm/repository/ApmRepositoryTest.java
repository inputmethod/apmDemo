package com.harvestasm.apm.repository;

import com.harvestasm.apm.reporter.SearchResult;
import com.harvestasm.apm.reporter.SearchDataParser;
import com.harvestasm.apm.repository.model.ApmConnectData;
import com.harvestasm.apm.repository.model.ApmConnectSearchResponse;
import com.harvestasm.apm.repository.model.ApmDataSearchResponse;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by yangfeng on 2017/12/5.
 */

public class ApmRepositoryTest {
    private ApmRepository repository;
    private ApmConnectData apmConnectData;

    @Before
    public void initialize() {
        repository = new ApmRepository();

        apmConnectData = new ApmConnectData();
        List<String> app = new ArrayList<>();
        app.add("Apm Sample");
        app.add("3.2");
        app.add("com.harvestasm.apm.sample");
        apmConnectData.setApp(app);

        List<String> devices = new ArrayList<>();
        devices.add("Android");
        devices.add("6.0.1");
        devices.add("samsung SM-N9100");
        devices.add("AndroidAgent");
        devices.add("1.0.0");
        devices.add("09fd19c0-e356-482d-9623-143404602b16");
        devices.add("");
        devices.add("");
        devices.add( "samsung");
        apmConnectData.setDevice(devices);
        apmConnectData.setDeviceId("09fd19c0-e356-482d-9623-143404602b16");

        List<ApmConnectData.DeviceMics> deviceMics = new ArrayList<>();
        ApmConnectData.DeviceMics d = new ApmConnectData.DeviceMics();
        d.setPlatform("Native");
        d.setPlatformVersion("1.0.0");
        d.setSize("normal");
        deviceMics.add(d);
        apmConnectData.setDevicemics(deviceMics);
        apmConnectData.setTimestamp("2018-03-16T11:15:13.623Z");
    }

    @Test
    public void testMobileAllSearch() throws Exception {
        ApmDataSearchResponse apmData = repository.mobileAllSearch();
        ApmConnectSearchResponse apmConnect = repository.mobileConnectSearch();
        Assert.assertNotNull(apmData);
        Assert.assertNotNull(apmConnect);
        SearchResult data = SearchDataParser.parse(apmData, apmConnect);
    }

    @Test
    public void testMobileConnectionSearch() throws Exception {
        ApmConnectSearchResponse result = repository.mobileConnectSearch();
        Assert.assertNotNull(result);
    }

    @Test
    public void testMobileDataSearch() throws Exception {
        ApmDataSearchResponse result = repository.mobileDataSearch();
        Assert.assertNotNull(result);
    }

//    @Test
//    public void testMobileConnect() throws Exception {
//        ApmConnectResponse response = repository.mobileConnect(apmConnectData);
//        Assert.assertNotNull(response);
//    }
}
