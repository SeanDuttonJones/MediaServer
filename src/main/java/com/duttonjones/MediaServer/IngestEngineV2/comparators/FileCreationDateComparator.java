package com.duttonjones.MediaServer.IngestEngineV2.comparators;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Comparator;

public class FileCreationDateComparator implements Comparator<File> {
    @Override
    public int compare(File o1, File o2) {
        BasicFileAttributes o1Attrs = null;
        BasicFileAttributes o2Attrs = null;

        try {
            o1Attrs = Files.readAttributes(o1.toPath(), BasicFileAttributes.class);
            o2Attrs = Files.readAttributes(o2.toPath(), BasicFileAttributes.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert o1Attrs != null;
        assert o2Attrs != null;

        if(o1Attrs.creationTime().toMillis() < o2Attrs.creationTime().toMillis()) {
            return -1;
        } else if(o1Attrs.creationTime().toMillis() > o2Attrs.creationTime().toMillis()) {
            return 1;
        }

        return 0;
    }
}
