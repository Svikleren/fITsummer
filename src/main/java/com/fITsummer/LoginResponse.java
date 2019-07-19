package com.fITsummer;

public class LoginResponse {
    private Boolean success;
    private String message;
    private String forwardTo;

    public LoginResponse(Boolean success, String message, String forwardTo) {
        this.success = success;
        this.message = message;
        this.forwardTo = forwardTo;
    }

    public LoginResponse() {
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getForwardTo() {
        return forwardTo;
    }

    public void setForwardTo(String forwardTo) {
        this.forwardTo = forwardTo;
    }
}
