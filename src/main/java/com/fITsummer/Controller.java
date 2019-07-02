package com.fITsummer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.xml.crypto.Data;
import java.sql.SQLException;

@org.springframework.stereotype.Controller
public class Controller {

    public static User user;
    @Autowired
    public Database database;

    @PostMapping(value = "/login")
    public boolean onLoginButtonClick(@RequestBody UserLogin userdata) throws SQLException {
        if (database.userExists(userdata.getUsername()) & database.userPwdCorrect(userdata.getUsername(), userdata.getPassword())) {
            user = new User(userdata.getUsername(), userdata.getPassword());
//
//      TODO salabot, lai padod tokenus
//            user.getTokensFromDb();

            user.setEpoch();
            return true;
        } else return false;
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }

    public boolean onRegisterButtonClick(UserLogin userdata) throws SQLException {
        if (database.userExists(userdata.getUsername()) == false) {
            database.registerNewUser(userdata.getUsername(), userdata.getPassword());
            return true;
        } else return false;
    }

    public  void onSetTokensButtonClick(String refreshToken, String accessToken) {
        user.setRefreshToken(refreshToken);
        user.setAccessToken(accessToken);
//        database.setRefreshToken(user.getUsername(), refreshToken);
//        database.setAccessToken(user.getUsername(), accessToken);
    }


    //	reģistrēt lietotāju + reģistrēt datubāzē
    //	ierakstīt tokenus

    // saglabāt datus datubāzē

    // ielogoties
    //	pieprasīt datus
    //  attēlot datus

}
