package com.example.mediplanner.calendar;

import android.util.Log;

import java.time.LocalDate;
import java.util.ArrayList;

public class Month {

    // list of days in the month
    private ArrayList<Week> weeks;
    private int monthOfYear;
    private int year;

    public Month(int month, int year) {
        weeks = new ArrayList<>();
        setMonthOfYear(month);
        setYear(year);
        setUpDays();
        // TODO Remove after testing
        Log.d("Monthtesting", month + "");
        Log.d("Monthtesting", weeks.size() + "");
        Week lastWeek = weeks.get(weeks.size() - 1);
        Log.d("Monthtesting", lastWeek.getDaysOfWeek().get(lastWeek.getDaysOfWeek().size() - 1).getDayOfMonth() + "");
    }

    private void setUpDays() {
        // Checking length of month and populating list of days
        LocalDate localDate = LocalDate.of(getYear(), getMonthOfYear(), 1);
        int monthLength = localDate.getMonth().length(localDate.isLeapYear());
        int dayOfWeek = localDate.getDayOfWeek().getValue();
        Week week = new Week();

        for (int i = 1; i <= monthLength; i++) {
            if (dayOfWeek == 1) { // if monay, new week and increment counter
                week = new Week();
                week.getDaysOfWeek().add(new Day(i, dayOfWeek));
                dayOfWeek++;
            } else if (dayOfWeek == 7) { // if sunday, add week and reset counter
                week.getDaysOfWeek().add(new Day(i, dayOfWeek));
                weeks.add(week);
                dayOfWeek = 1;
            } else if (i == monthLength) {
                week.getDaysOfWeek().add(new Day(i, dayOfWeek));
                weeks.add(week);
            } else { // otherwise just increment counter
                week.getDaysOfWeek().add(new Day(i, dayOfWeek));
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
}
