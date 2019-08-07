/**
 * @Project: micro-service
 * @Package com.user.response
 * @author : zzc
 * @date Date : 2019年08月04日 上午11:26
 * @version V1.0
 */


package com.user.response;

import java.io.Serializable;

public class Response implements Serializable {
    private String code;
    private String message;
    public static final Response USERNAME_OR_PASSWORD_INVALID = new Response("1001", "username or password invalid");
    public static final Response SEND_MOBILE_VERIFY_CODE_FAILED = new Response("1002", "send mobile verify code failed");
    public static final Response SEND_EMAIL_VERIFY_CODE_FAILED = new Response("1003", "send email verify code failed");
    public static final Response MOBILE_OR_EMAIL_IS_REQUIRED = new Response("1004", "mobile or email is required");
    public static final Response MOBILE_VERIFY_CODE_INVALID = new Response("1005", "mobile verify code invalid");
    public static final Response EMAIL_VERIFY_CODE_INVALID = new Response("1006", "email verify code invalid");
    public static final Response USER_INFO_VALID = new Response("1007", "user info valid");

    public Response() {
        this.code = "0";
        this.message = "success";
    }

    public Response(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
