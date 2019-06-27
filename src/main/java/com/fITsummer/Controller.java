package com.fITsummer;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@org.springframework.stereotype.Controller
public class Controller {

    public static User user;

    @PostMapping(value = "/login")
    public boolean onLoginButtonClick(@RequestBody UserLogin userdata) {
        if (Database.userExists(userdata.getUsername()) & Database.userPwdCorrect(userdata.getUsername(), userdata.getPassword())) {
            user = new User(userdata.getUsername(), userdata.getPassword());
            user.getTokensFromDb();
            user.setEpoch();
            return true;
        } else return false;
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }

    public static boolean onRegisterButtonClick(UserLogin userdata) {
        if (Database.userExists(userdata.getUsername()) == false) {
            Database.registerNewUser(userdata.getUsername(), userdata.getPassword());
            return true;
        } else return false;
    }

    public static void onSetTokensButtonClick(String refreshToken, String accessToken) {
        user.setRefreshToken(refreshToken);
        user.setAccessToken(accessToken);
        Database.setRefreshToken(user.getUsername(), refreshToken);
        Database.setAccessToken(user.getUsername(), accessToken);
    }


    //	reģistrēt lietotāju + reģistrēt datubāzē
    //	ierakstīt tokenus

    // saglabāt datus datubāzē

    // ielogoties
    //	pieprasīt datus
    //  attēlot datus

}
