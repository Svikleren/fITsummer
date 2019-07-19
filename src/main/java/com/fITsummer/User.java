package com.fITsummer;

import java.io.IOException;
import java.util.ArrayList;

public class User {
    private String username;
    private String password;
    private String accessToken;
    ArrayList<Day> results = new ArrayList<>();
    int[] stats = new int[4];

    public void setStats(int[] stats) {
        this.stats = stats;
    }

    public User(String login, String password) {
        this.username = login; //store login
        this.password = password; //store password
    }

    public ArrayList<Day> login() throws IOException {
        Requests google = new Requests(this);
        results = google.requestData();
        stats = google.getStatistics(results);
        return results;
    }

    public ArrayList<Day> login(Long startTimeMillis, Long endTimeMillis) throws IOException {
        Requests google = new Requests(this, startTimeMillis, endTimeMillis);
        results = google.requestData();
        stats = google.getStatistics(results);
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

    public int[] getStats() {
        return stats;
    }
}
