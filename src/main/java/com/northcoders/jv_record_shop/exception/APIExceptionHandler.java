package com.northcoders.jv_record_shop.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class APIExceptionHandler {
   @ExceptionHandler(ItemNotFoundException.class)
   public ResponseEntity<Object> handleItemNotFoundException(ItemNotFoundException e){
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
   }
   @ExceptionHandler(InvalidInputException.class)
   public ResponseEntity<Object> handleInvalidInputException(InvalidInputException e){
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
   }

   /**
    * Exception handler for the Jakarta validation if it's unable to create an object from the JSON input
    * with the given conditions.
    * The method will create a response body with a map of the attributes with errors and the error messages attached
    * @param ex - The exception that is thrown
    * @return - The response body with a Map of error messages for the attribute with an error
    */
   @ExceptionHandler(MethodArgumentNotValidException.class)
   public ResponseEntity<Map<String, String>> handleValidationExceptions(final MethodArgumentNotValidException ex) {

      final BindingResult bindingResult = ex.getBindingResult();
      final List<FieldError> fieldErrors = bindingResult.getFieldErrors();
      final Map<String, String> errors = new HashMap<>();
      fieldErrors.forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

      return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
   }
}
