package ru.javawebinar.topjava.web.DateTimeConversion;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;

@Component("dateConverter")
public class DateConverter implements Converter<String, LocalDate> {
    @Override
    public LocalDate convert(String str) {
        if(str == null || str.isEmpty()){
            return null;
        } else {
            return LocalDate.parse(str, ISO_LOCAL_DATE);
        }
    }
}