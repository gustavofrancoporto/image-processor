package com.bix.imageprocessor.utils;

import org.junit.jupiter.api.Test;

import static java.time.ZoneId.of;
import static java.time.ZonedDateTime.now;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

public class DateUtilsTest {

    @Test
    void shouldGetSecondsUntilNextUtcDay() {

        var utcNow = now(of("UTC"));

        var secondsUntilNextUtcDay = DateUtils.getSecondsUntilNextUtcDay();

        assertThat(utcNow.plusSeconds(secondsUntilNextUtcDay))
                .isCloseTo(utcNow.plusDays(1).truncatedTo(DAYS), within(10, SECONDS));

    }
}
