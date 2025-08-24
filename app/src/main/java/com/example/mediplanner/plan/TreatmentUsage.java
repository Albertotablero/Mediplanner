package com.example.mediplanner.plan;

import org.joda.time.Hours;
import org.joda.time.Minutes;

public class TreatmentUsage {
    public Plan treatment;
    public TreatmentUse usage;
    public Hours hour;
    public Minutes minutes;

    public TreatmentUsage(TreatmentUse usage, Plan treatmentPlan, Hours hour) {
        this.usage = usage;
        this.treatment = treatmentPlan;
    }

    public TreatmentUsage(Plan treatmentPlan, Hours hour, Minutes minutes) {
        this.usage = usage;
        this.treatment = treatmentPlan;
        this.minutes = minutes;
        this.hour = hour;
    }

    public TreatmentUsage(Plan treatmentPlan) {
        this.usage = usage;
        this.treatment = treatmentPlan;
    }
}