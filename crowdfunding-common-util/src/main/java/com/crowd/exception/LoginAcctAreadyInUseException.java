package com.crowd.exception;

/**
 * 保存或跟新Admin时如果检测到登录账号重复抛出这个异常
 */
public class LoginAcctAreadyInUseException extends RuntimeException {
    public LoginAcctAreadyInUseException() {
    }

    public LoginAcctAreadyInUseException(String message) {
        super(message);
    }

    public LoginAcctAreadyInUseException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoginAcctAreadyInUseException(Throwable cause) {
        super(cause);
    }

    public LoginAcctAreadyInUseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
