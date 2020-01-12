package com.fITsummer;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.sql.SQLException;
import java.util.*;

//@org.springframework.stereotype.Controller
public class Controller2 {

    User user;
    ArrayList<Day> results;
    String host = "http://localhost:8080";
/*    @Autowired
    Database db;*/

    @Autowired
    DatabaseJpa db;

    @GetMapping("/")
    public String home() {
        return "index.html";
    }

    @Autowired
    UserRepository userRepo;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String onLoginButtonClick(@RequestParam(value = "username", required = false) String username,
                                     @RequestParam(value = "password", required = false) String password) throws SQLException {
        if (username == null && password == null) {
            return "login.html";
        } else {
            username = username.toLowerCase();

            //====================================== funkcija userExists, kurai vajadzetu but atseviska klase un dot boolean mainigajam checkUser
            boolean checkUser = db.userExists(username);
            boolean checkUserPass = db.userPwdCorrect(username, password);
            if (checkUser && checkUserPass) {
                User user = new User(username, password);
                this.user = user;
                return redirect();
            } else if (checkUser == true & checkUserPass == false) {
                return "invalidlogin.html";
            } else return "invalidlogin.html";
        }
    }

    @RequestMapping(value = "/register")
    public String onRegisterButtonClick(@RequestParam(value = "username", required = false) String username,
                                        @RequestParam(value = "password", required = false) String password) throws SQLException {
        if (username == null && password == null) {
            return "reg.html";
        } else if (password == null) {
            return "invalidlogin.html";
        } else {
            username = username.toLowerCase();
            boolean checkUser = db.userExists(username);
            if (checkUser == false) {
                User user = new User(username, password);
                this.user = user;
                //userRepo.save(user); //te ari vajadzetu but atseviskai metodei kas izsauco to repo.save
                return redirect();
            } else return "invalidlogin.html";
        }
    }

    @RequestMapping(value = "/graph")
    @ResponseBody
    public String graph(@RequestParam(value = "from", required = false) String from,
                        @RequestParam(value = "to", required = false) String to,
                        @RequestParam(value = "chart", required = false) String chart) throws IOException {

        StringBuilder sb = new StringBuilder();
        String date = getMaxDate();
        int[] stats = new int[4];

        if ((from != null & to != null) & (from != "" & to != "")) {
            Long startTime = 0L;
            Long endTime = 0L;
            startTime = convertStringToDate(from).getTimeInMillis();
            endTime = convertStringToDate(to).getTimeInMillis() + 86400000;
            results = user.login(startTime, endTime);
            stats = user.getStats();
        } else {
            results = user.login();
            stats = user.getStats();
        }
        if (chart == null || chart == "") chart = "Line";
        sb.append("<!DOCTYPE html>\n" +
                "<html>\n" +
                " <head>\n" +
                "  <meta charset=\"utf-8\">\n" +
                "<a href='/'>Logout</a>" +
                "  <title>Steps history</title>\n" +
                "  <script src=\"https://www.google.com/jsapi\"></script>\n" +
                "  <script>\n" +
                "   google.load(\"visualization\", \"1\", {packages:[\"corechart\"]});\n" +
                "   google.setOnLoadCallback(drawChart);\n" +
                "   function drawChart() {\n" +
                "    var data = google.visualization.arrayToDataTable([\n" +
                "     ['Day', 'Steps', { role: 'annotation' }],\n");
        for (int i = 0; i < results.size(); i++) {
            sb.append("     ['" + results.get(i).getDate() + "'," + results.get(i).getStepCount() + "," + results.get(i).getStepCount() + "],\n");
        }
        sb.append("    ]);\n" +
                "    var options = {\n" +
                "     title: 'Steps history: " + results.get(0).getDate() + " - " + results.get(results.size() - 1).getDate() + "',\n" +
                "     hAxis: {title: 'Day'},\n" +
                "     vAxis: {title: 'Steps'}\n" +
                "    };\n" +
                "    var chart = new google.visualization." + chart + "Chart(document.getElementById('steps'));\n" +
                "    chart.draw(data, options);\n" +
                "   }\n" +
                "  </script>\n" +
                "<style>\n" +
                "span.b {\n" +
                "  display: inline-block;\n" +
                "  width: 40%;\n" +
                "  height: 40%;\n" +
                "  padding: 5px;\n" +
                "  vertical-align:top;\n" +
                "}\n" +
                "</style>" +
                " </head>\n" +
                " <body>\n" +
                "  <div id=\"steps\" style=\"width: 100%; height: 400px;\"></div>\n" +
                "<span class=\"b\"> <form>" +
                "<p>Date from: <input type=\"date\" name=\"from\" max=\"" + date + "\">" +
                "Date to: <input type=\"date\" name=\"to\" max=\"" + date + "\">" +
                "   <p><b>Chart style</b></p>\n" +
                "    <p><input name=\"chart\" type=\"radio\" value=\"Line\" checked>Line chart</p>\n" +
                "    <p><input name=\"chart\" type=\"radio\" value=\"Bar\">Bar chart</p>\n" +
                "    <p><input name=\"chart\" type=\"radio\" value=\"Column\">Column chart</p>\n" +
                "    <p><input type=\"submit\" value=\"Request\"></p>\n" +
                "  </form> </span>" +
                "<span  class=\"b\"><form>\n" +
                "<table border=\"1\" cellpadding=\"5\">\n" +
                "  <caption><b>Statistics for selected period</b></caption>\n" +
                "      <tr><td>Max steps count</td><td>" + stats[0] + "</td></tr>\n" +
                "      <tr><td>Min steps count</td><td>" + stats[1] + "</td></tr>\n" +
                "      <tr><td>Average steps count</td><td>" + stats[2] + "</td></tr>\n" +
                "      <tr><td>All steps count</td><td>" + stats[3] + "</td></tr>\n" +
                "  </table>\n" +
                "</form>\n" +
                "</span>" +
                " </body>\n" +
                " </html>");
        return sb.toString();
    }

