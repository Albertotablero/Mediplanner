package com.example.mediplanner.calendar;

import com.example.mediplanner.date.PlanHours;
import com.example.mediplanner.plan.Plan;
import com.example.mediplanner.plan.TreatmentUsage;

import org.joda.time.Hours;
import org.joda.time.LocalDate;

public class DayTreatmentPlansFiller {

    // Fill stored months with plans with daily frequency
    public static void fillDailyPlan(Plan p, Month month) {
        // If there ar no specified hours just add them once
        if (p.getPlanHours().isEmpty()) {
            for (Week w : month.getWeeks()) {
                for (Day d : w.getDaysOfWeek()) {
                    if (checkStartDate(p, d, month))
                        d.getTreatmentusages().add(new TreatmentUsage(p));
                }
            }
        } else { // Otherways they get added for every hour specified
            for (PlanHours h :
                    p.getPlanHours()) {
                for (Week w : month.getWeeks()) {
                    for (Day d : w.getDaysOfWeek()) {
                        d.getTreatmentusages().add(new TreatmentUsage(p, h.hours, h.minutes));
                    }
                }
            }
        }

    }

    private static boolean checkStartDate(Plan p, Day day, Month monthOfYear) {

        LocalDate date = LocalDate.parse(monthOfYear.getYear() + "-" + monthOfYear.getMonthOfYear() + "-" + day.getDayOfMonth());

        return date.isAfter(p.getStartDate()) || date.isEqual(p.getStartDate());
    }
}
