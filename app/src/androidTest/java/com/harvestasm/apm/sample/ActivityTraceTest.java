package com.harvestasm.apm.sample;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import typany.apm.agent.android.tracing.ActivityTrace;
import typany.apm.agent.android.tracing.Sample;
import typany.apm.agent.android.tracing.Trace;
import typany.apm.agent.android.tracing.TraceMachine;

import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class ActivityTraceTest {

    @Test
    public void buildActivityTrace() throws Exception {
        // important: 要在ActivityTrace创建前准备好所有vital数据，否则时间戳滞后，在
        // ActivityTrace.asELKJson()执行时会被忽略掉
        Map<Sample.SampleType, Collection<Sample>> vitals = generateVitals();

        Trace rootTrace = new Trace();
        rootTrace.displayName = TraceMachine.formatActivityDisplayName("Memory/Used");
        rootTrace.metricName = TraceMachine.formatActivityMetricName(rootTrace.displayName);
        rootTrace.metricBackgroundName = TraceMachine.formatActivityBackgroundMetricName(rootTrace.displayName);
        rootTrace.entryTimestamp = System.currentTimeMillis();

        ActivityTrace activityTrace = new ActivityTrace(rootTrace);
        activityTrace.setVitals(vitals);

        Log.i("ActivityTraceTest", activityTrace.asELKJson().toString());
        assertNotNull(activityTrace);
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
