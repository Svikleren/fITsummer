package com.fITsummer;

import java.sql.Connection;

public class Database {
    Connection conn;
    String username;

    //return true if inputed username exists in database
    public static boolean userExists(String username) {

        return false;
    }

    //return true if combination username+password is correct
    public static boolean userPwdCorrect(String username, String password) {

        return false;
    }

    //register new user with passed username and password.
    public static void registerNewUser(String username, String password) {

    }

    //set passed token in db for user with passed username
    public static void setRefreshToken(String username, String refreshToken) {

    }

    //set passed token in db for user with passed username
    public static void setAccessToken(String username, String accessToken) {

    }

    //get token for user with passed username
    public static String getRefreshToken(String username) {

        return null;
    }

    //get token for user with passed username
    public static String getAccessToken(String username) {

        return null;

    }

}
