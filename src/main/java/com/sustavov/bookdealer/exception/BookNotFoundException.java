package com.sustavov.bookdealer.exception;

import static com.sustavov.bookdealer.exception.ErrorCode.BOOK_NOT_FOUND;

public class BookNotFoundException extends BookDealerException {
    public BookNotFoundException(String message) {
        super(BOOK_NOT_FOUND, message);
    }
}
