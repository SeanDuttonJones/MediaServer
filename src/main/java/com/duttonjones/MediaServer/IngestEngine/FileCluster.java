package com.duttonjones.MediaServer.IngestEngine;

import java.io.File;
import java.util.ArrayList;

public class FileCluster extends ArrayList<File> implements Comparable<File> {

    private final String clusterName;

    public FileCluster(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getClusterName() {
        return clusterName;
    }

    @Override
    public int compareTo(File o) {
        return 0;
    }
}
