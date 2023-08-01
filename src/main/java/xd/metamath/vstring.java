package xd.metamath;

public class vstring {
    P<Byte> content;
    public vstring() {
        this.content = new P<>();
    }

    public vstring(P<Byte> content) {
        this.content = content;
    }

    public int length() {
        for (int i = 0; i < content.length(); ++i) {
            if (D.BZERO.equals(content.get(i))) {
                return i;
            }
        }
        return 0;
    }

    public void free() {
        content.free();
    }

    public void malloc(int size) {
        content.malloc(size, D.BZERO);
    }

    public static vstring empty() {
        return new vstring();
    }

    public P<Byte> toCharPointer() {
        return content;
    }

    public static vstring from(P<Byte> p) {
        return new vstring(p);
    }
}
