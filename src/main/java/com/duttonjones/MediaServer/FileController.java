package com.duttonjones.MediaServer;

import com.duttonjones.MediaServer.IngestEngine.*;
import com.duttonjones.MediaServer.IngestEngine.exceptions.CategoryNotFoundException;
import com.duttonjones.MediaServer.IngestEngine.exceptions.InvalidParamterException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.nio.file.NotDirectoryException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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

    @GetMapping("/")
    public ResponseEntity<Object> welcome() {
        Map<String, Object> message = new HashMap<>();
        message.put("message", "welcome");

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping(value = "files/{category}/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public FileCluster listFiles(@PathVariable("category") String category,
                                 @RequestParam(name = "sortBy", required = false) String sortBy,
                                 @RequestParam(name = "reversed", required = false, defaultValue = "false") boolean reversed) {

        FileCluster fileCluster = fileCategorizer.getFileCluster(category);

        if(sortBy != null) {
            switch (sortBy) {
                case "creationDate" -> fileCluster.sort(new FileCreationDateComparator());
                case "lastModified" -> fileCluster.sort(new FileModifiedDateComparator());
                case "name" -> fileCluster.sort(new FileNameComparator());
                default -> throw new InvalidParamterException("sortBy: unknown sort method '" + sortBy + "'");
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

    @ExceptionHandler(value = {CategoryNotFoundException.class, InvalidParamterException.class})
    public ResponseEntity<Object> exceptionHandler(RuntimeException e) {
        Map<String, Object> errResponse = new HashMap<>();

        errResponse.put("error", HttpStatus.BAD_REQUEST.value());
        errResponse.put("type", HttpStatus.BAD_REQUEST);
        errResponse.put("message", e.getMessage());

        return new ResponseEntity<>(errResponse, HttpStatus.BAD_REQUEST);
    }
}
