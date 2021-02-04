package com.roshka.javaldaptester.ldap;

public class LDAPTesterException extends Exception {

    private String errorCode;

    public LDAPTesterException(String code, String message) {
        this(code, message, null);
    }

    public LDAPTesterException(String code, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = code;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
