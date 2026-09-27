package com.github.bytecodebeaver.libraryapi.config;

import com.github.bytecodebeaver.libraryapi.security.exceptions.EmailAlreadyInUseException;
import com.github.bytecodebeaver.libraryapi.security.exceptions.InvalidPasswordException;
import com.github.bytecodebeaver.libraryapi.security.exceptions.UserNotFoundException;
import com.github.bytecodebeaver.libraryapi.exceptions.ResourceAlreadyExistsException;
import com.github.bytecodebeaver.libraryapi.exceptions.ResourceNotFoundException;
import com.github.bytecodebeaver.libraryapi.model.dto.ResourceAlreadyExistsDTO;
import com.github.bytecodebeaver.libraryapi.model.dto.ResourceNotFoundDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return new ResponseEntity<ResourceNotFoundDTO>(new ResourceNotFoundDTO(ex), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<?> handleResourceAlreadyExistsException(ResourceAlreadyExistsException ex) {
        return new ResponseEntity<ResourceAlreadyExistsDTO>(new ResourceAlreadyExistsDTO(ex), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EmailAlreadyInUseException.class)
    public ResponseEntity<?> handleEmailAlreadyInUseException(EmailAlreadyInUseException ex) {
        return new ResponseEntity<>("EMAIL_ALREADY_IN_USE", HttpStatus.CONFLICT);
    }

    @ExceptionHandler({InvalidPasswordException.class, UserNotFoundException.class})
    public ResponseEntity<?> handleInvalidCredentials(RuntimeException ex) {
        return new ResponseEntity<>("INVALID_CREDENTIALS", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<?> handleDisabledAccount(DisabledException ex) {
        return new ResponseEntity<>("ACCOUNT_DISABLED", HttpStatus.FORBIDDEN);
    }
}
