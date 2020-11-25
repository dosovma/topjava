package ru.javawebinar.topjava.util;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;

public class DateConverter implements Converter<String, LocalDate> {

    @Override
    public LocalDate convert(String s) {
        return DateTimeUtil.parseLocalDate(s);
    }
}