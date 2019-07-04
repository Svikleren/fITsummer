package com.fITsummer;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.ui.ModelMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

import org.apache.commons.collections.MultiMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@org.springframework.stereotype.Controller
public class Controller {

    User user;
    @Autowired
    Database db;

    @GetMapping("/")
    @ResponseBody
    public String home() {
        StringBuilder sb = new StringBuilder();
        sb.append("<a href='/login'>Login<a><br/>\n");
        sb.append("<a href='/register'>Register<a><br/>\n");
        return sb.toString();
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    @ResponseBody
    public String onLoginButtonClick(@RequestParam(value = "username", required = false) String username,
                                     @RequestParam(value = "password", required = false) String password) throws SQLException {
        if (username == null && password == null) {
            return "<form action=''>\n" + "Username: <input type='text' name='username' value=''><br/>\n"
                    + "Password:<input type='text' name='password' value=''><br/>\n"
                    + "<input type='submit' value='Login'><br/>\n" + "<a href='/'>Back</a>\n";
        } else {
            boolean checkUser = db.userExists(username);
            boolean checkUserPass = db.userPwdCorrect(username, password);
            if (checkUser && checkUserPass) {
                User user = new User(username, password);
                this.user = user;
                return "<a href='/getTokens'>Sign-in with Google<a><br/>\n";
            } else if (checkUser == true & checkUserPass == false) {
                return "Incorrect password" + "<a href='/'>Back</a>\n";
            } else return "Incorrect username" + "<a href='/'>Back</a>\n";
        }
    }

    @RequestMapping(value = "/register")
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

    @RequestMapping(value = "/graph")
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

    @RequestMapping(value = "/getTokens", method = RequestMethod.GET)
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
