package org.example.until;

import java.io.Serializable;

/**
 * Create by Administrator
 * Data 23:52 2021/12/5 星期日
 */
public class Result<R> implements Serializable {
    private static final long SerialVersionUID = 1L;
    private boolean success;
    private int code;
    private String msg;
    private R data;

    //成功
    public static <R> Result<R> ofSuccess(String msg, R data) {
        return new Result<R>()
                .setSuccess(true)
                .setCode(Result.Code.SUCCESS)
                .setMsg(msg)
                .setData(data);
    }

    public static <R> Result<R> ofSuccess(R data) {
        return ofSuccess("SUCCESS", data);
    }

    public static <R> Result<R> ofSuccess(String msg) {
        return ofSuccess(msg, null);
    }

    public static <R> Result<R> ofSuccess() {
        return ofSuccess("SUCCESS");
    }

    //失败
    public static <R> Result<R> ofFail(int code, String msg, R data) {
        return new Result<R>()
                .setSuccess(false)
                .setCode(Result.Code.FAILED)
                .setMsg(msg)
                .setData(data);
    }

    public static <R> Result<R> ofFail(int code, String msg) {
        return ofFail(code, msg, null);
    }

    public static <R> Result<R> ofFailByCode(int code, String msg) {
        return new Result<R>()
                .setSuccess(false)
                .setCode(code)
                .setMsg(msg)
                .setData(null);
    }

    public static <R> Result<R> ofFail(int code, R data) {
        return ofFail(code, "FAILED", data);
    }


    public static <R> Result<R> ofFail(String msg, R data) {
        return ofFail(Result.Code.FAILED, msg, data);
    }

    public static <R> Result<R> ofFail(String msg) {
        return ofFail(Result.Code.FAILED, msg, null);
    }

    public static <R> Result<R> ofFail(R data) {
        return ofFail("FAILED", data);
    }


    public boolean isSuccess() {
        return success;
    }

    public Result<R> setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public int getCode() {
        return code;
    }

    public Result<R> setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public Result<R> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public R getData() {
        return data;
    }

    public Result<R> setData(R data) {
        this.data = data;
        return this;
    }


    @Override
    public String toString() {
        return "Result{" +
                "success=" + success +
                ", code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    public class Code {
        public final static int SUCCESS = 200;
        public final static int FAILED = 400;
        public final static int UNAUTHORIZED = 401;
        public final static int FORBIDDEN = 402;
        public final static int NOT_FOUND = 403;

    }

}
