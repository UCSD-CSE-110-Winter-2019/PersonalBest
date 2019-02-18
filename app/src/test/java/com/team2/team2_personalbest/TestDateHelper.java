package com.team2.team2_personalbest;

import org.junit.Test;
import static org.junit.Assert.*;
import com.team2.team2_personalbest.DateHelper;

import java.util.Calendar;
import java.util.Date;


public class TestDateHelper {

    @Test
    public void testGetLastSevenWeekDays() {
        String[] days = DateHelper.getLastSevenWeekDays(1);
        String[] daysCheck = {"Mon", "Tues", "Wed", "Thurs", "Fri",
            "Sat", "Sun"};
        assertEquals(days.length, daysCheck.length);
        for(int i = 0; i < 7; i++) {
            assertEquals(daysCheck[i], days[i]);
        }
    }

    @Test
    public void testDayToString() {
        long feb17 = 1550445565750L;
        Date day = new Date();
        day.setTime(feb17);
        assertEquals("02.17.19", DateHelper.dayDateToString(day));
    }

}
