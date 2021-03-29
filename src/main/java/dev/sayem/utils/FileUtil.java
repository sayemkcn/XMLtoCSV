package dev.sayem.utils;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class FileUtil {
    private static FileUtil INSTANCE;
    private static final List<File> files = new ArrayList<>();

    private FileUtil() {
    }

    public static FileUtil getInstance() {
        files.clear();
        if (INSTANCE == null)
            INSTANCE = new FileUtil();
        return INSTANCE;
    }

    public List<File> listFiles(File dir) {
        if (!dir.isDirectory() && dir.isFile()) return Collections.singletonList(dir);
        Arrays.stream(Objects.requireNonNull(dir.listFiles())).forEach(f -> {
            if (f.isDirectory()) listFiles(f);
            else if (f.isFile()) files.add(f);
        });
        return files;
    }

    public static File createTempDirectory() throws IOException {
        final File temp;

        temp = File.createTempFile("temp", Long.toString(System.nanoTime()));

        if (!(temp.delete())) {
            throw new IOException("Could not delete temp file: " + temp.getAbsolutePath());
        }

        if (!(temp.mkdir())) {
            throw new IOException("Could not create temp directory: " + temp.getAbsolutePath());
        }

        return (temp);
    }
}
