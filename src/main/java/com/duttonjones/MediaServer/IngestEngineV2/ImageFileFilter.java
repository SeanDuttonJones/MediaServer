package com.duttonjones.MediaServer.IngestEngineV2;

import java.io.File;
import java.io.FileFilter;

public class ImageFileFilter implements FileFilter {

    private final String[] extensions;

    public ImageFileFilter() {
        extensions = new String[]{".jpg", ".png"};
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
