package com.sustavov.bookdealer.exception;

import lombok.Getter;

@Getter
public class BookDealerException extends RuntimeException {
    private ErrorCode errorCode;

    public BookDealerException(String message) {
        super(message);
    }

    public BookDealerException(String message, Exception e) {
        super(message, e);
    }

    public BookDealerException(Throwable cause) {
        super(cause);
    }

    public BookDealerException(ErrorCode errorCode, String message) {
        super(errorCode.getMessage() + ". " + message);

        this.errorCode = errorCode;
    }

    public BookDealerException(ErrorCode errorCode, String message, Throwable e) {
        super(errorCode.getMessage() + ". " + message, e);

        this.errorCode = errorCode;
    }

}
