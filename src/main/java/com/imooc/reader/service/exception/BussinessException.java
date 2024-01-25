package com.imooc.reader.service.exception;

/**
 * 68.业务逻辑异常 回到MemberServiceImpl编写用户名异常 69返回MemberServiceImpl
 */
public class BussinessException extends RuntimeException{
    private String code;
    private String msg;

    //构造方法
    public BussinessException(String code, String msg) {
        super(msg); //继承自运行时异常
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
