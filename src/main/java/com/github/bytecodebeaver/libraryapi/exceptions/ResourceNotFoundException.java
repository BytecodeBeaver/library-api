package com.github.bytecodebeaver.libraryapi.exceptions;

import lombok.Getter;

public class ResourceNotFoundException extends RuntimeException {
    @Getter
    private final String resourceId;

    public ResourceNotFoundException(String resourceId, String message) {
        super(message);
        this.resourceId = resourceId;
    }
}
