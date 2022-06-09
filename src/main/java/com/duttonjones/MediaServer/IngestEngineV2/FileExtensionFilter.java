package com.duttonjones.MediaServer.IngestEngineV2;

import java.io.File;
import java.io.FileFilter;

public class FileExtensionFilter implements FileFilter {

    private final String[] extensions;

    public FileExtensionFilter(String[] extensions) {
        this.extensions = extensions;
    }

    @Override
    public boolean accept(File pathname) {
        for(String ext : extensions) {
            if(pathname.getName().endsWith(ext)) {
                return true;
            }
        }

        return false;
    }
}
