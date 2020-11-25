package ru.javawebinar.topjava.util;

import org.springframework.format.Formatter;

import java.text.ParseException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public final class TimeFormatter implements Formatter<LocalTime> {
    private final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public LocalTime parse(String s, Locale locale) throws ParseException {
        return DateTimeUtil.parseLocalTime(s);
    }

    @Override
    public String print(LocalTime localTime, Locale locale) {
        if (localTime == null) {
            return "";
        }
        return localTime.format(TIME_FORMATTER);
    }
}
