package com.github.bytecodebeaver.libraryapi.exceptions;

public class ResourceAlreadyExistsException extends RuntimeException {
    public ResourceAlreadyExistsException(String message) {
        super(message);
    }
    public ResourceAlreadyExistsException() {
        super("Resource already exists");
    }
}
