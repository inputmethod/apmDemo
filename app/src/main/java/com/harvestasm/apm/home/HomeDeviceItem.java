package com.harvestasm.apm.home;

import android.text.TextUtils;

import com.harvestasm.apm.reporter.ApmSourceGroup;
import com.harvestasm.apm.repository.model.ApmDeviceMicsItem;
import com.harvestasm.apm.repository.model.ApmSourceConnect;
import com.harvestasm.apm.repository.model.search.ApmBaseUnit;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import typany.apm.agent.android.harvest.ApplicationInformation;

public class HomeDeviceItem extends HomeItem {
    private final int dataCount;
    private final int connectCount;

    private final List<HomeDeviceItem.AppItem> appItemList = new ArrayList<>();
    private final List<HomeDeviceItem.OsItem> osItemList = new ArrayList<>();
    private final List<HomeDeviceItem.HardwareItem> hardwareItemList = new ArrayList<>();

    public HomeDeviceItem(ApmSourceGroup group) {
        setId(group.getGroupId());
        dataCount = group.getDataSource().size();
        connectCount = group.getConnectSource().size();
        parseFrom(group);
    }

    public List<AppItem> getAppItemList() {
        return appItemList;
    }

    public List<OsItem> getOsItemList() {
        return osItemList;
    }

    public List<HardwareItem> getHardwareItemList() {
        return hardwareItemList;
    }

    private void parseFrom(ApmSourceGroup group) {
        Set<String> appSet = new HashSet<>();
        Set<String> osSet = new HashSet<>();
        Set<String> hwSet = new HashSet<>();
        for (ApmBaseUnit<ApmSourceConnect> unit : group.getConnectSource()) {
            String app = TextUtils.join(",", unit.get_source().getApp());
            appSet.add(app);
            List<String> deviceText = unit.get_source().getDevice();
            osSet.add(deviceText.get(0) + "," + deviceText.get(1));
            hwSet.add(deviceText.get(2) + "," + deviceText.get(5) + "," + deviceText.get(8));
        }

        parseAppItemList(appItemList, appSet);
        parseOsItemList(osSet);
        parseHardwareItemList(hwSet);

    }

    public static void parseAppItemList(List<HomeDeviceItem.AppItem> appItemList, Set<String> appSet) {
        for (String app : appSet) {
            HomeDeviceItem.AppItem appItem = new HomeDeviceItem.AppItem();
            appItem.parseFrom(app.split(","));
            // todo: parse and set version code.
            appItemList.add(appItem);
        }
    }

    public static void parseApplicationList(List<ApplicationInformation> informationList, Set<String> appSet) {
        for (String app : appSet) {
            String[] segments = app.split(",");
            ApplicationInformation appItem = new ApplicationInformation(segments[0], segments[1], segments[2], segments[1]);
            informationList.add(appItem);
        }
    }

    private void parseHardwareItemList(Set<String> hwSet) {
        hardwareItemList.clear();
        for (String hw : hwSet) {
            HardwareItem hwItem = new HardwareItem();
            hwItem.parseFrom(hw.split(","));
            hardwareItemList.add(hwItem);
        }
    }

    private void parseOsItemList(Set<String> osSet) {
        osItemList.clear();
        for (String os : osSet) {
            OsItem osItem = new OsItem();
            osItem.parseFrom(os.split(","));
            osItemList.add(osItem);
        }
    }

    public int getDataCount() {
        return dataCount;
    }

    public int getConnectCount() {
        return connectCount;
    }

    public interface ItemParser {
        void parseFrom(String[] segments);
    }

    public interface ItemDisplay {
        String getDisplayName();
        String getDisplayVersion();
        String getDisplayExtra();
    }

    // app info
    public static class AppItem implements ItemParser, ItemDisplay {
        private String appName;
        private String appVersion;
        private String appVersionCode;
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

        public String getAppVersionCode() {
            return appVersionCode;
        }

        public void setAppVersionCode(String appVersionCode) {
            this.appVersionCode = appVersionCode;
        }

        public String getAppPackage() {
            return appPackage;
        }

        public void setAppPackage(String appPackage) {
            this.appPackage = appPackage;
        }

        @Override
        public void parseFrom(String[] segments) {
            setAppName(segments[0]);
            setAppVersion(segments[1]);
            setAppPackage(segments[2]);
        }

        @Override
        public String getDisplayName() {
            return getAppName();
        }

        @Override
        public String getDisplayVersion() {
            return getAppVersion();
        }

        @Override
        public String getDisplayExtra() {
            return getAppPackage();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            AppItem item = (AppItem) o;

            if (appName != null ? !appName.equals(item.appName) : item.appName != null)
                return false;
            if (appVersion != null ? !appVersion.equals(item.appVersion) : item.appVersion != null)
                return false;
            if (appVersionCode != null ? !appVersionCode.equals(item.appVersionCode) : item.appVersionCode != null)
                return false;
            return appPackage != null ? appPackage.equals(item.appPackage) : item.appPackage == null;
        }

        @Override
        public int hashCode() {
            int result = appName != null ? appName.hashCode() : 0;
            result = 31 * result + (appVersion != null ? appVersion.hashCode() : 0);
            result = 31 * result + (appVersionCode != null ? appVersionCode.hashCode() : 0);
            result = 31 * result + (appPackage != null ? appPackage.hashCode() : 0);
            return result;
        }
    }

    // device info
    public static class OsItem implements ItemParser, ItemDisplay  {
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

        @Override
        public void parseFrom(String[] segments) {
            setOsName(segments[0]);
            setOsVersion(segments[1]);
        }

        @Override
        public String getDisplayName() {
            return getOsName();
        }

        @Override
        public String getDisplayVersion() {
            return getOsVersion();
        }

        @Override
        public String getDisplayExtra() {
            return null;
        }
    }


    public static class HardwareItem implements ItemParser  {
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

        @Override
        public void parseFrom(String[] segments) {
            setHwModel(segments[0]);
            setHwId(segments[1]);
            setHwVendor(segments[2]);
        }
    }
}
