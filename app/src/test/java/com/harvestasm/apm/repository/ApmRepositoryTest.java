package com.harvestasm.apm.repository;

import com.harvestasm.apm.reporter.SearchDataParser;
import com.harvestasm.apm.reporter.SearchResult;
import com.harvestasm.apm.repository.model.ApmDeviceMicsItem;
import com.harvestasm.apm.repository.model.ApmSourceConnect;
import com.harvestasm.apm.repository.model.ApmSourceData;
import com.harvestasm.apm.repository.model.search.ApmBaseSearchResponse;
import com.harvestasm.apm.repository.model.search.ApmConnectSearchResponse;

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
    private ApmSourceConnect apmSourceConnect;

    @Before
    public void initialize() {
        repository = new ApmRepository();

        apmSourceConnect = new ApmSourceConnect();
        List<String> app = new ArrayList<>();
        app.add("Apm Sample");
        app.add("3.2");
        app.add("com.harvestasm.apm.sample");
        apmSourceConnect.setApp(app);

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
        apmSourceConnect.setDevice(devices);
        apmSourceConnect.setDeviceId("09fd19c0-e356-482d-9623-143404602b16");

        List<ApmDeviceMicsItem> deviceMics = new ArrayList<>();
        ApmDeviceMicsItem d = new ApmDeviceMicsItem();
        d.setPlatform("Native");
        d.setPlatformVersion("1.0.0");
        d.setSize("normal");
        deviceMics.add(d);
        apmSourceConnect.setDevicemics(deviceMics);
        apmSourceConnect.setTimestamp("2018-03-16T11:15:13.623Z");
    }

    @Test
    public void testMobileAllSearch() throws Exception {
        ApmBaseSearchResponse apmData = repository.mobileAllSearch();
        Assert.assertNotNull(apmData);
        SearchResult data = SearchDataParser.parse(apmData);
    }

    @Test
    public void testMobileConnectionSearch() throws Exception {
        ApmConnectSearchResponse result = repository.mobileConnectSearch();
        Assert.assertNotNull(result);
        SearchDataParser.parseConnectionSummary(result);
    }

    @Test
    public void testMobileDataSearch() throws Exception {
        ApmBaseSearchResponse<ApmSourceData> result = repository.mobileDataSearch();
        Assert.assertNotNull(result);
    }

//    @Test
//    public void testMobileConnect() throws Exception {
//        ApmConnectResponse response = repository.mobileConnect(apmSourceConnect);
//        Assert.assertNotNull(response);
//    }
}
