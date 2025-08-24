package com.example.mediplanner.calendar;

import java.util.ArrayList;

public class Week {
    private ArrayList<Day> daysOfWeek;

    public Week() {
        daysOfWeek = new ArrayList<>();
    }

    public ArrayList<Day> getDaysOfWeek() {
        return daysOfWeek;
    }
}
