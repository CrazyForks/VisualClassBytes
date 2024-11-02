package com.liubs.vcb.entity;

/**
 * @author Liubsyy
 * @date 2024/10/26
 */
public class Result <T> {
    private boolean isSuccess;
    private String errorMessage;
    private T data;

    public Result() {
    }

    public Result(boolean isSuccess, String errorMessage, T data) {
        this.isSuccess = isSuccess;
        this.errorMessage = errorMessage;
        this.data = data;
    }

    public static Result success(){
        Result r = new Result();
        r.setSuccess(true);
        return r;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public T getData() {
        return data;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setData(T data) {
        this.data = data;
    }
}
