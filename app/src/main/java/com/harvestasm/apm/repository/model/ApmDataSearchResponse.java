package com.harvestasm.apm.repository.model;

import java.util.List;

/**
 * Created by yangfeng on 2018/1/14.
 */

public class ApmDataSearchResponse extends ApmBaseSearchResponse<ApmDataSearchResponse.DataWrapper>  {
    public static class DataWrapper extends ApmBaseWrapper<DataUnit> {
    }

    public static class DataUnit extends ApmBaseUnit<SourceTypeData> {
    }

    public static class SourceTypeData extends ApmBaseSourceType {
        private int tmDelta;
        private List<ApmTransactionItem> transaction;
        private List<ApmMeasurementItem> measurement;
        // todo: httpError还没有数据，暂时以String来解析，需要修改与确定字段和数据相匹配的json格式
//    private List<HttpErrorItem> httpError;
        private List<String> httpError;
    private List<ApmActivityItem> activity;
//    private List<HealthItem> health;
        private List<String> health;
//    private SessionType session;
        private String session;
        private String sessionId;
//    private List<EventItem> events;

        public List<String> getHttpError() {
            return httpError;
        }

        public void setHttpError(List<String> httpError) {
            this.httpError = httpError;
        }

        public List<ApmActivityItem> getActivity() {
            return activity;
        }

        public void setActivity(List<ApmActivityItem> activity) {
            this.activity = activity;
        }

        public List<String> getHealth() {
            return health;
        }

        public void setHealth(List<String> health) {
            this.health = health;
        }

        public String getSession() {
            return session;
        }

        public void setSession(String session) {
            this.session = session;
        }

        public int getTmDelta() {
            return tmDelta;
        }

        public void setTmDelta(int tmDelta) {
            this.tmDelta = tmDelta;
        }

        public List<ApmTransactionItem> getTransaction() {
            return transaction;
        }

        public void setTransaction(List<ApmTransactionItem> transaction) {
            this.transaction = transaction;
        }

        public List<ApmMeasurementItem> getMeasurement() {
            return measurement;
        }

        public void setMeasurement(List<ApmMeasurementItem> measurement) {
            this.measurement = measurement;
        }

        public String getSessionId() {
            return sessionId;
        }

        public void setSessionId(String sessionId) {
            this.sessionId = sessionId;
        }
    }
}
