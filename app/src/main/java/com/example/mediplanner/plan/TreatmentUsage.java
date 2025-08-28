package com.example.mediplanner.plan;

import com.example.mediplanner.date.PlanHours;

import org.joda.time.Hours;
import org.joda.time.Minutes;

public class TreatmentUsage {
    public Plan treatment;
    public TreatmentUse usage;
    public PlanHours hour;

    public TreatmentUsage(Plan treatmentPlan, TreatmentUse treatmentUse, PlanHours hour) {
        this.usage = treatmentUse;
        this.treatment = treatmentPlan;
        this.hour = hour;
    }

    public TreatmentUsage(Plan treatmentPlan) {
        this.treatment = treatmentPlan;
    }

    public TreatmentUsage(Plan treatmentPlan, TreatmentUse treatmentUse) {
        this.usage = treatmentUse;
        this.treatment = treatmentPlan;
    }
}