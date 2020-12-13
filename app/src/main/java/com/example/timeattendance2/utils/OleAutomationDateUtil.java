package com.example.timeattendance2.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class OleAutomationDateUtil {

    private long ONE_DAY = 24L * 60 * 60 * 1000;

    public Date fromOADate(double d, TimeZone tz) {

        long wholeDays = (long) d;
        double fracDays = Math.abs(d - wholeDays);

        long offset = (ONE_DAY * wholeDays) + (long) (fracDays * ONE_DAY);

        Date base = baseFor(tz);
        return new Date(base.getTime() + offset);
    }

    private Date baseFor(TimeZone tz) {

        Calendar c = Calendar.getInstance(tz);
        c.clear();
        c.set(1899, 11, 30, 0, 0, 0);
        return c.getTime();
    }

    public String convertToOADate(Date date) throws ParseException {
        double oaDate;
        SimpleDateFormat myFormat = new SimpleDateFormat("dd MM yyyy", Locale.US);
        Date baseDate = myFormat.parse("30 12 1899");
        Long days = TimeUnit.DAYS.convert(date.getTime() - baseDate.getTime(), TimeUnit.MILLISECONDS);

        oaDate = (double) days + ((double) date.getHours() / 24) + ((double) date.getMinutes() / (60 * 24)) + ((double) date.getSeconds() / (60 * 24 * 60));
        return String.valueOf(oaDate);
    }
}