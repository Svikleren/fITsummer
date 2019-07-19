package com.fITsummer;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.*;

public class ControllerTest {
    Controller controller;

    @Before
    public void setUp() throws Exception {
        controller = new Controller();
    }

    @Test
    public void convertStringToDate() {
        String dateString = "2019-05-05";
        Calendar date = Calendar.getInstance();
        date.set(2019, 4, 5, 3, 0);
        Calendar result = controller.convertStringToDate(dateString);
        int year = result.get(Calendar.YEAR);
        int month = result.get(Calendar.MONTH) + 1;
        int day = result.get(Calendar.DAY_OF_MONTH);
        assertEquals("check year", 2019, year);
        assertEquals("check month", 5, month);
        assertEquals("check day", 5, day);
    }

    @Test
    public void getMaxDate1() {
        String expected = "2019-07-19"; //this string needs to be changed every day to todays date
        String result = controller.getMaxDate();
        assertEquals("check max date", expected, result);
    }
}