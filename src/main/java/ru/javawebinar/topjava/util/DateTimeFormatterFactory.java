package ru.javawebinar.topjava.util;

import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Formatter;
import org.springframework.format.Parser;
import org.springframework.format.Printer;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DateTimeFormatterFactory implements AnnotationFormatterFactory<DateTime> {
    @Override
    public Set<Class<?>> getFieldTypes() {
        return new HashSet<Class<?>>(List.of(LocalDate.class, LocalTime.class));
    }

    @Override
    public Printer<?> getPrinter(DateTime dateTime, Class<?> aClass) {
        return configureFormatterFrom(dateTime, aClass);
    }

    @Override
    public Parser<?> getParser(DateTime dateTime, Class<?> aClass) {
        return configureFormatterFrom(dateTime, aClass);
    }

    private Formatter<?> configureFormatterFrom(DateTime annotation, Class<?> fieldType) {
        if (annotation.info().equals("LocalDate")) {
            return new DateFormatter();
        } else {
            return new TimeFormatter();
        }
    }
}