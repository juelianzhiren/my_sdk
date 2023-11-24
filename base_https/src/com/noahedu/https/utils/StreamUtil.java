package com.noahedu.https.utils;

import java.io.Closeable;
import java.io.IOException;

public class StreamUtil {

    public static void closeStream(Closeable stream) {
        if (stream == null) {
            return;
        }
        try {
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
