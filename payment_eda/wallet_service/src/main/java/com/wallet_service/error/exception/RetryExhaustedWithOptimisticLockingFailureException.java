package com.wallet_service.error.exception;

public class RetryExhaustedWithOptimisticLockingFailureException extends RuntimeException {

    public RetryExhaustedWithOptimisticLockingFailureException(String message) {
        super(message);
    }
}