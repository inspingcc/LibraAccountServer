package com.insping.libra.account;

public class LibraToken {
    private long uid;
    private String token;
    private long expiresTime;

    LibraToken() {

    }

    LibraToken(User user) {
        this.uid = user.getUid();
        this.token = user.getToken();
        this.expiresTime = user.getExpiresTime();
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getExpiresTime() {
        return expiresTime;
    }

    public void setExpiresTime(long expiresTime) {
        this.expiresTime = expiresTime;
    }
}