    @RequestMapping(value = "/getTokens", method = RequestMethod.GET)
    public String redirect() {
        String redirectUrl = "https://accounts.google.com/o/oauth2/v2/auth?client_id=123456648359-h291vabrnarv7ftjf2ff0p8vb740vm7l.apps.googleusercontent.com&response_type=code&scope=https://www.googleapis.com/auth/fitness.activity.read&redirect_uri=http://localhost:8080/code&access_type=offline&prompt=select_account";
        return "redirect:" + redirectUrl;
    }

    @RequestMapping(value = "/code")
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
                        host + "/code")
                        .execute();
        user.setAccessToken(tokenResponse.getAccessToken());
        return "redirect:" + host + "/graph";
    }

    public String getMaxDate() {
        Calendar endDate = Calendar.getInstance();
        int year;
        int month;
        int day;
        year = endDate.get(Calendar.YEAR);
        month = endDate.get(Calendar.MONTH) + 1;
        day = endDate.get(Calendar.DAY_OF_MONTH);
        String date;
        if (month < 10) date = year + "-0" + month + "-" + day;
        else date = year + "-" + month + "-" + day;
        return date;
    }

    public Calendar convertStringToDate(String date) {
        int year = Integer.parseInt(date.substring(0, 4));
        int month = Integer.parseInt(date.substring(5, 7)) - 1;
        if (month == 0) month = Character.getNumericValue(date.charAt(6)) - 1;
        int day = Integer.parseInt(date.substring(8, 10));
        if (day == 0) day = Character.getNumericValue(date.charAt(9));
        Calendar calendarDate = Calendar.getInstance();
        calendarDate.set(year, month, day, 3, 0);
        return calendarDate;
    }

/*    public boolean userExists(String username){
        List<User> rs=userRepo.findByUsername(username);
        if (!rs.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean userPwdCorrect(String username, String password){
        List<User> rs=userRepo.findByUsername(username);
        if (!rs.isEmpty()&&rs.get(rs.size()-1).getPassword().equals(password)) {
            return true;
        } else {
            return false;
        }
    }*/
}
