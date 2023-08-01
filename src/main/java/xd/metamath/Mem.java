package xd.metamath;

import java.util.Map;
import java.util.TreeMap;

/*
Memory for pointers
 */
public class Mem<T> {

    private final Map<Long, T> content;
    private final int size;
    private final T defaultValue;

    public Mem(int size, T defaultValue) {
        this.size = size;
        this.defaultValue = defaultValue;
        content = new TreeMap<>();
    }

    public T get(int idx) {
        return content.getOrDefault(idx, defaultValue);
    }

    public int length() {
        return size;
    }

    public void set(long idx, T other) {
        content.put(idx,other);
    }
}
