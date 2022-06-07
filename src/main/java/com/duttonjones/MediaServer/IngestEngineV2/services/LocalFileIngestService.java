package com.duttonjones.MediaServer.IngestEngineV2.services;

import com.duttonjones.MediaServer.IngestEngineV2.core.IngestService;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LocalFileIngestService extends IngestService {

    private final File parentDirectory;
    private final ArrayList<File> localFiles;

    //TODO: Implement this Service
    public LocalFileIngestService(String name) {
        super(name);

        parentDirectory = new File("/home/dev/Desktop/MediaServerTest");
        localFiles = new ArrayList<>();
    }

//    private void ingestLocalFiles() {
//        LinkedList<File> directories = new LinkedList<>();
//        directories.add(parentDirectory);
//
//        while(!directories.isEmpty()) {
//            File directory = directories.poll();
//
//            File[] files = directory.listFiles();
//            if(files == null) {
//                continue;
//            }
//
//            for(File f : files) {
//                if(f.isDirectory() && !directories.contains(f)) {
//                    directories.add(f);
//                }
//            }
//
//            localFiles.addAll(Arrays.asList(files));
//        }
//    }

    private ArrayList<File> listFiles(Path path) throws IOException {
        List<File> files;
        try(Stream<Path> walk = Files.walk(path)) {
            files = walk.filter(Files::isRegularFile).map(Path::toFile).collect(Collectors.toList());
        }

        return (ArrayList<File>) files;
    }

    private void handleParentDirectoryChange(WatchEvent<?> event) {
        File file = new File(parentDirectory.getPath() + "/" + event.context());

        if (event.kind().equals(StandardWatchEventKinds.ENTRY_CREATE)) {
            localFiles.add(file);
        } else if(event.kind().equals(StandardWatchEventKinds.ENTRY_DELETE)) {
            localFiles.remove(file);
        }
    }

    // TODO: Put this onto a separate Thread so that it can run without blocking the service
    // TODO: Make sure that files within subfolders are being watched too I.E., register subfolders with the WatchService
    private void startParentDirectoryWatchService() throws IOException, InterruptedException {
        WatchService watchService = FileSystems.getDefault().newWatchService();

        Path path = Path.of(parentDirectory.getPath());

        path.register(watchService,
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_MODIFY,
                StandardWatchEventKinds.ENTRY_DELETE);

        WatchKey key;
        while((key = watchService.take()) != null) {
            for(WatchEvent<?> event : key.pollEvents()) {
                handleParentDirectoryChange(event);
            }

            key.reset();
        }
    }

    @Override
    public void start() {
        super.start();
        System.out.println("LocalFileIngestService: starting...");

//        try {
//            localFiles.addAll(listFiles(parentDirectory.toPath()));
//            startParentDirectoryWatchService();
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void stop() {
        super.stop();
        System.out.println("LocalFileIngestService: stopping...");
    }

    public ArrayList<File> getLocalFiles() {
        return localFiles;
    }
    public ArrayList<File> getLocalFiles(Comparator<File> comparator) {
        ArrayList<File> sortedFiles = new ArrayList<>(localFiles);
        sortedFiles.sort(comparator);
        return sortedFiles;
    }
}