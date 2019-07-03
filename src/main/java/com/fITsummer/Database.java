package com.fITsummer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.sql.*;


@Component
public class Database {
    public static Logger logger = LoggerFactory.getLogger(Database.class);

//    Statement statement;
//    String username;

    protected Connection conn = null;
    private final String DB_URL = "jdbc:mysql://127.0.0.1:3306/?autoReconnect=true&useSSL=false&characterEncoding=utf8&serverTimezone=GMT ";
    private final String USER = "root";
    private final String PASS = "admin";
    private PreparedStatement preparedStatement = null;

/*    //testa nolūkiem datubāzes izdrukāšana, kamēr taisam projektu
    public static void main(String[] args) throws SQLException {
        Database ourDB = new Database();
        System.out.println(ourDB);
        System.out.println(ourDB.userExists("renata"));
        System.out.println(ourDB.userExists("dsfgdsfgsdf"));
        System.out.println(ourDB.userPwdCorrect("renata", "parole1"));
        System.out.println(ourDB.userPwdCorrect("renata", "pasdfgdsfsrole1"));
        System.out.println(ourDB.userPwdCorrect("renasdfsdfta", "parole1"));
        ourDB.registerNewUser("randomuser", "parole");
        System.out.println(ourDB.userExists("randomuser"));
    }*/

    public Database() {
        logger.info("test printing");
        try
        {
            try
            {
                Class.forName("com.mysql.cj.jdbc.Driver"); //loading mysql driver
            }
            catch (ClassNotFoundException e)
            {
                e.printStackTrace();
            }
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
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
            System.out.println("Username " + username + " already exists");
            return true;
        }
        return false;
    }


    //return true if combination username+password is correct
    public boolean userPwdCorrect(String username, String password) throws SQLException{
        preparedStatement = conn.prepareStatement("SELECT username, password FROM fITsummer.user_info WHERE username like ? and password like ?");
        preparedStatement.setString(1,username);
        preparedStatement.setString(2, password);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            System.out.println("Username " + username + " and password correct");
            return true;
        } return false;
    }

    //register new user with passed username and password.
    public void registerNewUser(String username, String password) throws SQLException{
        preparedStatement = conn.prepareStatement("INSERT INTO fITsummer.user_info(username, password) VALUES (?, ?)");
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, password);
        int i = preparedStatement.executeUpdate();
        conn.commit();
        System.out.println(i + "users inserted");
    }
}//end main
