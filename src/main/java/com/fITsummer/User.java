package com.fITsummer;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.services.oauth2.Oauth2;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;

public class User {
    private String username;
    private String password;
    private String accessToken;
    ArrayList<Day> results = new ArrayList<>();
    //Day[] results = new Day[7];

    public User(String login, String password) {
        this.username = login; //store login
        this.password = password; //store password
    }

    public ArrayList<Day> login() throws IOException {
        Requests google = new Requests(this);
        results = google.requestData();
        return results;
    }

    public ArrayList<Day> login(Long startTimeMillis, Long endTimeMillis) throws IOException {
        Requests google = new Requests(this, startTimeMillis, endTimeMillis);
        results = google.requestData();
        return results;
    }



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public ArrayList<Day> getResults() {
        return results;
    }

    public void setResults(ArrayList<Day> results) {
        this.results = results;
    }
}
