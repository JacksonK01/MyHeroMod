package net.michaeljackson23.mineademia.util;

public class IndexMap<K, V> {
    private Node<K, V> root;
    private int size = 0;

    public void put(K key, V value) {
        root = putHelper(key, value, root);
    }

    private Node<K, V> putHelper(K key, V value, Node<K, V> node) {
        if (node == null) {
            size++;
            return new Node<>(key, value, size - 1);
        }
        node.next = putHelper(key, value, node.next);
        return node;
    }

    public V getValue(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        return getValueHelper(index, root);
    }

    private V getValueHelper(int index, Node<K, V> node) {
        if (node == null) {
            return null;
        }
        if (index == node.getIndex()) {
            return node.value;
        }
        return getValueHelper(index, node.next);
    }

    public V getValue(K key) {
        return getValueByKeyHelper(key, root);
    }

    private V getValueByKeyHelper(K key, Node<K, V> node) {
        if (node == null) {
            return null;
        }
        if (key.equals(node.key)) {
            return node.value;
        }
        return getValueByKeyHelper(key, node.next);
    }

    public int getSize() {
        return size;
    }

    private static class Node<K, V> {
        Node<K, V> next;
        private final int index;
        private final K key;
        private final V value;

        private Node(K key, V value, int index) {
            this.key = key;
            this.value = value;
            this.index = index;
        }

        private int getIndex() {
            return index;
        }
    }
}
