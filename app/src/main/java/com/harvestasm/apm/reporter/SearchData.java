package com.harvestasm.apm.reporter;

import com.harvestasm.apm.repository.model.ApmActivityItem;
import com.harvestasm.apm.repository.model.ApmDataSearchResponse;
import com.harvestasm.apm.repository.model.ApmMeasurementItem;
import com.harvestasm.apm.repository.model.ApmTransactionItem;

/**
 * Created by yangfeng on 2018/3/20.
 */

public class SearchData {
    public static class Node<T, P> {
        private T data;
        private P parent;

        public Node(T data, P parent) {
            this.data = data;
            this.parent = parent;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }

        public P getParent() {
            return parent;
        }

        public void setParent(P parent) {
            this.parent = parent;
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
