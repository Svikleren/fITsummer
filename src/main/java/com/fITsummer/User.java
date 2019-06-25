package com.fITsummer;

public class User {
    private String login;
    private String password;
    private String refreshToken;
    private String accessToken;
    private long epoch;

    public User(String login, String password) {
        this.login = login; //store login
        this.password = password; //store password
    }
}
