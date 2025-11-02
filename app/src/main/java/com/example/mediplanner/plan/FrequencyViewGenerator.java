package com.example.mediplanner.plan;

import static com.example.mediplanner.plan.PlanFragment.DATE_FORMAT;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.example.mediplanner.R;
import com.example.mediplanner.date.PlanHours;
import com.example.mediplanner.utils.HoursInADayAdapter;
import com.example.mediplanner.utils.HoursListViewAdapter;
import com.example.mediplanner.utils.NoScrollListView;
import com.example.mediplanner.utils.SpecificDateViewAdapter;

import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.Minutes;

import java.util.ArrayList;
import java.util.Locale;

public class FrequencyViewGenerator {

    /*
        For EDIT views

     */
    public static @NonNull RelativeLayout setUpHoursIntervalEditView(LayoutInflater layoutInflater, Context context, TreatmentPlan treatmentPlan) {
        RelativeLayout houtIntervalView = (RelativeLayout) layoutInflater.inflate(R.layout.hour_interval_frequency_view, null, false);

        EditText hoursInterval = houtIntervalView.findViewById(R.id.hourIntervalEditText);
        hoursInterval.setHint(R.string.dailyFreqHintText);

        // This list will show expected hours to be set up on a day
        NoScrollListView list = houtIntervalView.findViewById(R.id.hoursInADay);
        ArrayList<PlanHours> planHours = new ArrayList<>();
        HoursInADayAdapter adapter = new HoursInADayAdapter(context, 0, planHours);
        list.setAdapter(adapter);

        // Number pickers for init Hour from which the rest will be calculated, sharing a listener
        NumberPicker hourPicker = houtIntervalView.findViewById(R.id.monthPicker);
        hourPicker.setTag("hourP");
        hourPicker.setMinValue(0);
        hourPicker.setMaxValue(23);

        NumberPicker minutePicker = houtIntervalView.findViewById(R.id.yearPicker);
        minutePicker.setTag("minuteP");
        minutePicker.setMinValue(0);
        minutePicker.setMaxValue(59);

        PlanHours iniTHourMinutePicked = new PlanHours(DateTime.now().getHourOfDay(), DateTime.now().getMinuteOfHour());

        NumberPicker.OnValueChangeListener onValueChangedListener = new NumberPicker.OnValueChangeListener() {
            private boolean generationFlag;

            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if (!generationFlag && oldVal != newVal && hoursInterval.getText().length() != 0) {
                    generationFlag = true;

                    // Ill comment this later
                    iniTHourMinutePicked.hours = Hours.hours(hourPicker.getValue());
                    iniTHourMinutePicked.minutes = Minutes.minutes(minutePicker.getValue());
                    adapter.clear();
                    boolean isSameDay = true;
                    PlanHours hoursToAdd = iniTHourMinutePicked;
                    adapter.add(hoursToAdd);

                    // Adding hours to preview the distribution in a day
                    do {
                        LocalDateTime date = new LocalDateTime(treatmentPlan.getStartDate().getYear(), treatmentPlan.getStartDate().getMonthOfYear(),
                                treatmentPlan.getStartDate().getDayOfMonth(), hoursToAdd.hours.getHours(), hoursToAdd.minutes.getMinutes());
                        LocalDateTime newDate = date.plusHours(Integer.parseInt(hoursInterval.getText().toString()));

                        if (newDate.getDayOfMonth() == date.getDayOfMonth()) {
                            hoursToAdd = new PlanHours(newDate.getHourOfDay(), newDate.getMinuteOfHour());
                            adapter.add(hoursToAdd);
                        } else {
                            isSameDay = false;
                        }

                    } while (isSameDay);
                    generationFlag = false;
                }
            }
        };
        hourPicker.setOnValueChangedListener(onValueChangedListener);
        minutePicker.setOnValueChangedListener(onValueChangedListener);

        hourPicker.setValue(iniTHourMinutePicked.hours.getHours());
        minutePicker.setValue(iniTHourMinutePicked.minutes.getMinutes());

