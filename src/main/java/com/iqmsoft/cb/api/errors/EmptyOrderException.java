package com.iqmsoft.cb.api.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "invalid operation for an empty order")
public class EmptyOrderException extends RuntimeException {
    public EmptyOrderException(String s) {
        super(s);
    }
}
