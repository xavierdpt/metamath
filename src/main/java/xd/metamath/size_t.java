package xd.metamath;

public class size_t {
    private int value;

    public size_t(int value) {
        this.value = value;
    }

    public static size_t of(int value) {
        return new size_t(value);
    }

    public static boolean lt(size_t left, size_t right) {
        return left.value<right.value;
    }

    public boolean toBoolean() {
        return value != 0;
    }

    public long toLong() {
        return value;
    }

    public int getValue() {
        return value;
    }

    public void pluseq(size_t other) {
        value+=other.value;
    }
}
