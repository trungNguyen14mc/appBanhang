package edu.fpt.appbanhang.model;

import java.util.List;

public class UserModel {
    boolean success;
    String message;
    List<user> result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<user> getResult() {
        return result;
    }

    public void setResult(List<user> result) {
        this.result = result;
    }
}
