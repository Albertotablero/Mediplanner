package com.example.mediplanner.plan;

import com.example.mediplanner.utils.FrequencyMetric;
import com.example.mediplanner.utils.TreatmentForm;

import org.joda.time.LocalDate;

import java.util.ArrayList;

public class Plan {

    private String name = ""; //Name of the medicine
    private String details = ""; //Important details to take note of
    private FrequencyMetric frequencyMetric; // How to measure frequency
    private LocalDate startDate; // Start date of treatment plan
    private boolean isPermanent; // Whether is permanent as opossed to having a set duration
    //private float duration; // Duration of the treatment plan
    private boolean isActive; // Whether the treatment plan is active or not
    private TreatmentForm form; // Form of the treatment (Medium of consumption)
    private float quantity;
    private String quantityMetric = "";

    //for specific treatment forms or frequencies
    private ArrayList<String> specificDates;
    private ArrayList<String> dailyHours;
    private ArrayList<Boolean> weekdaysActive;


    public Plan(String name, String details, boolean isActive, int fMetric,
                LocalDate startDate, boolean isPermanent, float duration, FrequencyMetric durationMetric) {
        initialize();
        setName(name);
        setDetails(details);
        setActive(isActive);
        setFrequencyMetric(fMetric);
        setPermanent(isPermanent);
        setStartDate(startDate);
    }

    public Plan() {
        initialize();

    }

    private void initialize() {
        setStartDate(LocalDate.now());
        setActive(false);

        dailyHours = new ArrayList<String>();
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

    public void setFrequencyMetric(int frequencyMetric) {

        switch (frequencyMetric) {
            case 1:
                this.frequencyMetric = FrequencyMetric.DAILY;
                break;
            case 2:
                this.frequencyMetric = FrequencyMetric.WEEKLY;
                break;
            case 3:
                this.frequencyMetric = FrequencyMetric.MONTHLY;
                break;
            case 4:
                this.frequencyMetric = FrequencyMetric.YEARLY;
                break;
            case 5:
                this.frequencyMetric = FrequencyMetric.SPECIFFICDATES;
                break;
            case 6:
                this.frequencyMetric = FrequencyMetric.ASNEEDED;
                break;
            default:
                this.frequencyMetric = FrequencyMetric.HOURLY;
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

    public ArrayList<String> getDailyHours() {
        return dailyHours;
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
}
