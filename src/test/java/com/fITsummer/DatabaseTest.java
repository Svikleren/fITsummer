package com.fITsummer;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class DatabaseTest {
    Database db;

    @Before
    public void setUp() throws Exception {
        db = new Database();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        Database db = new Database();
        String stString = "DELETE FROM fITsummer.user_info WHERE username='Aaa'";
        PreparedStatement statement = null;
        statement = db.conn.prepareStatement(stString);
        statement.executeUpdate();
        stString = "DELETE FROM fITsummer.user_info WHERE username='Aaa1'";
        statement = db.conn.prepareStatement(stString);
        statement.executeUpdate();
        db.conn.commit();
    }


    @Test
    public void userExists() throws SQLException {
        Boolean result = db.userExists("Renia");
        assertEquals("check userExists", true, result);
        Boolean resultWrong = db.userExists("Aaaa");
        assertEquals("check userExists with wrong parameter", false, resultWrong);
    }

    @Test
    public void registerNewUser() throws SQLException {
        String name = "Aaa";
        String pwd = "Bbb";
        db.registerNewUser(name, pwd);
        Boolean result = db.userExists("Aaa");
        assertEquals("check new user register", true, result);


    }

    @Test
    public void userPwdCorrect() throws SQLException {
        String name = "Aaa1";
        String pwd = "Bbb1";
        db.registerNewUser(name, pwd);
        Boolean result = db.userPwdCorrect("Aaa1", "Bbb1");
        assertEquals("check username password combination", true, result);
    }


}