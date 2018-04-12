package com.harvestasm.apm.reporter;

import com.harvestasm.apm.repository.model.ApmSourceConnect;
import com.harvestasm.apm.repository.model.ApmSourceData;
import com.harvestasm.apm.repository.model.search.ApmBaseUnit;

import java.util.List;

// data和connect数据索引后以某个标识进行分组，如以device矩阵或者app矩阵，词库groupId. device矩阵是把
// 设备相关的信息，如厂商，型号等串在一起，app矩阵是把app的信息，如包名，应用名和版本等，串在一起.
// 同一个group里分别对性能data类数据和上报connection类数据进行索引，详见各自对应的类ApmConnectSourceIndex和
// ApmDataSourceIndex.
public class ApmSourceIndexGroup {
    private String groupId;
    private List<ApmBaseUnit<ApmSourceData>> dataSourceIndex;
    private List<ApmBaseUnit<ApmSourceConnect>> connectSourceIndex;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public List<ApmBaseUnit<ApmSourceData>> getDataSourceIndex() {
        return dataSourceIndex;
    }

    public void setDataSourceIndex(List<ApmBaseUnit<ApmSourceData>> dataSourceIndex) {
        this.dataSourceIndex = dataSourceIndex;
    }

    public List<ApmBaseUnit<ApmSourceConnect>> getConnectSourceIndex() {
        return connectSourceIndex;
    }

    public void setConnectSourceIndex(List<ApmBaseUnit<ApmSourceConnect>> connectSourceIndex) {
        this.connectSourceIndex = connectSourceIndex;
    }
}
