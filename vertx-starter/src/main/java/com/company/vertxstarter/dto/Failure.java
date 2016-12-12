/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.company.vertxstarter.dto;

/**
 *
 * @author thomasperoutka
 */
public class Failure extends RuntimeException {
            
    public static final Failure NOT_FOUND	 = new Failure("Resource not found", 404);
    public static final Failure UNSUPPORTED_MEDIA_TYPE	 = new Failure("Media type not supported", 415);
    public static final Failure NO_MEDIA_TYPE	 = new Failure("No media type provided", 415);
    
    public static final Failure INTERNAL_ERROR	 = new Failure("Server internal error", 500);

    private int code;

    public Failure() {
        super();
        code = 500;
    }

    public Failure(String message) {
        super(message);
        code = 500;
    }

    public Failure(Throwable throwable) {
        super(throwable);
        code = 500;
    }

    public Failure(int failureCode) {
        code = failureCode;
        initCause(new RuntimeException());
    }

    public Failure(String message, Throwable throwable) {
        super(message, throwable);
        code = 500;
    }

    public Failure(String message, int failureCode) {
        super(message);
        code = failureCode;
    }

    public Failure(Throwable throwable, int failureCode) {
        super(throwable);
        code = failureCode;
    }

    public Failure(String message, Throwable throwable, int failureCode) {
        super(message, throwable);
        code = failureCode;
    }

    public int getCode() {
        return code;
    }

    public static Failure failure(Throwable throwable) {
        if (throwable instanceof Failure) {
            return (Failure) throwable;
        }
        if (throwable.getMessage() == null) {
            return new Failure("No message provided", throwable.getCause());
        }
        return new Failure(throwable.getMessage(), throwable.getCause());
    }
}
