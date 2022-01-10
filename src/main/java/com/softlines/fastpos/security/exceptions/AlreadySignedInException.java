package com.softlines.fastpos.security.exceptions;

import org.springframework.security.authentication.AccountStatusException;

public class AlreadySignedInException extends AccountStatusException {
    public AlreadySignedInException(String msg) {
        super(msg);
    }

    public AlreadySignedInException(String msg, Throwable t) {
        super(msg, t);
    }
}
