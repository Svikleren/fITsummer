package com.fITsummer;

public class User {
    private String username;
    private String password;
    private String refreshToken;
    private String accessToken;
    private long epoch;

    public User(String login, String password) {
        this.username = login; //store login
        this.password = password; //store password
    }

    public String getUsername() {
        return username;
    }

    //get all tokens for current instance of class User from database
    public void getTokensFromDb() {
        setRefreshToken(Database.getRefreshToken(this.username));
        setAccessToken(Database.getAccessToken(this.username));
    }


    //static void to get current epoch
    public static long getCurrentEpoch() {
        return System.currentTimeMillis();
    }


    //set current time (epoch) for current instance of class User
    public void setEpoch() {
        this.epoch = getCurrentEpoch();
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
