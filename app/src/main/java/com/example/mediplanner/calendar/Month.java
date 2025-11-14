package com.example.mediplanner.calendar;

import java.time.LocalDate;
import java.util.ArrayList;

public class Month {

    // list of days in the month
    private ArrayList<Day> days;
    private int monthOfYear;
    private int year;

    public Month(int month, int year) {
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
        int weekOfMonth = 1;

        for (int i = 1; i <= monthLength; i++) {
            localDate = LocalDate.of(getYear(), getMonthOfYear(), i);
            weekOfMonth = dayOfWeek > localDate.getDayOfWeek().getValue() ? weekOfMonth + 1 : weekOfMonth;
            dayOfWeek = localDate.getDayOfWeek().getValue();

            days.add(new Day(i,
                    dayOfWeek,
                    weekOfMonth
            ));
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

    public ArrayList<Day> getDays() {
        return days;
    }
}
