package com.bix.imageprocessor.utils;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public class DateUtils {

    public static long getSecondsUntilNextDay() {
        var utcNow = ZonedDateTime.now(ZoneId.of("UTC"));
        var utcTomorrow = utcNow.plusDays(1).truncatedTo(ChronoUnit.DAYS);

        return ChronoUnit.SECONDS.between(utcNow, utcTomorrow);
    }

}
