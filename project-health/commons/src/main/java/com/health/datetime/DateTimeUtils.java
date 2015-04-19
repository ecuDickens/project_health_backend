package com.health.datetime;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.joda.time.DateTime;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

/**
 * Various date/time utilities
 */
public final class DateTimeUtils {

    /** Date time formatter yyyy-MM-dd'T'HH:mm:ssZ  and supports parsing  yyyy-MM-dd'T'HH:mm:ssZZ  */
    private static final DateTimeFormatter DATE_TIME_FORMATTER = ISODateTimeFormat.dateTimeNoMillis().withZoneUTC();
    /** ISO Date Format "yyyy-MM-dd" */
    public static final DateTimeFormatter DATE_FORMATTER = ISODateTimeFormat.date();
    
    /** don't instantiate me! */
    private DateTimeUtils() {
        // noop
    }

    /**
     * Returns a DateTime for the specified instant.
     */
    public static DateTime getDateTime(long instant) {
        return new DateTime(instant, ISOChronology.getInstanceUTC());
    }

    /**
     * Parses a string as a date time object using this format:
     *      yyyy-MM-dd'T'HH:mm:ssZ
     *
     * The returned date time's time zone is set to UTC.
     *
     * If argument value contains only the date information,
     * parses and returns the DateTime object at midnight UTC
     *
     * @param value the String
     * @return the equivalent DateTime
     */
    public static DateTime parse(final String value) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(value), "a valid date-time string is required");
        if (value.length() == 10) {
            return DATE_FORMATTER.parseDateTime(value);
        }

        return DATE_TIME_FORMATTER.parseDateTime(value);
    }

    /**
     * Returns a string representing the textual equivalent
     * of a given date time object in this format:
     *      yyyy-MM-dd'T'HH:mm:ssZ
     *
     * The returned string will represent the date in the UTC
     * time zone.
     *
     * @param value the DateTime
     * @return the equivalent String
     */
    public static String print(final DateTime value) {
        return DATE_TIME_FORMATTER.print(value);
    }
}
