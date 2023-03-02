package com.javainuse.threadHandler;
import java.nio.file.Files;
import java.nio.file.Path;

public class DirectoryDeleter implements Runnable {

    private final Path directoryPath;

    public DirectoryDeleter(Path directoryPath) {
        this.directoryPath = directoryPath;
    }

    @Override
    public void run() {
        try {
            Files.walk(directoryPath)
                    .sorted((path1, path2) -> -path1.compareTo(path2))
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (Exception e) {
                            // handle exception
                        }
                    });
        } catch (Exception e) {
            // handle exception
        }
    }
}
