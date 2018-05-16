package com.harvestasm.apm.repository;

import com.harvestasm.apm.browser.DataStorage;
import com.harvestasm.apm.reporter.ApmConnectSourceIndex;
import com.harvestasm.apm.reporter.ApmDataSourceIndex;
import com.harvestasm.apm.reporter.ApmSourceGroup;
import com.harvestasm.apm.reporter.SearchDataParser;
import com.harvestasm.apm.reporter.SearchResult;
import com.harvestasm.apm.repository.model.ApmActivityItem;
import com.harvestasm.apm.repository.model.ApmDeviceMicsItem;
import com.harvestasm.apm.repository.model.ApmSourceConnect;
import com.harvestasm.apm.repository.model.ApmSourceData;
import com.harvestasm.apm.repository.model.connect.ApmConnectResponse;
import com.harvestasm.apm.repository.model.search.ApmBaseSearchResponse;
import com.harvestasm.apm.repository.model.search.ApmCommonSearchResponse;
import com.harvestasm.apm.repository.model.search.ApmConnectSearchResponse;
import com.harvestasm.apm.repository.model.search.ApmDataSearchResponse;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import typany.apm.agent.android.Agent;
import typany.apm.agent.android.harvest.ApplicationInformation;
import typany.apm.agent.android.harvest.DeviceInformation;
import typany.apm.agent.android.harvest.HarvestData;
import typany.apm.agent.android.tracing.ActivityTrace;
import typany.apm.agent.android.tracing.Sample;
import typany.apm.agent.android.tracing.Trace;
import typany.apm.agent.android.tracing.TraceMachine;
import typany.apm.retrofit2.Call;
import typany.apm.retrofit2.Response;


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
        ApmCommonSearchResponse apmData = mobileAllSearch();
        Assert.assertNotNull(apmData);
        SearchResult data = SearchDataParser.parse(apmData);
    }

    private ApmCommonSearchResponse mobileAllSearch() throws Exception{
        Call<ApmCommonSearchResponse> call = repository.mobileAllSearch();
        Response<ApmCommonSearchResponse> response = call.execute();
        return response.body();
    }

    @Test
    public void testMobileConnectionSearch() throws Exception {
        ApmConnectSearchResponse result = mobileConnectSearch();
        Assert.assertNotNull(result);
        SearchDataParser.parseConnectionSummary(result);
    }

    private ApmConnectSearchResponse mobileConnectSearch() throws Exception {
        Call<ApmConnectSearchResponse> call = repository.mobileConnectSearch();
        Response<ApmConnectSearchResponse> response = call.execute();
        return response.body();
    }

    @Test
    public void testMobileDataSearch() throws Exception {
        ApmBaseSearchResponse<ApmSourceData> result = mobileDataSearch();
        Assert.assertNotNull(result);
        SearchDataParser.parseDataSummary(result);
    }

    @Test
    public void testMobileIndexGroupSearch() throws Exception {
        ApmBaseSearchResponse<ApmSourceData> data = mobileDataSearch();
        ApmConnectSearchResponse connect = mobileConnectSearch();
        ApmDataSourceIndex dataSourceIndex = new ApmDataSourceIndex(data);
        ApmConnectSourceIndex connectSourceIndex = new ApmConnectSourceIndex(connect);
        Assert.assertNotNull(dataSourceIndex);
        Assert.assertNotNull(connectSourceIndex);
        List<ApmSourceGroup> groupList = ApmSourceGroup.parseSourceGroup(dataSourceIndex, connectSourceIndex);
        Assert.assertNotNull(groupList);

//        groupList = SearchDataParser.parseSourceGroup(data, connect);
//        Assert.assertNotNull(groupList);
    }

    private ApmDataSearchResponse mobileDataSearch() throws Exception {
        Call<ApmDataSearchResponse> call = repository.mobileDataSearch();
        Response<ApmDataSearchResponse> response = call.execute();
        return response.body();
    }

//    @Test
//    public void testMobileConnect() throws Exception {
//        ApmConnectResponse response = repository.connect(apmSourceConnect);
//        Assert.assertNotNull(response);
//    }
    @Test
    public void testCombineDataConnection() throws Exception {
        ApmDataSearchResponse dataResponse = mobileDataSearch();
        ApmConnectSearchResponse connectResponse = mobileConnectSearch();
        DataStorage.get().setDataResponse(dataResponse);
        DataStorage.get().setConnectResponse(connectResponse);
        Assert.assertNotNull(DataStorage.get().queryTransaction());
        Assert.assertNotNull(DataStorage.get().queryByOption());
        Map<String, List<ApmActivityItem.VitalUnit>> vitalMap = DataStorage.get().buildApplicationMemoryMap();
        Assert.assertNotNull(vitalMap);
    }

    @Test
    public void testActivityTraceData() throws Exception {
        // important: 要在ActivityTrace创建前准备好所有vital数据，否则时间戳滞后，在
        // ActivityTrace.asELKJson()执行时会被忽略掉
        Map<Sample.SampleType, Collection<Sample>> vitals = generateVitals();

        final DeviceInformation deviceInformation = Agent.getDeviceInformation();
        ApplicationInformation app = new ApplicationInformation("testName", "testVersion", "testId", "1024");

        HarvestData harvestData = new HarvestData(app, deviceInformation);

        Trace rootTrace = new Trace();
        rootTrace.displayName = TraceMachine.formatActivityDisplayName("Memory/Used");
        rootTrace.metricName = TraceMachine.formatActivityMetricName(rootTrace.displayName);
        rootTrace.metricBackgroundName = TraceMachine.formatActivityBackgroundMetricName(rootTrace.displayName);
        rootTrace.entryTimestamp = System.currentTimeMillis();

        ActivityTrace activityTrace = new ActivityTrace(rootTrace);
        activityTrace.setVitals(vitals);

        harvestData.getActivityTraces().add(activityTrace);

        ApmConnectResponse response = repository.apmTestData(harvestData.toJson());
        Assert.assertNotNull(response);
    }

    private Map<Sample.SampleType, Collection<Sample>> generateVitals() {
        EnumMap<Sample.SampleType, Collection<Sample>> samples = new EnumMap(Sample.SampleType.class);
        List<Sample> memorySamples = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Sample sample = new Sample(Sample.SampleType.MEMORY);
            sample.setSampleValue(Math.random() * 1000 * i);
            memorySamples.add(sample);
        }
        samples.put(Sample.SampleType.MEMORY, memorySamples);

        List<Sample> cpuSamples = new ArrayList<>();for (int i = 0; i < 10; i++) {
            Sample sample = new Sample(Sample.SampleType.CPU);
            sample.setSampleValue(Math.random() * 128 * i);
            cpuSamples.add(sample);
        }
        samples.put(Sample.SampleType.CPU, cpuSamples);
        return samples;
    }
}
