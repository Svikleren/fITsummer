package com.fITsummer;

public class Controller {

    public static User user;


    public static boolean onLoginButtonClick(String username, String password) {
        if (Database.userExists(username) & Database.userPwdCorrect(username, password)) {
            user = new User(username, password);
            user.getTokensFromDb();
            user.setEpoch();
            return true;
        } else return false;
    }

    public static boolean onRegisterButtonClick(String username, String password) {
        if (Database.userExists(username) == false) {
            Database.registerNewUser(username, password);
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
