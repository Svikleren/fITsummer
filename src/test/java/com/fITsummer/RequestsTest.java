package com.fITsummer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class RequestsTest {
    User user;
    Requests req;

    @Before
    public void setUp() throws Exception {
        user = new User("renia", "renia");
        req = new Requests(user);
    }

    @Test
    public void getStatistics() {
        ArrayList<Day> list = new ArrayList<>();
        Day day1 = new Day();
        day1.setDate("09.07.2019");
        day1.setStepCount(20);
        Day day2 = new Day();
        day2.setDate("10.07.2019");
        day2.setStepCount(10);
        Day day3 = new Day();
        day3.setDate("11.07.2019");
        day3.setStepCount(30);
        Day day4 = new Day();
        day4.setDate("12.07.2019");
        day4.setStepCount(40);
        list.add(day1);
        list.add(day2);
        list.add(day3);
        list.add(day4);
        int expMax = 40;
        int expMin = 10;
        int expSum = 100;
        int expAv = 25;
        String expResult = "max:" + expMax + "min:" + expMin + "average:" + expAv + "size:" + 4 + "sum:" + expSum;
        assertEquals("check statistics string", expResult, req.getStatistics(list));
    }
}