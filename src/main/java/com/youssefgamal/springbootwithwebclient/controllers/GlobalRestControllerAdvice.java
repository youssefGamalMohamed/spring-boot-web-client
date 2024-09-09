package com.youssefgamal.springbootwithwebclient.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
@Slf4j
public class GlobalRestControllerAdvice {


    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException noSuchElementException) {
        log.error("handleNoSuchElementException(): {}", noSuchElementException.getMessage());
        return ResponseEntity.notFound()
                .build();
    }
}
