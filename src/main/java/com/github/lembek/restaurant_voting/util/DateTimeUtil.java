package com.github.lembek.restaurant_voting.util;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneOffset;

public class DateTimeUtil {

    private DateTimeUtil() {
    }

    private static final Clock defaultClock = Clock.systemDefaultZone();

    private static Clock clock = defaultClock;

    public static LocalTime timeNow() {
        return LocalTime.now(clock);
    }

    public static void setTime(String time) {
        clock = Clock.fixed(Instant.parse(time), ZoneOffset.UTC);
    }

    public static void resetTime() {
        clock = defaultClock;
    }
}
