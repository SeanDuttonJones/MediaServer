package com.duttonjones.MediaServer.IngestEngineV2.comparators;

import java.io.File;
import java.util.Comparator;

public class FileNameComparator implements Comparator<File> {

    @Override
    public int compare(File o1, File o2) {
        return o1.getName().compareToIgnoreCase(o2.getName());
    }
}
