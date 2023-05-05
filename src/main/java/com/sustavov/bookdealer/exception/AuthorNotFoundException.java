package com.sustavov.bookdealer.exception;

import static com.sustavov.bookdealer.exception.ErrorCode.AUTHOR_NOT_FOUND;

public class AuthorNotFoundException extends BookDealerException {
    public AuthorNotFoundException(String message) {
        super(AUTHOR_NOT_FOUND, message);
    }
}
