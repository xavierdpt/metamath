package xd.metamath;

/**
 * Pointer wrapper
 */
public class P<T> {

    Mem<T> memory;
    private int idx = 0;

    public P() {
        memory=new Mem<>(0,null);
    }

    public T get() {
        return memory.get(idx);
    }

    public T get(int i) {
        return memory.get(i);
    }

    public int length() {
        return memory.length();
    }

    public void free() {
        memory=null;
    }

    public void malloc(int size, T defaultValue) {
        memory = new Mem<>(size, defaultValue);
    }

    public void set(T other) {
        memory.set(idx,other);
    }

}
