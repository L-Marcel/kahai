package org.kahai.framework.errors;

import org.springframework.http.HttpStatus;

public class GameStorageException extends HttpError {

   public GameStorageException(String message, HttpStatus status) {
       super(message, status);
    }
    
    public GameStorageException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}