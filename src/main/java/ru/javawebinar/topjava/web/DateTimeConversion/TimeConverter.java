package ru.javawebinar.topjava.web.DateTimeConversion;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_TIME;

@Component("timeConverter")
public class TimeConverter implements Converter<String, LocalTime> {
    @Override
    public LocalTime convert(String str) {
        if(str == null || str.isEmpty()){
            return null;
        } else {
            return LocalTime.parse(str, ISO_LOCAL_TIME);
        }
    }
}