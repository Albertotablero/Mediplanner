package com.example.mediplanner.calendar;

import com.example.mediplanner.date.PlanHours;
import com.example.mediplanner.plan.TreatmentPlan;
import com.example.mediplanner.plan.TreatmentUsage;
import com.example.mediplanner.plan.TreatmentUse;

import org.joda.time.LocalDate;

import java.util.Locale;

public class DayTreatmentPlansFiller {
    private static final String TREATMENT_PLAN_USAGE_TAG_FORMAT = "%1$2d/%2$2d/%3$4d/%4$d/%5$s/%6$02d:%7$02d";

    // Fill stored months with plans with daily frequency
    public static void fillDailyPlan(TreatmentPlan p, Month month) {
        // If there ar no specified hours just add them once
        if (p.getPlanHours().isEmpty()) {
            for (Day day : month.getDays()) {
                if (checkStartDate(p, day, month))
                    day.getTreatmentUsages().putIfAbsent(tagFor(day.getDayOfMonth(), month.getMonthOfYear(), month.getYear(), p.getName(), 0, 0), new TreatmentUsage(p, TreatmentUse.UNKOWWN));
            }

        } else { // Otherways they get added for every hour specified
            for (Day day : month.getDays()) {
                if (checkStartDate(p, day, month)) {
                    for (PlanHours hour :
                            p.getPlanHours()) {
                        day.getTreatmentUsages().putIfAbsent(
                                tagFor(day.getDayOfMonth(), month.getMonthOfYear(), month.getYear(), p.getName(), hour.hours.getHours(), hour.minutes.getMinutes()),
                                new TreatmentUsage(p, TreatmentUse.UNKOWWN, hour));
                    }
                }
            }
        }
    }

    /*
     * The date needs to be >= than StarDate AND <= than EndDate if it isn't permanent
     *
     * */
    private static boolean checkStartDate(TreatmentPlan treatmentPlan, Day day, Month monthOfYear) {
        LocalDate date = new LocalDate(monthOfYear.getYear(), monthOfYear.getMonthOfYear(), day.getDayOfMonth());
        return (date.isAfter(treatmentPlan.getStartDate()) || date.isEqual(treatmentPlan.getStartDate())) &&
                (treatmentPlan.isPermanent() || (date.isBefore(treatmentPlan.getEndDate()) || date.isEqual(treatmentPlan.getEndDate())));
    }

    private static String tagFor(int dayOfMonth, int monthOfYear, int year, String name, int hour, int minute) {
        return String.format(Locale.getDefault(), TREATMENT_PLAN_USAGE_TAG_FORMAT, dayOfMonth, dayOfMonth, monthOfYear, year, name, hour, minute);
    }

}
