package com.harvestasm.apm.reporter;

import com.harvestasm.apm.repository.model.ApmActivityItem;
import com.harvestasm.apm.repository.model.ApmConnectSearchResponse;
import com.harvestasm.apm.repository.model.ApmDataSearchResponse;
import com.harvestasm.apm.repository.model.ApmDeviceMicsItem;
import com.harvestasm.apm.repository.model.ApmMeasurementItem;
import com.harvestasm.apm.repository.model.ApmTransactionItem;

/**
 * Created by yangfeng on 2018/3/20.
 */

public class SearchResult {
    public static class Node<T, P> {
        private T node;
        private P parent;

        public Node(T node, P parent) {
            this.node = node;
            this.parent = parent;
        }

        public T getNode() {
            return node;
        }

        public void setNode(T node) {
            this.node = node;
        }

        public P getParent() {
            return parent;
        }

        public void setParent(P parent) {
            this.parent = parent;
        }
    }

    public static class NodeConnectWrapper extends Node<ApmConnectSearchResponse.ConnectWrapper, ApmConnectSearchResponse> {
        public NodeConnectWrapper(ApmConnectSearchResponse.ConnectWrapper node, ApmConnectSearchResponse parent) {
            super(node, parent);
        }
    }

    public static class NodeConnectUnit extends Node<ApmConnectSearchResponse.ConnectUnit, NodeConnectWrapper> {
        public NodeConnectUnit(ApmConnectSearchResponse.ConnectUnit node, NodeConnectWrapper parent) {
            super(node, parent);
        }
    }

    public static class NodeSourceTypeConnect extends Node<ApmConnectSearchResponse.SourceTypeConnect, NodeConnectUnit> {
        public NodeSourceTypeConnect(ApmConnectSearchResponse.SourceTypeConnect node, NodeConnectUnit parent) {
            super(node, parent);
        }
    }

    public static class NodeApmDeviceMicsItem extends Node<ApmDeviceMicsItem, NodeSourceTypeConnect> {
        public NodeApmDeviceMicsItem(ApmDeviceMicsItem node, NodeSourceTypeConnect parent) {
            super(node, parent);
        }
    }

    public static class NodeDataWrapper extends Node<ApmDataSearchResponse.DataWrapper, ApmDataSearchResponse> {
        public NodeDataWrapper(ApmDataSearchResponse.DataWrapper data, ApmDataSearchResponse parent) {
            super(data, parent);
        }
    }

    public static class NodeDataUnit extends Node<ApmDataSearchResponse.DataUnit, NodeDataWrapper> {
        public NodeDataUnit(ApmDataSearchResponse.DataUnit data, NodeDataWrapper parent) {
            super(data, parent);
        }
    }

    public static class NodeSourceType extends Node<ApmDataSearchResponse.SourceTypeData, NodeDataUnit> {
        public NodeSourceType(ApmDataSearchResponse.SourceTypeData data, NodeDataUnit parent) {
            super(data, parent);
        }
    }

    public static class NodeApmTransactionItem extends Node<ApmTransactionItem, NodeSourceType> {
        public NodeApmTransactionItem(ApmTransactionItem data, NodeSourceType parent) {
            super(data, parent);
        }
    }

    public static class NodeApmMeasurementItem extends Node<ApmMeasurementItem, NodeSourceType> {
        public NodeApmMeasurementItem(ApmMeasurementItem data, NodeSourceType parent) {
            super(data, parent);
        }
    }

    public static class NodeApmActivityItem extends Node<ApmActivityItem, NodeSourceType> {
        public NodeApmActivityItem(ApmActivityItem data, NodeSourceType parent) {
            super(data, parent);
        }
    }
}
