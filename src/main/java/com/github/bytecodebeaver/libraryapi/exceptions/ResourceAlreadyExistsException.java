package com.github.bytecodebeaver.libraryapi.exceptions;

import lombok.Getter;

public class ResourceAlreadyExistsException extends RuntimeException {
   @Getter
   private final String resourceId;

   public ResourceAlreadyExistsException(String resourceId, String message) {
       super(message);
       this.resourceId = resourceId;
   }
}
