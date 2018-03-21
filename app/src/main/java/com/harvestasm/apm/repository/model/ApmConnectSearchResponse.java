package com.harvestasm.apm.repository.model;

import java.util.List;

/**
 * Created by yangfeng on 2018/1/14.
 */

public class ApmConnectSearchResponse extends ApmBaseSearchResponse<ApmConnectSearchResponse.ConnectWrapper> {
    public static class ConnectWrapper extends ApmBaseWrapper<ConnectUnit> {
    }

    public static class ConnectUnit extends ApmBaseUnit<SourceTypeConnect> {
    }

    public static class SourceTypeConnect extends ApmBaseSourceType {
        private List<String> app;
        private List<String> device;
        private List<ApmDeviceMicsItem> devicemics;
    }
}
