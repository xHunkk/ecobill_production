package com.ecobill.ecobill.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

@Component
public class ConversionUtils {

    public Long integerToLongConversion(Integer intValue) {
        return Long.valueOf(intValue);
    }

    public Long doubleToLongConversion(Double doubleValue) {
        return doubleValue.longValue();
    }

    public Date StringToDateConversion(String stringValue) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.parse(stringValue);
    }

    public static Timestamp StringToTimestamp(String dateTimeString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
        LocalDateTime localDateTime;
        try {
            localDateTime = LocalDateTime.parse(dateTimeString, formatter);
        } catch (DateTimeParseException e) {
            // Try parsing without milliseconds
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            localDateTime = LocalDateTime.parse(dateTimeString, formatter);
        }
        return Timestamp.valueOf(localDateTime);
    }
}
