package com.example.mediplanner.plan;

import com.example.mediplanner.date.PlanHours;
import com.example.mediplanner.utils.FrequencyMetric;
import com.example.mediplanner.utils.TreatmentForm;

import org.joda.time.LocalDate;

import java.util.ArrayList;

public class TreatmentPlan {

    private String name = ""; //Name of the medicine
    private String details = ""; //Important details to take note of
    private FrequencyMetric frequencyMetric; // How to measure frequency
    private int hourIntervalFrequency;
    private LocalDate startDate; // Start date of treatment plan
    private boolean isPermanent; // Whether is permanent as opposed to having a set duration
    private LocalDate endDate;
    private boolean isActive; // Whether the treatment plan is active or not
    private TreatmentForm form; // Form of the treatment (Medium of consumption)
    private float quantity;
    private String quantityMetric = "";

    //for specific treatment forms or frequencies
    private PlanHours initialHour;
    private ArrayList<String> specificDates;
    private ArrayList<PlanHours> planHours;
    private ArrayList<Boolean> weekdaysActive;


    public TreatmentPlan(String name, String details, boolean isActive, int freqMetric, int form, float quantity, String quantityMetric,
                         LocalDate startDate, boolean isPermanent, float duration, FrequencyMetric frequencyMetric) {
        initialize();
        setName(name);
        setDetails(details);
        setActive(isActive);
        if (freqMetric != -1) {
            setFrequencyMetric(freqMetric);
        } else {
            setFrequencyMetric(frequencyMetric);
        }
        setQuantity(quantity);
        setQuantityMetric(quantityMetric);
        setForm(form);
        setPermanent(isPermanent);
        setStartDate(startDate);
    }

    public TreatmentPlan() {
        initialize();
    }

    private void initialize() {
        setStartDate(LocalDate.now());
        setActive(false);

        planHours = new ArrayList<>();
        weekdaysActive = new ArrayList<>();
        specificDates = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }


    public FrequencyMetric getFrequencyMetric() {
        return frequencyMetric;
    }

    public void setFrequencyMetric(FrequencyMetric frequencyMetric) {
        this.frequencyMetric = frequencyMetric;
    }

    public void setFrequencyMetric(int frequencyMetric) {

        switch (frequencyMetric) {
            case 1:
                this.frequencyMetric = FrequencyMetric.HOURLY;
                break;
            case 3:
                this.frequencyMetric = FrequencyMetric.WEEKLY;
                break;
            case 4:
                this.frequencyMetric = FrequencyMetric.MONTHLY;
                break;
            case 5:
                this.frequencyMetric = FrequencyMetric.YEARLY;
                break;
            case 6:
                this.frequencyMetric = FrequencyMetric.SPECIFFICDATES;
                break;
            case 7:
                this.frequencyMetric = FrequencyMetric.ASNEEDED;
                break;
            default:
                this.frequencyMetric = FrequencyMetric.DAILY;
                break;
        }

    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public boolean isPermanent() {
        return isPermanent;
    }

    public void setPermanent(boolean permanent) {
        isPermanent = permanent;
    }

    public TreatmentForm getForm() {
        return form;
    }

    public void setForm(int treatmentForm) {
        switch (treatmentForm) {
            case 1:
                this.form = TreatmentForm.PILLS;
                break;
            case 2:
                this.form = TreatmentForm.LIQUID;
                break;
            case 3:
                this.form = TreatmentForm.DROPS;
                break;
            case 4:
                this.form = TreatmentForm.INHALER;
                break;
            case 5:
                this.form = TreatmentForm.INJECTION;
                break;
            case 6:
                this.form = TreatmentForm.CREAM;
                break;
            case 7:
                this.form = TreatmentForm.PATCHES;
                break;
            case 8:
                this.form = TreatmentForm.PACKET;
            default:
                this.form = TreatmentForm.PILLS;
                break;
        }
    }

    public ArrayList<String> getSpecificDates() {
        return specificDates;
    }

    public void setSpecificDates(ArrayList<String> specificDates) {
        this.specificDates = specificDates;
    }

    public ArrayList<PlanHours> getPlanHours() {
        return planHours;
    }

    public ArrayList<Boolean> getWeekdaysActive() {
        return weekdaysActive;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public String getQuantityMetric() {
        return quantityMetric;
    }

    public void setQuantityMetric(String quantityMetric) {
        this.quantityMetric = quantityMetric;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public int getHourIntervalFrequency() {
        return hourIntervalFrequency;
    }

    public void setHourIntervalFrequency(int hourIntervalFrequency) {
        this.hourIntervalFrequency = hourIntervalFrequency;
    }

    public void setForm(TreatmentForm form) {
        this.form = form;
    }

    public PlanHours getInitialHour() {
        return initialHour;
    }

    public void setInitialHour(PlanHours initialHour) {
        this.initialHour = initialHour;
    }
}
