package com.bix.imageprocessor.utils;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.SECONDS;

public class DateUtils {

    public static long getSecondsUntilNextUtcDay() {
        var utcNow = ZonedDateTime.now(ZoneId.of("UTC"));
        var utcTomorrow = utcNow.plusDays(1).truncatedTo(DAYS);

        return SECONDS.between(utcNow, utcTomorrow);
    }
}
