package com.github.bytecodebeaver.libraryapi.model.dto;

import com.github.bytecodebeaver.libraryapi.exceptions.ResourceAlreadyExistsException;

public record ResourceAlreadyExistsDTO(String resourceId, String message) {
    public ResourceAlreadyExistsDTO(ResourceAlreadyExistsException ex) {
        this(ex.getResourceId(), ex.getMessage());
    }
}
