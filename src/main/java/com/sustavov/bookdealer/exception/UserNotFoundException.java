package com.sustavov.bookdealer.exception;

import static com.sustavov.bookdealer.exception.ErrorCode.USER_NOT_FOUND;

public class UserNotFoundException extends BookDealerException {
    public UserNotFoundException(String message) {
        super(USER_NOT_FOUND, message);
    }
}
