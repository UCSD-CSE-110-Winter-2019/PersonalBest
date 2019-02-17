package com.team2.team2_personalbest;

import org.junit.Test;
import static org.junit.Assert.*;
import com.team2.team2_personalbest.DateHelper;


public class TestDateHelper {

    @Test
    public void testGetLastSevenWeekDays() {
        String[] days = DateHelper.getLastSevenWeekDays(1);
        String[] daysCheck = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday",
            "Saturday", "Sunday"};
        assertEquals(days.length, daysCheck.length);
        for(int i = 0; i < 7; i++) {
            assertEquals(daysCheck[i], days[i]);
        }
    }

    @Test
    public void testGetDayOfWeek() {
        int i = DateHelper.getDayOfWeek();

        assertEquals(1, i);

    }
}