        return houtIntervalView;
    }

    public static @NonNull ListView setUpDailyEditView(LayoutInflater layoutInflater, Context context, TreatmentPlan treatmentPlan, boolean isNew, FragmentManager fragmentManager) {
        // Create list view to add or remove hours for daily treatments
        NoScrollListView dailyHoursView = new NoScrollListView(context);

        // Setting up adapter for list view
        if (treatmentPlan.getPlanHours().isEmpty() && isNew) {
            DateTime now = DateTime.now();
            treatmentPlan.getPlanHours().add(new PlanHours(now.getHourOfDay(), now.getMinuteOfHour()));
        }

        HoursListViewAdapter hoursAdapter = new HoursListViewAdapter(context, treatmentPlan.getPlanHours(), fragmentManager);
        dailyHoursView.setAdapter(hoursAdapter);
        dailyHoursView.setNestedScrollingEnabled(true);
        dailyHoursView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        // For adding another hour to the listview
        Button addHours = new Button(context);
        addHours.setText(R.string.add);
        addHours.setOnClickListener(e -> addHours(hoursAdapter));
        addHours.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        dailyHoursView.addFooterView(addHours);
        return dailyHoursView;
    }

    public static @NonNull RelativeLayout setUpWeeklyEditView(LayoutInflater layoutInflater, Context context, TreatmentPlan treatmentPlan, boolean isNew, FragmentManager fragmentManager) {
        // Inflating a week day piker for the weekly frequency type
        RelativeLayout weeklyView = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.week_day_picker_widget, null, false);
        // Setting up functionality for each button
        ArrayList<Boolean> weeksActive = new ArrayList<Boolean>();
        for (int i = 0; i < 7; i++) {
            weeksActive.add(false);
        }

        // Monday
        setUpWeekButtons(weeklyView, R.id.mondayButton, 0, weeksActive, true);
        // Tuesday
        setUpWeekButtons(weeklyView, R.id.tuesdayButton, 1, weeksActive, true);
        // Wednesda,truey
        setUpWeekButtons(weeklyView, R.id.wednesdayButton, 2, weeksActive, true);
        // Thursday
        setUpWeekButtons(weeklyView, R.id.thursdayButton, 3, weeksActive, true);
        // Friday
        setUpWeekButtons(weeklyView, R.id.fridayButton, 4, weeksActive, true);
        // Saturday
        setUpWeekButtons(weeklyView, R.id.saturdayButton, 5, weeksActive, true);
        // Sunday
        setUpWeekButtons(weeklyView, R.id.sundayButton, 6, weeksActive, true);

        //Managing the hour for the treatment
        HoursListViewAdapter hoursAdapterWeekly = new HoursListViewAdapter(context, treatmentPlan.getPlanHours(), fragmentManager);
        NoScrollListView weeklyHours = weeklyView.findViewById(R.id.hoursListWeekly);
        weeklyHours.setAdapter(hoursAdapterWeekly);

        // Button for adding more hours
        Button addHours = new Button(context);
        addHours.setText(R.string.add);
        addHours.setOnClickListener(e -> addHours(hoursAdapterWeekly));
        weeklyHours.addFooterView(addHours);

        if (treatmentPlan.getPlanHours().isEmpty() && isNew)
            hoursAdapterWeekly.add(new PlanHours(DateTime.now().getHourOfDay(), DateTime.now().getMinuteOfHour()));

        return weeklyView;
    }

    public static @NonNull NoScrollListView setUpSpecificDateEditView(Context context, TreatmentPlan treatmentPlan) {
        // Hiding unnecessary items


        NoScrollListView specificDatesListView = new NoScrollListView(context);

        SpecificDateViewAdapter specificDateViewAdapter = new SpecificDateViewAdapter(context, treatmentPlan.getSpecificDates(), DATE_FORMAT);
        specificDatesListView.setAdapter(specificDateViewAdapter);

        if (treatmentPlan.getSpecificDates().isEmpty()) {
            LocalDate now = LocalDate.now();
            specificDateViewAdapter.add(String.format(Locale.getDefault(), DATE_FORMAT, now.getDayOfMonth(), now.getMonthOfYear(), now.getYear()));
        }

        Button addDateButton = new Button(context);
        addDateButton.setText(R.string.add);
        addDateButton.setOnClickListener(e -> addDate(specificDateViewAdapter));

        specificDatesListView.addFooterView(addDateButton);
        return specificDatesListView;
    }



    /*
        For DISPLAY views
     */

    public static View setUpHoursIntervalDisplayView(LayoutInflater layoutInflater, Context context, TreatmentPlan treatmentPlan) {
        RelativeLayout hourIntervalView = (RelativeLayout) layoutInflater.inflate(R.layout.hour_interval_frequency_view, null, false);

        EditText hoursInterval = hourIntervalView.findViewById(R.id.hourIntervalEditText);
        hoursInterval.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        hoursInterval.setText(String.format(Locale.getDefault(), "%1$02d", treatmentPlan.getHourIntervalFrequency()));

        // Hiding anything not needed for display purposes
        hourIntervalView.findViewById(R.id.hoursInADay).setVisibility(View.GONE);
        hourIntervalView.findViewById(R.id.monthPicker).setVisibility(View.GONE);
        hourIntervalView.findViewById(R.id.yearPicker).setVisibility(View.GONE);
        hourIntervalView.findViewById(R.id.separator).setVisibility(View.GONE);
        hourIntervalView.findViewById(R.id.startTextView).setVisibility(View.GONE);

        return hourIntervalView;
    }

    public static @NonNull View setUpDailyDisplayView(LayoutInflater layoutInflater, Context context, TreatmentPlan treatmentPlan, boolean isNew, String[] freqTextArray) {
        LinearLayout view = new LinearLayout(context);
        view.setOrientation(LinearLayout.VERTICAL);

        // Create list view to add or remove hours for daily treatments
        NoScrollListView dailyHoursView = new NoScrollListView(context);

        TextView frequencyTypeText = new TextView(context);
        frequencyTypeText.setTypeface(frequencyTypeText.getTypeface(), Typeface.BOLD);
        frequencyTypeText.setText(freqTextArray[treatmentPlan.getFrequencyMetric().getValue()]);
        view.addView(frequencyTypeText);

        // Setting up adapter for list view
        if (treatmentPlan.getPlanHours().isEmpty() && isNew) {
            DateTime now = DateTime.now();
            treatmentPlan.getPlanHours().add(new PlanHours(now.getHourOfDay(), now.getMinuteOfHour()));
        }
        if (!treatmentPlan.getPlanHours().isEmpty()) {
            HoursListViewAdapter hoursAdapter = new HoursListViewAdapter(context, treatmentPlan.getPlanHours());
            dailyHoursView.setAdapter(hoursAdapter);
            dailyHoursView.setNestedScrollingEnabled(true);
            dailyHoursView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            view.addView(dailyHoursView);
        }
        return view;
    }

    /*
        General use

     */
    private static void addDate(SpecificDateViewAdapter specificDateViewAdapter) {
        LocalDate today = LocalDate.now();
        int year = today.getYear();
        int month = today.getMonthOfYear();
        int day = today.getDayOfMonth();

        specificDateViewAdapter.add(String.format(DATE_FORMAT, day, month, year));

    }

    private static void addHours(HoursListViewAdapter hoursAdapter) {
        hoursAdapter.add(new PlanHours(DateTime.now().getHourOfDay(), DateTime.now().getMinuteOfHour()));
    }

    public static void setUpWeekButtons(RelativeLayout weeklyView, int buttonID, int dayOfWeek, ArrayList<Boolean> weeksActive, boolean display) {
        TextView b = weeklyView.findViewById(buttonID);

        // If it's for display it activates based on stored values, otherwise defaults to false (Not activated)
        b.setActivated(display ? weeksActive.get(dayOfWeek) : false);

        // If it's for display it doesn't need an OnClickListener
        if (!display)
            b.setOnClickListener(e -> changeWeekButtonSate(weeksActive, dayOfWeek, b));

    }

    // Negating the boolean representing the active state of the button and assigning that state to the button so that it changes bg dynamically
    private static void changeWeekButtonSate(ArrayList<Boolean> daysActive, int dayOfWeek, TextView weekdayButton) {
        daysActive.set(dayOfWeek, !daysActive.get(dayOfWeek));
        weekdayButton.setActivated(daysActive.get(dayOfWeek));
    }
}
