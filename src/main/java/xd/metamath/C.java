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

}
