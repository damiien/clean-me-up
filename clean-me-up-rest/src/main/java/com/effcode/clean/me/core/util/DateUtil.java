package com.effcode.clean.me.core.util;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Date utility class, contains date & time manipulation methods
 *
 * @author dame.gjorgjievski
 * @version 1.0
 * @since 1.0
 */
public final class DateUtil {

    /**
     * Sealed constructor
     */
    private DateUtil() {
    }

    /**
     * Provides the current date and time with UTC offset
     *
     * @return current UTC date and time
     */
    public static Date utc() {

        final Calendar c = Calendar.getInstance();
        final TimeZone z = c.getTimeZone();

        int offset = z.getRawOffset();
        if (z.inDaylightTime(new Date())) {
            offset = offset + z.getDSTSavings();
        }
        final int offsetHours = offset / 1000 / 60 / 60;
        final int offsetMins = offset / 1000 / 60 % 60;

        c.add(Calendar.HOUR_OF_DAY, -offsetHours);
        c.add(Calendar.MINUTE, -offsetMins);
        return c.getTime();
    }
}
