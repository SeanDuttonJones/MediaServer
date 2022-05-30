package com.duttonjones.MediaServer;

import com.duttonjones.MediaServer.IngestEngine.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.nio.file.NotDirectoryException;
import java.util.ArrayList;
import java.util.Collections;

@RestController
public class FileController {

    private FileCategorizer fileCategorizer;

    public FileController() {
        try {
            File file = new File("/home/dev/Desktop/MediaServerTest");
            fileCategorizer = new FileCategorizer(file);

            FileCategory documentCat = new FileCategory("documents", new String[]{".txt"});
            FileCategory imageCat = new FileCategory("images", new String[]{".jpg"});
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

    // TODO: ADD sortBy parameter to allow sorting by creation date, modified, name, etc...
    @GetMapping(value = "/files/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public FileCluster listFiles(@RequestParam(name = "category") String category,
                                 @RequestParam(name = "sortBy", required = false) String sortBy,
                                 @RequestParam(name = "reversed", defaultValue = "false") boolean reversed) {

        FileCluster fileCluster = fileCategorizer.getFileCluster(category);

        if(sortBy != null) {
            switch (sortBy) {
                case "creationDate" -> fileCluster.sort(new FileCreationDateComparator());
                case "lastModified" -> fileCluster.sort(new FileModifiedDateComparator());
                case "name" -> fileCluster.sort(new FileNameComparator());
            }

            if(reversed) {
                Collections.reverse(fileCluster);
            }
        }

        return fileCluster;
    }

    @GetMapping("/categories")
    public ArrayList<FileCategory> getCategories() {
        return fileCategorizer.getFileCategories();
    }

    @ExceptionHandler(value = CategoryNotFoundException.class)
    public ResponseEntity<Object> categoryNotFoundException(CategoryNotFoundException e) {
        return new ResponseEntity<>("No such category exists!", HttpStatus.BAD_REQUEST);
    }
}
