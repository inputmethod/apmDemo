package com.harvestasm.apm.reporter;

import com.harvestasm.apm.repository.model.ApmSourceConnect;
import com.harvestasm.apm.repository.model.ApmSourceData;
import com.harvestasm.apm.repository.model.search.ApmBaseUnit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

// data和connect数据索引后以某个标识进行分组，如以device矩阵或者app矩阵，词库groupId. device矩阵是把
// 设备相关的信息，如厂商，型号等串在一起，app矩阵是把app的信息，如包名，应用名和版本等，串在一起.
// 同一个group里分别对性能data类数据和上报connection类数据进行索引，详见各自对应的类ApmConnectSourceIndex和
// ApmDataSourceIndex.
public class ApmSourceGroup {
    private String groupId;
    private List<ApmBaseUnit<ApmSourceData>> dataSource;
    private List<ApmBaseUnit<ApmSourceConnect>> connectSource;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public List<ApmBaseUnit<ApmSourceData>> getDataSource() {
        return dataSource;
    }

    public void setDataSource(List<ApmBaseUnit<ApmSourceData>> dataSource) {
        this.dataSource = dataSource;
    }

    public List<ApmBaseUnit<ApmSourceConnect>> getConnectSource() {
        return connectSource;
    }

    public void setConnectSource(List<ApmBaseUnit<ApmSourceConnect>> connectSource) {
        this.connectSource = connectSource;
    }

    public static List<ApmSourceGroup> parseSourceGroup(ApmDataSourceIndex dataSourceIndex,
                                                        ApmConnectSourceIndex connectSourceIndex) {
        selfTestDevice(dataSourceIndex, connectSourceIndex);

        HashMap<String, List<ApmBaseUnit<ApmSourceData>>> dataUnits = dataSourceIndex.getDeviceIdIndexMap();
        HashMap<String, List<ApmBaseUnit<ApmSourceConnect>>> connectUnits = connectSourceIndex.getDeviceIdIndexMap();
        Set<String> devicesOfData = dataUnits.keySet();
        Set<String> devicesOfConnect = connectUnits.keySet();

        List<ApmSourceGroup> groups = new ArrayList<>();
        for (String device : devicesOfData) {
            ApmSourceGroup g = new ApmSourceGroup();
            g.setGroupId(device);
            g.setDataSource(dataUnits.get(device));
            g.setConnectSource(connectUnits.get(device));
            devicesOfConnect.remove(device);
            groups.add(g);
        }

        for (String device : devicesOfConnect) {
            ApmSourceGroup g = new ApmSourceGroup();
            g.setGroupId(device);
            g.setDataSource(null);
            g.setConnectSource(connectUnits.get(device));
            groups.add(g);
        }

        return groups;
    }

    public static void selfTestDevice(ApmDataSourceIndex dataSourceIndex,
                                      ApmConnectSourceIndex connectSourceIndex) {
        HashMap<String, List<ApmBaseUnit<ApmSourceData>>> dataByDevice = dataSourceIndex.getDeviceIdIndexMap();
        HashMap<String, List<ApmBaseUnit<ApmSourceConnect>>> connectByDevice = connectSourceIndex.getDeviceIdIndexMap();

        Set<String> connectDeviceSet = connectByDevice.keySet();
        Set<String> dataDeviceSet = dataByDevice.keySet();

        // verify device id within connect and data source.
        ArrayList<String> noMatchConnectDevice = new ArrayList<>(connectDeviceSet);
        noMatchConnectDevice.removeAll(dataDeviceSet);

        ArrayList<String> noMatchDataDevice = new ArrayList<>(dataDeviceSet);
        noMatchDataDevice.removeAll(connectDeviceSet);
    }
}
