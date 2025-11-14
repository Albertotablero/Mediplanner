package com.example.mediplanner.calendar;

import com.example.mediplanner.plan.TreatmentUsage;

import java.util.HashMap;
import java.util.Map;

public class Day {

    private int dayOfMonth;
    private int dayOfWeek;
    private int weekOfMonth;
    private int weekOfYear;

    private Map<String, TreatmentUsage> treatmentUsages;


    public Day(int day, int dayOfWeek, int weekOfMonth) {
        setDayOfMonth(day);
        setDayOfWeek(dayOfWeek);
        setWeekOfMonth(weekOfMonth);
        setWeekOfYear(weekOfYear);
        treatmentUsages = new HashMap<>();
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    private void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public Map<String, TreatmentUsage> getTreatmentUsages() {
        return treatmentUsages;
    }

    public int getWeekOfMonth() {
        return weekOfMonth;
    }

    public void setWeekOfMonth(int weekOfMonth) {
        this.weekOfMonth = weekOfMonth;
    }

    public int getWeekOfYear() {
        return weekOfYear;
    }

    public void setWeekOfYear(int weekOfYear) {
        this.weekOfYear = weekOfYear;
    }
}
