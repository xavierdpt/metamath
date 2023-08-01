package xd.metamath;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class C {

    static Map<File, InputStream> inputStreams = new HashMap<>();

    public static void go2(String destination) {
        // Placeholder to identify call sites for goto statements, and make the syntax valid
    }

    public static void label(String label) {
        // Placeholder to identify call sites for label statements, and make the syntax valid
    }

    public static void fclose(File file) {
        InputStream inputStream = inputStreams.get(file);
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException exception) {
                throw new RuntimeException(exception);
            }
            inputStreams.remove(file);
        }
    }

    public static int strlen(vstring s) {
        return s.length();
    }
    public static int strlen(P<Byte> bytes) {
        return bytes.length();
    }


    public static int strlen(String s) {
        return s.length();
    }

    static public void free(vstring str) {
        str.free();
    }
    static public <T> void free(T what) {

    }

    static public void malloc(vstring v, int size) {
        v.malloc(size);
    }

    static public void strcpy(vstring left, vstring right) {

    }

    public static void printf(String str) {
        System.out.println(str);
    }



}
