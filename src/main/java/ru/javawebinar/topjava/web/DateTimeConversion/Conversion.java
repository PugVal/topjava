package ru.javawebinar.topjava.web.DateTimeConversion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("conversion")
public class Conversion extends DefaultFormattingConversionService {

    @Autowired
    public Conversion(List<Converter> converters){
        super();
        converters.forEach(this::addConverter);
    }
}