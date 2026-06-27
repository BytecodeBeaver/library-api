package com.github.bytecodebeaver.libraryapi.model.dto;

import com.github.bytecodebeaver.libraryapi.exceptions.ResourceNotFoundException;

public record ResourceNotFoundDTO(String resourceId, String message) {
    public ResourceNotFoundDTO(ResourceNotFoundException resourceNotFoundException) {
        this(resourceNotFoundException.getResourceId(), resourceNotFoundException.getMessage());
    }
}
