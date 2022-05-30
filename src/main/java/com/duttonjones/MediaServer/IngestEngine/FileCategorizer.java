package com.duttonjones.MediaServer.IngestEngine;

import java.io.File;
import java.io.FileFilter;
import java.nio.file.NotDirectoryException;
import java.util.*;

public class FileCategorizer {

    private final File directory;
    private final Hashtable<String, FileCluster> fileClusters;
    private final ArrayList<FileCategory> fileCategories;

    public FileCategorizer(File directory) throws NotDirectoryException {
        if(!directory.isDirectory()) {
            throw new NotDirectoryException("File is not a directory!");
        }

        this.directory = directory;

        this.fileClusters = new Hashtable<>();
        this.fileCategories = new ArrayList<>();
    }

    public void addCategory(FileCategory category) {
        fileCategories.add(category);

        FileCluster fileCluster = new FileCluster(category.getCategoryName());
        fileClusters.put(category.getCategoryName(), fileCluster);
    }

    public void categorize() {
        LinkedList<File> directories = new LinkedList<>();
        directories.add(this.directory);

        while(!directories.isEmpty()) {
            File directory = directories.poll();
            if(directory == null) {
                continue;
            }

            for(FileCategory category : fileCategories) {
                File[] files = directory.listFiles(new FileFilter() {
                    @Override
                    public boolean accept(File file) {
                        if(file.isDirectory()) {
                            if(!directories.contains(file)) {
                                directories.add(file);
                            }

                            return false;
                        }

                        return category.belongsInCategory(file);
                    }
                });

                if(files == null) {
                    return;
                }

                fileClusters.get(category.getCategoryName()).addAll(Arrays.asList(files));
            }
        }
    }

    public Dictionary<String, FileCluster> getFileClusters() {
        return fileClusters;
    }

    public FileCluster getFileCluster(String category) {
        if(!fileClusters.containsKey(category)) {
            throw new CategoryNotFoundException();
        }

        return fileClusters.get(category);
    }

    public ArrayList<FileCategory> getFileCategories() {
        return fileCategories;
    }
}
