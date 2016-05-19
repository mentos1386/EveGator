package com.mentos1386.evegator.Models;

public class AuthObject {

    public String ACCESS_TOKEN;
    public String REFRESH_TOKEN;
    public String OWNER;
    public long EXPIRES_IN;

    public AuthObject(String ACCESS_TOKEN, String REFRESH_TOKEN, long EXPIRES_IN, String OWNER){
        this.ACCESS_TOKEN = ACCESS_TOKEN;
        this.REFRESH_TOKEN = REFRESH_TOKEN;
        this.OWNER = OWNER;
        this.EXPIRES_IN = EXPIRES_IN;
    }
}
