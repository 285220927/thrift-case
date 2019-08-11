/**
 * @Project: micro-service
 * @Package com.user.response
 * @author : zzc
 * @date Date : 2019年08月04日 下午1:15
 * @version V1.0
 */


package com.user.response;

public class LoginResponse extends Response {
    private String token;

    public LoginResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
