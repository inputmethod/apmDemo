package com.harvestasm.apm.home;

import com.harvestasm.apm.repository.model.ApmDeviceMicsItem;

import java.util.List;

public class HomeDeviceItem extends HomeItem {
    // app info
    public static class AppItem {
        private String appName;
        private String appVersion;
        private String appPackage;

        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }

        public String getAppVersion() {
            return appVersion;
        }

        public void setAppVersion(String appVersion) {
            this.appVersion = appVersion;
        }

        public String getAppPackage() {
            return appPackage;
        }

        public void setAppPackage(String appPackage) {
            this.appPackage = appPackage;
        }
    }

    // device info
    public static class OsItem {
        private String osName;
        private String osVersion;

        public String getOsName() {
            return osName;
        }

        public void setOsName(String osName) {
            this.osName = osName;
        }

        public String getOsVersion() {
            return osVersion;
        }

        public void setOsVersion(String osVersion) {
            this.osVersion = osVersion;
        }
    }


    public static class HardwareItem {
        private String hwModel;
        private String hwId;
        private String hwVendor;
        private List<ApmDeviceMicsItem> devicemics;

        public String getHwModel() {
            return hwModel;
        }

        public void setHwModel(String hwModel) {
            this.hwModel = hwModel;
        }

        public String getHwId() {
            return hwId;
        }

        public void setHwId(String hwId) {
            this.hwId = hwId;
        }

        public String getHwVendor() {
            return hwVendor;
        }

        public void setHwVendor(String hwVendor) {
            this.hwVendor = hwVendor;
        }

        public List<ApmDeviceMicsItem> getDevicemics() {
            return devicemics;
        }

        public void setDevicemics(List<ApmDeviceMicsItem> devicemics) {
            this.devicemics = devicemics;
        }
    }
}
