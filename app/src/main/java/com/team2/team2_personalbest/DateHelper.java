package com.team2.team2_personalbest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateHelper {

    public static Date previousDay(int daysAgo) {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -daysAgo);
        return cal.getTime();
    }

    public static String getPreviousDayDateString(int daysAgo) {
        DateFormat dateFormat = new SimpleDateFormat("MM.dd.yy", Locale.US);
        return dateFormat.format(previousDay(daysAgo));
    }
}
