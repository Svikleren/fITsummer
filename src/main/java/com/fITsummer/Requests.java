package com.fITsummer;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.util.Utils;
import com.google.api.services.fitness.Fitness;
import com.google.api.services.fitness.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class Requests {

    User user;
    String appName = "fITsummer";
    String dataSourceId = "derived:com.google.step_count.delta:com.google.android.gms:estimated_steps";
    String accessToken;
    Long durationMillis = 86400000L;
    Long startTimeMillis = 0L;
    Long endTimeMillis = 0L;
    Calendar endDate;
    Calendar startDate;
    GoogleCredential credential;
    Fitness fitness;
    AggregateResponse response;

    public Requests(User user) {
        this.user = user;
        this.accessToken = user.getAccessToken();
        setDates();
        authorizeUser();
    }


    public Requests(User user, Long startTime, Long endTime) {
        this.user = user;
        this.accessToken = user.getAccessToken();
        this.startTimeMillis = startTime;
        this.endTimeMillis = endTime;
        authorizeUser();
    }


    public void authorizeUser() {
        credential = new GoogleCredential().setAccessToken(accessToken);
        fitness = new Fitness.Builder(Utils.getDefaultTransport(), Utils.getDefaultJsonFactory(), null)
                .setHttpRequestInitializer(credential)
                .setApplicationName(appName)
                .build();
    }

    public ArrayList<Day> requestData() throws IOException {
        BucketByTime bucketByTime = new BucketByTime();
        bucketByTime.setDurationMillis(durationMillis);

        AggregateRequest request = new AggregateRequest();
        request.setAggregateBy(Collections.singletonList(new AggregateBy().setDataSourceId(dataSourceId)));
        request.setBucketByTime(bucketByTime);
        request.setStartTimeMillis(startTimeMillis);
        request.setEndTimeMillis(endTimeMillis);
        Fitness.Users.Dataset.Aggregate requestAgg = fitness.users().dataset().aggregate("me", request);
        response = requestAgg.execute();
        return parseReceivedData(response);
    }

    public ArrayList<Day> parseReceivedData(AggregateResponse response) {
        ArrayList<Day> results = new ArrayList<>();
        int sum = 0;
        int i = 0;
        for (AggregateBucket aggregateBucket : response.getBucket()) {
            sum = 0;
            for (Dataset dataset : aggregateBucket.getDataset()) {
                for (DataPoint dataPoint : dataset.getPoint()) {
                    for (Value value : dataPoint.getValue()) {
                        if (value.getIntVal() != null) {
                            sum += value.getIntVal(); //for steps you only receive int values
                        }
                    }
                }
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(aggregateBucket.getStartTimeMillis());
            int mMonth = calendar.get(Calendar.MONTH) + 1;
            int mDay = calendar.get(Calendar.DAY_OF_MONTH);
            String year = Integer.toString(calendar.get(Calendar.YEAR));
            String month = Integer.toString(calendar.get(Calendar.MONTH) + 1);
            String day = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
            if (mDay < 10) day = "0" + day;
            if (mMonth < 10) month = "0" + month;
            String date = day + "." + month + "." + year;
            Day daySteps = new Day();
            daySteps.setDate(date);
            daySteps.setStepCount(sum);
            results.add(daySteps);
            i++;
        }
        return results;
    }

    public void setDates() {
        this.endDate = Calendar.getInstance();
        this.startDate = Calendar.getInstance();
        this.endDate.set(endDate.get(Calendar.YEAR), endDate.get(Calendar.MONTH), endDate.get(Calendar.DAY_OF_MONTH), 3, 0);
        this.startDate.set(endDate.get(Calendar.YEAR), endDate.get(Calendar.MONTH), endDate.get(Calendar.DAY_OF_MONTH) - 7, 3, 0);
        this.startTimeMillis = startDate.getTimeInMillis();
        this.endTimeMillis = endDate.getTimeInMillis();
    }

    public String getStatistics(ArrayList<Day> results) {
        int max = 0;
        int min = results.get(0).getStepCount();
        int sum = 0;
        int average = 0;
        for (int i = 0; i < results.size(); i++) {
            max = Math.max(results.get(i).getStepCount(), max);
            min = Math.min(results.get(i).getStepCount(), min);
            sum = sum + results.get(i).getStepCount();
        }
        average = sum / results.size();
        return "max:" + max + "min:" + min + "average:" + average + "size:" + results.size() + "sum:" + sum;
    }

}
