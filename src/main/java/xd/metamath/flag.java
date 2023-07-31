package xd.metamath;

import java.util.Objects;

public final class flag {
    byte content; // Map 8-bit C char to 8-bit Java byte

    public flag(byte content) {
        this.content = content;
    }

    public static flag of(byte i) {
        return new flag(i);
    }

    public static flag of(int i) {
        return new flag((byte) i);
    }

    public void set(int content) {
        this.content= (byte) content;
    }
    public void set(byte content) {
        this.content=content;
    }

    public void set(flag other) {
        this.content=other.content;
    }

    public boolean asBoolean() {
        return content != 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        flag flag = (flag) o;
        return content == flag.content;
    }

    @Override
    public int hashCode() {
        return Objects.hash(content);
    }
}
