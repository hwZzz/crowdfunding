package com.crowd.exception;

/**
 * 保存或跟新Admin时如果检测到登录账号重复抛出这个异常
 */
public class LoginAcctAreadyInUseForUpdateException extends RuntimeException {
    public LoginAcctAreadyInUseForUpdateException() {
    }

    public LoginAcctAreadyInUseForUpdateException(String message) {
        super(message);
    }

    public LoginAcctAreadyInUseForUpdateException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoginAcctAreadyInUseForUpdateException(Throwable cause) {
        super(cause);
    }

    public LoginAcctAreadyInUseForUpdateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
