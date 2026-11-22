package com.anirudh.demo.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
     @ExceptionHandler(SeatNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(SeatNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(404,ex.getMessage()));

    }
    @ExceptionHandler(SeatAlreadyBookedException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyBooked(SeatAlreadyBookedException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(409,ex.getMessage()));
    }
    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<ErrorResponse> handleOptimisticLock(OptimisticLockingFailureException ex){
         return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(409, " Seat was just booked by someone else. "));
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex){
         String message = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(400,message));
    }
    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUsernametaken(UsernameAlreadyExistsException ex){
         return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(409, ex.getMessage()));
    }
    @ExceptionHandler(SeatAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleSeatExists(SeatAlreadyExistsException ex){
         return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(409,ex.getMessage()));
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleIntegrity(DataIntegrityViolationException ex){
         return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(409,"Duplicate or Invalid data"));
    }
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentials(InvalidCredentialsException ex){
         return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                 .body(new ErrorResponse(401, ex.getMessage()));
    }
}
