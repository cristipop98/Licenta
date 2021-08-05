package com.example.patientinterface;

import java.util.ArrayList;
import java.util.Calendar;

public class TimeIntervalHelper {
    public static void generateTimeInterval(final ArrayList<String> intervals, final int startHour, final boolean isStartHalf, int endHour, final boolean isEndHalf) {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, startHour);
        calendar.set(Calendar.MINUTE, isStartHalf ? 30 : 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        endHour = endHour == 12 ? 0 : endHour;
        while (calendar.get(Calendar.HOUR) != endHour)
            intervals.add(getInterval(calendar));
        intervals.add(getInterval(calendar));

        if (isEndHalf)
            intervals.add(getInterval(calendar));
    }

    public static void generateTimeInterval(final ArrayList<String> intervals, final int startHour, int endHour) {
        generateTimeInterval(intervals, startHour, false, endHour, false);
    }

    private static String getInterval(final Calendar calendar) {
        final String interval = String.format(
                "%d.%02d",
                calendar.get(Calendar.HOUR) != 0 ? calendar.get(Calendar.HOUR) : 12,
                calendar.get(Calendar.MINUTE)
        );
        calendar.add(Calendar.MINUTE, 30);
        return interval;
    }
}
