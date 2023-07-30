package xd.metamath;

import java.util.Objects;

public final class flag {
    byte content; // Map 8-bit C char to 8-bit Java byte

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
