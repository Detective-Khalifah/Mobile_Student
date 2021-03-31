package com.blogspot.thengnet.mobilestudent.data;

import java.util.concurrent.TimeUnit;

public class TimeConverter {

    /**
     * Utility method to convert time from source type
     *
     * @param milliseconds fetched time in same unit
     * @return converted time in seconds, minutes and hours.
     */
    public static String convertTime (String milliseconds) {
        long hour, minute, second, millisecond;
        millisecond = Long.parseLong(milliseconds);
        StringBuilder time = new StringBuilder();

        hour = TimeUnit.HOURS.convert(millisecond, TimeUnit.MILLISECONDS);
        minute = TimeUnit.MINUTES.convert(millisecond, TimeUnit.MILLISECONDS);
        second = TimeUnit.SECONDS.convert(millisecond, TimeUnit.MILLISECONDS);

        if (hour > 0)
            time.append(hour).append(":");

        if (minute > 9) {
            if (minute > 59) {
                long remainingMinutes = minute - ((minute / 60) * 60);
                if (remainingMinutes > 9)
                    time.append(remainingMinutes).append(":");
                else
                    time.append("0").append(remainingMinutes).append(":");
            } else
                time.append(minute).append(":");
        } else
            time.append("0").append(minute).append(":");

        if (second > 9) {
            if (second > 59) {
                long remainingSeconds = second - (second / 60) * 60;
                if (remainingSeconds > 9)
                    time.append(remainingSeconds);
                else
                    time.append("0").append(remainingSeconds);
            } else
                time.append(second);
        } else {
            time.append("0").append(second);
        }

        return String.valueOf(time);
    }

}
