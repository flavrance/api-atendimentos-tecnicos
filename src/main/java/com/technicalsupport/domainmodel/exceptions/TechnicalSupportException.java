package com.technicalsupport.domainmodel.exceptions;

public class TechnicalSupportException extends RuntimeException {
    
    public TechnicalSupportException(String message) {
        super(message);
    }
    
    public TechnicalSupportException(String message, Throwable cause) {
        super(message, cause);
    }
} 