package com.harvestasm.apm.reporter;

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
}
