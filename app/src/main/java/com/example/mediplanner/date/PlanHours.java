package com.example.mediplanner.date;

import org.joda.time.Hours;
import org.joda.time.Minutes;

public class PlanHours {
    public Hours hours;
    public Minutes minutes;

    public PlanHours(int hour, int minute) {
        this.hours = Hours.hours(hour);
        this.minutes = Minutes.minutes(minute);
    }
}
