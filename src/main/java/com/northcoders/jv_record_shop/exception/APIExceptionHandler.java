package com.northcoders.jv_record_shop.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class APIExceptionHandler {
   @ExceptionHandler
   public ResponseEntity<Object> handleItemNotFoundException(ItemNotFoundException e){
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
   }
}
