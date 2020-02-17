package com.example.demo.pojo;

public class Result<T> {
    private boolean success = true;
    private String msg = "";
    private T data = null;

    public Result() {
        super();
    }

    public Result(boolean success) {
        super();
        this.success = success;
    }

    public Result(boolean success, T data) {
        super();
        this.success = success;
        this.data = data;
    }

    public Result(boolean success, String msg, T data) {
        super();
        this.success = success;
        this.msg = msg;
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}