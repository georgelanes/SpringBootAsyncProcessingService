package br.com.globit.async_processing.exception;

public class AsyncProcessingException extends RuntimeException {
    public AsyncProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}