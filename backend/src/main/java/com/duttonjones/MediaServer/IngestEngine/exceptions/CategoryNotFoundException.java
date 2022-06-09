package com.duttonjones.MediaServer.IngestEngine.exceptions;

public class CategoryNotFoundException extends RuntimeException {

    public CategoryNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
