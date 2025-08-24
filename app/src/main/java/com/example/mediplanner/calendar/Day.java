package com.example.mediplanner.calendar;

import com.example.mediplanner.plan.Plan;
import com.example.mediplanner.plan.TreatmentUsage;
import com.example.mediplanner.plan.TreatmentUse;

import org.joda.time.Hours;

import java.util.ArrayList;

public class Day {

    private int dayOfMonth;
    private int dayOfWeek;

    private ArrayList<TreatmentUsage> treatmentusages;


    public Day(int day, int dayOfWeek) {
        setDayOfMonth(day);
        setDayOfWeek(dayOfWeek);
        treatmentusages = new ArrayList<>();
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


    public ArrayList<TreatmentUsage> getTreatmentusages() {
        return treatmentusages;
    }

}
