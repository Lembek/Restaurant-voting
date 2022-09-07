package com.github.lembek.RestaurantVoting.error;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.http.HttpStatus;

public class VoteAlreadyExistException extends AppException {
    public VoteAlreadyExistException(String message) {
        super(HttpStatus.CONFLICT, message, ErrorAttributeOptions.of(ErrorAttributeOptions.Include.MESSAGE));
    }
}
