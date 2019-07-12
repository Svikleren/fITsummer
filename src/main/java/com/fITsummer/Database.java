package com.fITsummer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.*;


@Component
public class Database {
    public static Logger logger = LoggerFactory.getLogger(Database.class);


    protected Connection conn = null;
    private final String DB_URL = "jdbc:mysql://127.0.0.1:3306/?autoReconnect=true&useSSL=false&characterEncoding=utf8&serverTimezone=GMT ";
    private final String USER = "root";
    private final String PASS = "admin";
    private PreparedStatement preparedStatement = null;


    public Database() throws SQLException {
        logger.info("test printing");
        Statement statement = null;
        try {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver"); //loading mysql driver
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            preparedStatement = conn.prepareStatement("CREATE TABLE IF NOT EXISTS fITsummer.user_info (\n" +
                    "`user_id` int(11) NOT NULL AUTO_INCREMENT,\n" +
                    "`username` varchar(45) DEFAULT NULL,\n" +
                    "`password` varchar(60) DEFAULT NULL,\n" +
                    "\n" +
                    "PRIMARY KEY (`user_id`),\n" +
                    "UNIQUE KEY `user_id_UNIQUE` (`user_id`),\n" +
                    "UNIQUE KEY `username_UNIQUE` (`username`)\n" +
                    ") ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci");
            preparedStatement.executeUpdate();
            conn.setAutoCommit(false);
        } catch (
                SQLException e) {
            e.printStackTrace();
        }
    }


    public boolean userExists(String username) throws SQLException {
        preparedStatement = conn.prepareStatement("SELECT username FROM fITsummer.user_info WHERE username like ?");
        preparedStatement.setString(1, username);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            logger.info("Username " + username + " already exists");
            return true;
        }
        return false;
    }


    public boolean userPwdCorrect(String username, String password) throws SQLException {

        preparedStatement = conn.prepareStatement("SELECT username, password FROM fITsummer.user_info WHERE username like ? and password like SHA1(?)");
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, password);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            logger.info("Username " + username + " and password " + password + "correct");
            return true;
        }
        return false;
    }


    public void registerNewUser(String username, String password) throws SQLException {

        preparedStatement = conn.prepareStatement("INSERT INTO fITsummer.user_info(username, password) VALUES (?, SHA1(?))");
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, password);
        int i = preparedStatement.executeUpdate();
        conn.commit();
        logger.info(i + "users inserted" + password);
    }

}//end main