package com.fITsummer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;


public class Database {
    public static Logger logger = LoggerFactory.getLogger(Database.class);

    Statement statement;
    String username;

    protected static Connection conn = null;
    private final static String DB_URL = "jdbc:mysql://127.0.0.1:3306/?autoReconnect=true&useSSL=false&characterEncoding=utf8&serverTimezone=GMT ";
    private final static String USER = "root";
    private final static String PASS = "Student007";
    private PreparedStatement preparedStatement = null;

    public Database() {
//        logger.info("test printing");

        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            conn.setAutoCommit(false);
        } catch (
                SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean userExists(String username) throws SQLException {
        Statement statement;
        statement = conn.createStatement();
        String q = "SELECT * FROM fITsummer.user_info WHERE username like " + username;
        ResultSet rs = statement.executeQuery(q);
        if (rs.next()) {
            return true;
        } else
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
