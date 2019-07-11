package com.fITsummer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.*;

@Component
public class Database {
    public static Logger logger = LoggerFactory.getLogger(Database.class);

    Statement statement;
    String username;

    protected Connection conn = null;
    private final String DB_URL = "jdbc:mysql://127.0.0.1:3306/?autoReconnect=true&useSSL=false&characterEncoding=utf8&serverTimezone=GMT ";
    private final String USER = "root";
    private final String PASS = "admin";
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

    @PostConstruct
    public void executeThisFunctionAfterStart() {
        try {
            userExists("renata");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean userExists(String username) throws SQLException {
        Statement statement;
        statement = conn.createStatement();
        String q = "SELECT * FROM fITsummer.user_info WHERE username like '" + username+"'";
        ResultSet rs = statement.executeQuery(q);
        if (rs.next()) {
            return true;
        } else
            return false;
    }



    //return true if combination username+password is correct
    public boolean userPwdCorrect(String username, String password) {

        return false;
    }

    //register new user with passed username and password.
    public void registerNewUser(String username, String password) {

    }

    //set passed token in db for user with passed username
    public void setRefreshToken(String username, String refreshToken) {

    }

    //set passed token in db for user with passed username
    public void setAccessToken(String username, String accessToken) {

    }

    //get token for user with passed username
    public String getRefreshToken(String username) {

        return null;
    }

    //get token for user with passed username
    public String getAccessToken(String username) {

        return null;

    }

}
