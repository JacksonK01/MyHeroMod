package net.michaeljackson23.mineademia.util;

//Util object for storing immutable data
public class MutableObject<T> {
    private T data;

    public MutableObject(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
