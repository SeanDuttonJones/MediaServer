package com.duttonjones.MediaServer.IngestEngineV2;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// TODO: Possibly integrate this as a IngestService. Maybe services should have their own configuration?
public class FileLister {
    private final File directory;
    private int maxDepth;

    public FileLister(File directory) {
        this.directory = directory;
        maxDepth = Integer.MAX_VALUE;
    }
    private List<File> _listFiles(FileFilter fileFilter) throws IOException {
        List<File> files;
        try(Stream<Path> walk = Files.walk(directory.toPath(), maxDepth)) {
            files = walk.map(Path::toFile)
                    .filter(fileFilter::accept)
                    .collect(Collectors.toList());
        }

        return files;
    }
    public List<File> listFiles() throws IOException {
        return _listFiles(pathname -> true);
    }

    public List<File> listFiles(Comparator<File> comparator) throws IOException {
        List<File> files = listFiles();
        files.sort(comparator);

        return files;
    }

    public List<File> listFiles(FileFilter fileFilter) throws IOException {
        return _listFiles(fileFilter);
    }

    public List<File> listFiles(FileFilter fileFilter, Comparator<File> comparator) throws IOException {
        List<File> files = _listFiles(fileFilter);
        files.sort(comparator);

        return files;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public void setMaxDepth(int maxDepth) {
        this.maxDepth = maxDepth;
    }
}
