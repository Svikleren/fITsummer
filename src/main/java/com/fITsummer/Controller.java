package com.fITsummer;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
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
    String clientID = "";
    String clientSecret = "";

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
    public String onLoginButtonClick(String username, String password) {
        //šeit pārbaudam vai ir datubāzē
        //un pārbaudam vai lietotājvārds+parole sakrīt
        User user = new User(username, password);
        this.user = user;
        return "<a href='/getTokens'>Sign-in with Google<a><br/>\n";
    }

    @RequestMapping(value = "/register")
    @ResponseBody
    public String onRegisterButtonClick(String username, String password, String email) {
        //šeit pārbaudam vai datubāzē nav tāds lietotājvārds un parole
        User user = new User(username, password);
        this.user = user;
        return "<a href='/getTokens'>Sign-in with Google<a><br/>\n";
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
        return sb.toString();
    }

    @RequestMapping(value = "/getTokens", method = RequestMethod.GET)
    public String redirect() {
        String redirectUrl = "";
        return "redirect:" + redirectUrl;
    }

    @RequestMapping(value = "/code")
    @ResponseBody
    public String code(@RequestParam String code) throws IOException {
        GoogleTokenResponse tokenResponse =
                new GoogleAuthorizationCodeTokenRequest(
                        new NetHttpTransport(),
                        JacksonFactory.getDefaultInstance(),
                        "https://www.googleapis.com/oauth2/v4/token",
                        clientID,
                        clientSecret,
                        code,
                        "http://localhost:8080/code")  // Specify the same redirect URI that you use with your web
                        .execute();
        user.setAccessToken(tokenResponse.getAccessToken());
        Day[] results = user.login();
        return graph(results);
    }
}
