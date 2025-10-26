package app.web;

import app.exception.PartyCreationDeniedException;
import app.exception.PartyDeleteDeniedException;
import app.exception.UserAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class ExceptionAdvice {


    @ExceptionHandler(PartyCreationDeniedException.class)
    public ResponseEntity<String> handleCreationDenied(){
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body("Only admins can create a party!");
    }

    @ExceptionHandler(PartyDeleteDeniedException.class)
    public ResponseEntity<String> handleDeleteDenied(){
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body("Only admins can delete a party!");
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<String> handleUserExistsException(){
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body("User with this username is already registered");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleAnyException(Exception exception){
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", exception.getMessage()));
    }
}
