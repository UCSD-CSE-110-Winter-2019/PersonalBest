package com.team2.team2_personalbest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateHelper {
    private static String[] daysOfWeek = {"Sat", "Sun", "Mon", "Tues", "Wed",
            "Thurs", "Fri"};

    public static Date previousDay(int daysAgo) {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -daysAgo);
        return cal.getTime();
    }

    public static String getPreviousDayDateString(int daysAgo) {
        DateFormat dateFormat = new SimpleDateFormat("MM.dd.yy", Locale.US);
        return dateFormat.format(previousDay(daysAgo));
    }

    public static String[] getLastSevenWeekDays(int dayOfWeek) {
        String[] days = new String[7];
        dayOfWeek += 1;
        for(int i = 0; i < 7; i++) {
            days[i] = daysOfWeek[dayOfWeek];
            dayOfWeek += 1;
            dayOfWeek = dayOfWeek % 7;
        }
        return days;
    }

    public static int getDayOfWeek() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.DAY_OF_WEEK);
    }
}
