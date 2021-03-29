package dev.sayem.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

public class DateTimeUtil {
    public static final String DATE_FORMAT_BACKWARD = "yyyy-MM-dd";

    public static Date parse(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_BACKWARD);
        try {
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getReadableDuration(Duration duration) {
        long h = duration.toHours();
        if (h < 0) {
            h = 0;
        }
        long m = duration.toMinutes() % 24;
        if (m < 0) {
            m = 0;
        }
        return h + ":" + m;
    }

    public static String getReadableDurationHMS(Duration duration) {
        return duration.toString().substring(2).replaceAll("(\\d[HMS])(?!$)", "$1 ").toLowerCase();
    }

    public static Duration calculateDuration(Date fromDate, Date toDate) {
        Instant from = Instant.ofEpochMilli(fromDate.getTime());
        Instant to = Instant.ofEpochMilli(toDate.getTime());
        return Duration.between(from, to);
    }

}

