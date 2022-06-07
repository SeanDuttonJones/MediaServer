package com.duttonjones.MediaServer;

import com.duttonjones.MediaServer.IngestEngine.*;
import com.duttonjones.MediaServer.IngestEngine.exceptions.CategoryNotFoundException;
import com.duttonjones.MediaServer.IngestEngine.exceptions.InvalidParamterException;
import com.duttonjones.MediaServer.IngestEngineV2.FileLister;
import com.duttonjones.MediaServer.IngestEngineV2.ImageFileFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.NotDirectoryException;
import java.util.*;

@RestController
public class FileController {

    private FileCategorizer fileCategorizer;

    public FileController() {
        try {
//            File file = new File("/home/dev/Desktop/MediaServerTest");
            File file = new File("/Users/seanduttonjones/Desktop/MediaServerTest");
            fileCategorizer = new FileCategorizer(file);

            FileCategory documentCat = new FileCategory("documents", new String[]{".txt"});
            FileCategory imageCat = new FileCategory("images", new String[]{".jpg", ".png"});
            FileCategory videoCat = new FileCategory("videos", new String[]{".mp4"});
            FileCategory allCat = new FileCategory("all", new String[]{""});

            fileCategorizer.addCategory(documentCat);
            fileCategorizer.addCategory(imageCat);
            fileCategorizer.addCategory(videoCat);
            fileCategorizer.addCategory(allCat);

            fileCategorizer.categorize();
        } catch (NotDirectoryException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/")
    public ResponseEntity<Object> welcome() {
        Map<String, Object> message = new HashMap<>();
        message.put("message", "welcome");

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

//    @GetMapping(value = "files/{category}/list", produces = MediaType.APPLICATION_JSON_VALUE)
//    public FileCluster listFiles(@PathVariable("category") String category,
//                                 @RequestParam(name = "sortBy", required = false) String sortBy,
//                                 @RequestParam(name = "reversed", required = false, defaultValue = "false") boolean reversed) {
//
//        FileCluster fileCluster = fileCategorizer.getFileCluster(category);
//
//        if(sortBy != null) {
//            switch (sortBy) {
//                case "creationDate" -> fileCluster.sort(new FileCreationDateComparator());
//                case "lastModified" -> fileCluster.sort(new FileModifiedDateComparator());
//                case "name" -> fileCluster.sort(new FileNameComparator());
//                default -> throw new InvalidParamterException("sortBy: unknown sort method '" + sortBy + "'");
//            }
//
//            if(reversed) {
//                Collections.reverse(fileCluster);
//            }
//        }
//
//        return fileCluster;
//    }

    @GetMapping(value = "files/{category}/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<File> listFiles(@PathVariable("category") String category,
                                 @RequestParam(name = "sortBy", required = false) String sortBy,
                                 @RequestParam(name = "reversed", required = false, defaultValue = "false") boolean reversed)
            throws IOException {

        FileLister fileLister = new FileLister(new File("/Users/seanduttonjones/Desktop/MediaServerTest"));
        List<File> files = fileLister.listFiles();
        if(sortBy != null) {
            switch (sortBy) {
                case "creationDate" -> files = fileLister.listFiles(new ImageFileFilter(), new FileCreationDateComparator());
            }
        }

        if(reversed) {
            Collections.reverse(files);
        }

        return files;
    }

    @GetMapping("/categories")
    public ArrayList<FileCategory> getCategories() {
        return fileCategorizer.getFileCategories();
    }

    @ExceptionHandler(value = {CategoryNotFoundException.class, InvalidParamterException.class})
    public ResponseEntity<Object> exceptionHandler(RuntimeException e) {
        Map<String, Object> errResponse = new HashMap<>();

        errResponse.put("error", HttpStatus.BAD_REQUEST.value());
        errResponse.put("type", HttpStatus.BAD_REQUEST);
        errResponse.put("message", e.getMessage());

        return new ResponseEntity<>(errResponse, HttpStatus.BAD_REQUEST);
    }
}
