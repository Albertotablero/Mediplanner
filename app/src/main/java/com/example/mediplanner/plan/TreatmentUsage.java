package com.example.mediplanner.plan;

import com.example.mediplanner.date.PlanHours;

public class TreatmentUsage {
    public TreatmentPlan treatment;
    public TreatmentUse usage;
    public PlanHours hour;

    public TreatmentUsage(TreatmentPlan treatmentPlan, TreatmentUse treatmentUse, PlanHours hour) {
        this.usage = treatmentUse;
        this.treatment = treatmentPlan;
        this.hour = hour;
    }

    public TreatmentUsage(TreatmentPlan treatmentPlan) {
        this.treatment = treatmentPlan;
    }

    public TreatmentUsage(TreatmentPlan treatmentPlan, TreatmentUse treatmentUse) {
        this.usage = treatmentUse;
        this.treatment = treatmentPlan;
    }
}