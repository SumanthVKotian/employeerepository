package com.instagramclone.exception;

import com.instagramclone.payload.ErrorDetails;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.HashMap;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {

        HashMap<String,String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error)->{
            String fieldName = ((FieldError)error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName,errorMessage);
        });

        return new ResponseEntity<Object>(errors,HttpStatus.UNPROCESSABLE_ENTITY);

    }

    /**** handling specific exceptions ****/

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleResourceNotFoundException (ResourceNotFoundException resourceNotFoundException,
                                                                         WebRequest webRequest) {
     ErrorDetails errorDetails = new ErrorDetails(new Date(),resourceNotFoundException.getMessage(),
            webRequest.getDescription(false));

      return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(APIException.class)
    public ResponseEntity<ErrorDetails> handleAPIException (APIException apiException,
                                                                         WebRequest webRequest) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(),apiException.getMessage(),
                webRequest.getDescription(false));

        return new ResponseEntity<ErrorDetails>(errorDetails, HttpStatus.NOT_FOUND);
    }


    /**** handling global exception ****/

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGlobalException (Exception exception, WebRequest webRequest) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(),
                exception.getMessage(),webRequest.getDescription(false));

        return new ResponseEntity<ErrorDetails>(errorDetails,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
