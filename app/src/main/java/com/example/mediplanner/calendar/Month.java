package com.example.mediplanner.calendar;

import android.util.Log;

import java.time.LocalDate;
import java.util.ArrayList;

public class Month {

    // list of days in the month
    private ArrayList<Week> weeks;
    private ArrayList<Day> days;
    private int monthOfYear;
    private int year;

    public Month(int month, int year) {
        weeks = new ArrayList<>();
        days = new ArrayList<>();
        setMonthOfYear(month);
        setYear(year);
        setUpDays();
    }

    private void setUpDays() {
        // Checking length of month and populating list of days
        LocalDate localDate = LocalDate.of(getYear(), getMonthOfYear(), 1);
        int monthLength = localDate.getMonth().length(localDate.isLeapYear());
        int dayOfWeek = localDate.getDayOfWeek().getValue();
        Week week = new Week();

        for (int i = 1; i <= monthLength; i++) {
            Day newDay = new Day(i, dayOfWeek);
            days.add(newDay);
            if (dayOfWeek == 1) { // if monay, new week and increment counter
                week = new Week();
                week.getDaysOfWeek().add(newDay);

                dayOfWeek++;
            } else if (dayOfWeek == 7) { // if sunday, add week and reset counter
                week.getDaysOfWeek().add(newDay);
                weeks.add(week);

                dayOfWeek = 1;
            } else if (i == monthLength) {
                week.getDaysOfWeek().add(newDay);
                weeks.add(week);

            } else { // otherwise just increment counter
                week.getDaysOfWeek().add(newDay);

                dayOfWeek++;

            }
        }

    }

    public int getMonthOfYear() {
        return monthOfYear;
    }

    private void setMonthOfYear(int monthOfYear) {
        this.monthOfYear = monthOfYear;
    }

    public int getYear() {
        return year;
    }

    private void setYear(int year) {
        this.year = year;
    }

    public ArrayList<Week> getWeeks() {
        return weeks;
    }

    public ArrayList<Day> getDays() {
        return days;
    }
}
