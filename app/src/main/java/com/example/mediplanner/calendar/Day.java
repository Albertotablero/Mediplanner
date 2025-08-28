package com.example.mediplanner.calendar;

import com.example.mediplanner.plan.TreatmentUsage;

import java.util.HashMap;
import java.util.Map;

public class Day {

    private int dayOfMonth;
    private int dayOfWeek;

    private Map<String, TreatmentUsage> treatmentUsages;


    public Day(int day, int dayOfWeek) {
        setDayOfMonth(day);
        setDayOfWeek(dayOfWeek);
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
}
