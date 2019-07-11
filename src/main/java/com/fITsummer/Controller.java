package com.fITsummer;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import java.io.*;
import java.sql.SQLException;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@org.springframework.stereotype.Controller
public class Controller {

    User user;
    @Autowired
    Database db;

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/logbox")
    public String logbox( @ModelAttribute("message") String message,   @RequestParam(value = "attr", required = false) String attr, Model model) {
        model.addAttribute("message","990");
        System.out.println("message " + message);
        System.out.println("attr " + attr);
        model.addAttribute("message", message+"");
        return "logbox";
    }

    @GetMapping("/login")
    public String login() {
        return "register";
    }

    @GetMapping("/statics")
    public String statics() {
        return "statics";
    }


    @PostMapping(value = "/login")
   // @ResponseBody
    public String onLoginButtonClick(@RequestBody LoginData loginData,RedirectAttributes ra) throws SQLException {

        if (loginData.getUser() == null && loginData.getPassword() == null) {
            return "logbox";
        } else {
            boolean checkUser = db.userExists(loginData.getUser());
            boolean checkUserPass = db.userPwdCorrect(loginData.getUser(), loginData.getPassword());
            if (checkUser && checkUserPass) {
                User user = new User(loginData.getUser(), loginData.getPassword());
                this.user = user;
                return "redirect:/getTokens";
            } else if (checkUser == true & checkUserPass == false) {
                ra.addAttribute("attr", "attrVal");
                ra.addFlashAttribute("message", "ufonogduafsnaosidlfs");
                return  "redirect:/logbox";
                //return "Incorrect password" + "<a href='/'>Back</a>\n";
            } else return "Incorrect username" + "<a href='/'>Back</a>\n";
        }
    }

    @PostMapping(value = "/register")
    @ResponseBody
    public String onRegisterButtonClick(@RequestParam(value = "username", required = false) String username,
                                        @RequestParam(value = "password", required = false) String password) throws SQLException {
        if (username == null && password == null) {
            return "<form action=''>\n" + "New username: <input type='text' name='username' value=''><br/>\n"
                    + "New password:<input type='text' name='password' value=''><br/>\n"
                    + "<input type='submit' value='Register'><br/>\n" + "<a href='/'>Back</a>\n";
        } else if (password == null) {
            return "Empty password!" + "<a href='/'>Back</a>\n";
        } else {
            boolean checkUser = db.userExists(username);
            if (checkUser == false) {
                User user = new User(username, password);
                this.user = user;
                db.registerNewUser(username, password);
                return "<a href='/getTokens'>Sign-in with Google<a><br/>\n";
            } else return "This username already exists" + "<a href='/'>Back</a>\n";
        }
    }

    @PostMapping(value = "/graph")
    @ResponseBody
    public String graph(Day[] results) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < results.length; i++) {
            sb.append(results[i].getDate());
            sb.append(": ");
            sb.append(results[i].getStepCount() + "<br/>");
        }
        sb.append("<a href='/'>Back</a>\n");
        return sb.toString();
    }

    @RequestMapping(value = "/getTokens", method = GET)
    public String redirect() {
        String redirectUrl = "https://accounts.google.com/o/oauth2/v2/auth?client_id=123456648359-h291vabrnarv7ftjf2ff0p8vb740vm7l.apps.googleusercontent.com&response_type=code&scope=https://www.googleapis.com/auth/fitness.activity.read&redirect_uri=http://localhost:8080/code&access_type=offline&prompt=select_account";
        return "redirect:" + redirectUrl;
    }

    @RequestMapping(value = "/code")
    @ResponseBody
    public String code(@RequestParam String code) throws IOException {
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
                JacksonFactory.getDefaultInstance(), new FileReader("client_secrets.json"));

        GoogleTokenResponse tokenResponse =
                new GoogleAuthorizationCodeTokenRequest(
                        new NetHttpTransport(),
                        JacksonFactory.getDefaultInstance(),
                        "https://www.googleapis.com/oauth2/v4/token",
                        clientSecrets.getDetails().getClientId(),
                        clientSecrets.getDetails().getClientSecret(),
                        code,
                        "http://localhost:8080/code")  // Specify the same redirect URI that you use with your web
                        .execute();
        user.setAccessToken(tokenResponse.getAccessToken());
        Day[] results = user.login();
        return graph(results);
    }
}
