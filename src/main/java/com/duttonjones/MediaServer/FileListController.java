package com.duttonjones.MediaServer;

import com.duttonjones.MediaServer.IngestEngine.exceptions.CategoryNotFoundException;
import com.duttonjones.MediaServer.IngestEngine.exceptions.InvalidParamterException;
import com.duttonjones.MediaServer.IngestEngineV2.FileExtensionFilter;
import com.duttonjones.MediaServer.IngestEngineV2.comparators.FileCreationDateComparator;
import com.duttonjones.MediaServer.IngestEngineV2.comparators.FileModifiedDateComparator;
import com.duttonjones.MediaServer.IngestEngineV2.comparators.FileNameComparator;
import com.duttonjones.MediaServer.IngestEngineV2.core.IngestEngine;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class FileListController {
    private final File defaultMediaDirectory;
    private final Map<String, FileFilter> fileExtensionFilters;
    private final Map<String, Comparator<File>> sortMethods;
    public FileListController() {
        defaultMediaDirectory = new File(IngestEngine.getInstance().getProperties().getProperty("MEDIA_DIRECTORY"));

        fileExtensionFilters = new HashMap<>();
        fileExtensionFilters.put("images", new FileExtensionFilter(new String[]{".jpg", ".png"}));
        fileExtensionFilters.put("videos", new FileExtensionFilter(new String[]{".mp4"}));
        fileExtensionFilters.put("documents", new FileExtensionFilter(new String[]{".txt", ".docx"}));

        sortMethods = new HashMap<>();
        sortMethods.put("creationDate", new FileCreationDateComparator());
        sortMethods.put("lastModified", new FileModifiedDateComparator());
        sortMethods.put("name", new FileNameComparator());
    }

    @GetMapping(value = "files/{type}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<File> listFiles(@PathVariable("type") String type,
                                @RequestParam(name = "sortBy", required = false, defaultValue = "creationDate") String sortBy,
                                @RequestParam(name = "reversed", required = false, defaultValue = "false") boolean reversed)
            throws IOException {

        if(!fileExtensionFilters.containsKey(type)) {
            throw new CategoryNotFoundException("File Type: unknown file type '" + type + "'");
        }

        if(!sortMethods.containsKey(sortBy)) {
            throw new InvalidParamterException("sortBy: unknown sort method '" + sortBy + "'");
        }

        List<File> files;
        try(Stream<Path> walk = Files.walk(defaultMediaDirectory.toPath())) {
            files = walk.map(Path::toFile)
                    .filter(fileExtensionFilters.get(type)::accept)
                    .sorted((reversed ? sortMethods.get(sortBy).reversed() : sortMethods.get(sortBy)))
                    .collect(Collectors.toList());
        }

        return files;
    }

    @ExceptionHandler(value = {CategoryNotFoundException.class, InvalidParamterException.class, IllegalArgumentException.class})
    public ResponseEntity<Object> exceptionHandler(RuntimeException e) {
        Map<String, Object> errResponse = new HashMap<>();

        errResponse.put("error", HttpStatus.BAD_REQUEST.value());
        errResponse.put("type", HttpStatus.BAD_REQUEST);
        errResponse.put("message", e.getMessage());

        return new ResponseEntity<>(errResponse, HttpStatus.BAD_REQUEST);
    }
}
