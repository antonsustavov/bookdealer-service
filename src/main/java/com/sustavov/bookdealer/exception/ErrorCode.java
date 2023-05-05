package com.sustavov.bookdealer.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    AUTHOR_NOT_FOUND(101, "Author not found"),
    BOOK_NOT_FOUND(102, "Book not found"),
    USER_NOT_FOUND(103, "User not found");

    private final int code;
    private final String message;
}
