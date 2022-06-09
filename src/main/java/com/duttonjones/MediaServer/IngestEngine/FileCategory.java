package com.duttonjones.MediaServer.IngestEngine;

import java.io.File;

public class FileCategory {

    private final String categoryName;
    private final String[] fileExtensions;

    public FileCategory(String categoryName, String[] fileExtensions) {
        this.categoryName = categoryName;
        this.fileExtensions = fileExtensions;
    }

    public boolean belongsInCategory(File file) {
        for(String ext : fileExtensions) {
            if(file.getName().endsWith(ext)) {
                return true;
            }
        }

        return false;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String[] getFileExtensions() {
        return fileExtensions;
    }
}
