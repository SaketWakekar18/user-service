package app.userservice.Exceptions;

import app.userservice.Payloads.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<APIResponse> handleResourceNotFoundExeption(ResourceNotFoundException ex) {
        String message = ex.getMessage();
        APIResponse response = APIResponse.builder()
                .message(message)
                .issuccess(false)
                .httpStatus(HttpStatus.NOT_FOUND)
                .build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errorsMap = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String message = error.getDefaultMessage();
            String fieldName = ((FieldError) error).getField();

            errorsMap.put(fieldName, message);
        });
        return new ResponseEntity<>(errorsMap, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<APIResponse> emailAlreadyExistsException(EmailAlreadyExistsException ex) {
        String message = ex.getMessage();
        APIResponse apiResponse = new APIResponse(message, false, HttpStatus.ALREADY_REPORTED);
        return new ResponseEntity<>(apiResponse, HttpStatus.ALREADY_REPORTED);
    }
}
