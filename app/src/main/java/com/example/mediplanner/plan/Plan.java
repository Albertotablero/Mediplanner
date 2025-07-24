package com.example.mediplanner.plan;

import com.example.mediplanner.utils.FrequencyMetric;
import com.example.mediplanner.utils.TreatmentForm;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Plan {

    private String name = ""; //Name of the medicine
    private String details = ""; //Important details to take note of
    private float frequency; // How often to take treatment plan
    private FrequencyMetric frequencyMetric; // How to measure frequency
    private Date startDate; // Start date of treatment plan
    private boolean isPermanent; // Whether is permanent as opossed to having a set duration
    private float duration; // Duration of the treatment plan
    private FrequencyMetric durationMetric; //How to measure the duration
    private boolean isActive; // Whether the treatment plan is active or not
    private TreatmentForm form; // Form of the treatment (Medium of consumption)ç

    //for specific treatment forms or frequencies
    private ArrayList<String> specificDates;
    private ArrayList<String> dailyHours;
    private ArrayList<Boolean> weekdaysActive;


    public Plan(String name, String details, boolean isActive, float frequency, int fMetric,
                LocalDate startDate, boolean isPermanent, float duration, FrequencyMetric durationMetric) {
        initialize();
        setName(name);
        setDetails(details);
        setActive(isActive);
        setFrequency(frequency);
        setFrequencyMetric(fMetric);
        setPermanent(isPermanent);
        setDuration(duration);
        setDurationMetric(durationMetric);
        setStartDate(Calendar.getInstance().getTime());
    }

    public Plan() {
        initialize();

    }

    private void initialize() {
        setStartDate(Calendar.getInstance().getTime());
        setActive(false);

        dailyHours = new ArrayList<String>();
        //Test hours
        dailyHours.add(DateTime.now().getHourOfDay() + ":" + DateTime.now().getMinuteOfHour());
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

    public float getFrequency() {
        return frequency;
    }

    public void setFrequency(float frequency) {
        this.frequency = frequency;
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public boolean isPermanent() {
        return isPermanent;
    }

    public void setPermanent(boolean permanent) {
        isPermanent = permanent;
    }

    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public FrequencyMetric getDurationMetric() {
        return durationMetric;
    }

    public void setDurationMetric(FrequencyMetric durationMetric) {
        this.durationMetric = durationMetric;
    }

    public TreatmentForm getForm() {
        return form;
    }

    public void setForm(TreatmentForm form) {
        this.form = form;
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
}
