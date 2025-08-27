package com.example.mediplanner.plan;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.mediplanner.R;
import com.example.mediplanner.databinding.PlanFragmentViewBinding;
import com.example.mediplanner.date.PlanHours;
import com.example.mediplanner.treatments.TreatmentsArrayAdapter;
import com.example.mediplanner.utils.CloseListener;
import com.example.mediplanner.utils.HoursInADayAdapter;
import com.example.mediplanner.utils.HoursListViewAdapter;
import com.example.mediplanner.utils.MediPConstants;
import com.example.mediplanner.utils.NoScrollListView;
import com.example.mediplanner.utils.SpecificDateViewAdapter;
import com.example.mediplanner.utils.SpinnerAdapter;
import com.example.mediplanner.utils.TreatmentForm;

import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.Minutes;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class PlanFragment extends DialogFragment {

    public static final String DATE_FORMAT = MediPConstants.DATE_FORMAT;
    private PlanFragmentViewBinding planBinding;
    private Plan plan;
    private DatePickerDialog datePickerDialog;
    private ListView planList;

    // Check if creating plan or editing existing
    private boolean isNew;
    private boolean isCanceled = false;

    private CloseListener closeListener;

    private boolean generationFlag = false;


    public PlanFragment(CloseListener closeListener) {
        super();
        isNew = true;
        plan = new Plan();
        this.closeListener = closeListener;
    }

    public PlanFragment(Plan plan, ListView treatmentPlansListView) {
        super();
        isNew = false;
        this.plan = plan;
        planList = treatmentPlansListView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, R.style.Theme_Mediplanner);
    }

    //Setting up view with binging
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        planBinding = PlanFragmentViewBinding.inflate(inflater, container, false);
        return planBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        planBinding.fragmentContainerView.setVisibility(View.VISIBLE);
        planBinding.detailsTextInput.setHint(getResources().getString(R.string.treatment_details_prompt));

        // Setting up a date picker cause why not I fucking guess
        if (isNew) {
            LocalDate startDate = LocalDate.now();
            plan.setStartDate(startDate);
            planBinding.startDate.setText(String.format(Locale.getDefault(), DATE_FORMAT, startDate.dayOfMonth().get(), startDate.monthOfYear().get(), startDate.year().get()));
        }

        planBinding.calendarDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker dp, int year, int month, int dayOfMonth) {
                        planBinding.startDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }

                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        // Frequency Spinner
        SpinnerAdapter freqSpinAdapter = new SpinnerAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.freq_type_array), true);
        freqSpinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        planBinding.freqTypeSpinner.setAdapter(freqSpinAdapter);

        // Change view based on selected frequency typ
        planBinding.freqTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Removing the last child (Pending check before deleting and inserting the same view)
                if (planBinding.frequencyBox.getChildCount() > 1) {
                    planBinding.frequencyBox.removeViewAt(1);

                    // resetting visibility of start date after changing from specific dates
                    if (planBinding.startDate.getVisibility() != View.VISIBLE) {
                        planBinding.startDate.setVisibility(View.VISIBLE);
                        planBinding.StartDateTextView.setVisibility(View.VISIBLE);
                        planBinding.calendarDialogButton.setVisibility(View.VISIBLE);
                    }
                }

                View child = null;
                switch (position) {
                    // EVERY X HOURS
                    case 1:
                        child = setUpHoursIntervalView();
                        break;
                    //   DAILY
                    case 2:
                        child = setUpDailyView();
                        break;
                    // WEEKLY
                    case 3:
                        child = setUpWeeklyView();
                        break;
                    // SPECIFIC DATES
                    case 6:
                        child = setUpSpecificDateView();
                        break;
                    default:
                        break;

                }
                if (child != null) planBinding.frequencyBox.addView(child);
            }

            private @NonNull RelativeLayout setUpHoursIntervalView() {
                RelativeLayout houtIntervalView = (RelativeLayout) getLayoutInflater().inflate(R.layout.hour_interval_frequency_view, null, false);

                EditText hoursInterval = houtIntervalView.findViewById(R.id.hourIntervalEditText);
                hoursInterval.setHint(R.string.dailyFreqHintText);

                // This list will show expected hours to be set up on a day
                NoScrollListView list = houtIntervalView.findViewById(R.id.hoursInADay);
                ArrayList<PlanHours> planHours = new ArrayList<>();
                HoursInADayAdapter adapter = new HoursInADayAdapter(getContext(), 0, planHours);
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

                            // Adding hours to preview the distribution in a day
                            do {
                                LocalDateTime d = new LocalDateTime(plan.getStartDate().getYear(), plan.getStartDate().getMonthOfYear(),
                                        plan.getStartDate().getDayOfMonth(), hoursToAdd.hours.getHours(), hoursToAdd.minutes.getMinutes());
                                LocalDateTime nD = d.plusHours(Integer.parseInt(hoursInterval.getText().toString()));

                                if (nD.getDayOfMonth() == d.getDayOfMonth()) {
                                    adapter.add(hoursToAdd);
                                    hoursToAdd = new PlanHours(nD.getHourOfDay(), nD.getMinuteOfHour());
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

            private @NonNull ListView setUpDailyView() {
                // Create list view to add or remove hours for daily treatments
                NoScrollListView dailyHoursView = new NoScrollListView(getContext());

                // Setting up adapter for list view
                if (plan.getPlanHours().isEmpty() && isNew) {
                    DateTime now = DateTime.now();
                    plan.getPlanHours().add(new PlanHours(now.getHourOfDay(), now.getMinuteOfHour()));
                }

                HoursListViewAdapter hoursAdapter = new HoursListViewAdapter(getContext(), plan.getPlanHours(), getParentFragmentManager());
                dailyHoursView.setAdapter(hoursAdapter);
                //dailyHoursView.setEnabled(false);
                dailyHoursView.setNestedScrollingEnabled(true);

                // For adding another hour to the listview
                Button addHours = new Button(getContext());
                addHours.setText(R.string.add);
                addHours.setOnClickListener(e -> addHours(hoursAdapter));

                dailyHoursView.addFooterView(addHours);
                return dailyHoursView;
            }

            private @NonNull RelativeLayout setUpWeeklyView() {
                // Inflating a week day piker for the weekly frequency type
                RelativeLayout weeklyView = (RelativeLayout) LayoutInflater.from(getContext()).inflate(R.layout.week_day_picker_widget, planBinding.frequencyBox, false);
                // Setting up functionality for each button
                ArrayList<Boolean> weeksActive = new ArrayList<Boolean>();
                for (int i = 0; i < 7; i++) {
                    weeksActive.add(false);
                }

                // Monday
                setUpWeekButtons(weeklyView, R.id.mondayButton, 0, weeksActive);
                // Tuesday
                setUpWeekButtons(weeklyView, R.id.tuesdayButton, 1, weeksActive);
                // Wednesday
                setUpWeekButtons(weeklyView, R.id.wednesdayButton, 2, weeksActive);
                // Thursday
                setUpWeekButtons(weeklyView, R.id.thursdayButton, 3, weeksActive);
                // Friday
                setUpWeekButtons(weeklyView, R.id.fridayButton, 4, weeksActive);
                // Saturday
                setUpWeekButtons(weeklyView, R.id.saturdayButton, 5, weeksActive);
                // Sunday
                setUpWeekButtons(weeklyView, R.id.sundayButton, 6, weeksActive);

                //Managing the hour for the treatment
                HoursListViewAdapter hoursAdapterWeekly = new HoursListViewAdapter(getContext(), plan.getPlanHours(), getParentFragmentManager());
                NoScrollListView weeklyHours = weeklyView.findViewById(R.id.hoursListWeekly);
                weeklyHours.setAdapter(hoursAdapterWeekly);

                // Button for adding more hours
                Button addHours = new Button(getContext());
                addHours.setText(R.string.add);
                addHours.setOnClickListener(e -> addHours(hoursAdapterWeekly));
                weeklyHours.addFooterView(addHours);

                if (plan.getPlanHours().isEmpty() && isNew)
                    hoursAdapterWeekly.add(new PlanHours(DateTime.now().getHourOfDay(), DateTime.now().getMinuteOfHour()));

                return weeklyView;
            }

            private @NonNull NoScrollListView setUpSpecificDateView() {
                // Hiding unnecessary items
                planBinding.StartDateTextView.setVisibility(View.INVISIBLE);
                planBinding.startDate.setVisibility(View.INVISIBLE);
                planBinding.calendarDialogButton.setVisibility(View.INVISIBLE);

                NoScrollListView specificDatesListView = new NoScrollListView(getContext());

                SpecificDateViewAdapter specificDateViewAdapter = new SpecificDateViewAdapter(getContext(), plan.getSpecificDates(), DATE_FORMAT);
                specificDatesListView.setAdapter(specificDateViewAdapter);

                if (plan.getSpecificDates().isEmpty()) {
                    LocalDate now = LocalDate.now();
                    specificDateViewAdapter.add(String.format(Locale.getDefault(), DATE_FORMAT, now.getDayOfMonth(), now.getMonthOfYear(), now.getYear()));
                }

                Button addDateButton = new Button(getContext());
                addDateButton.setText(R.string.add);
                addDateButton.setOnClickListener(e -> addDate(specificDateViewAdapter));

                specificDatesListView.addFooterView(addDateButton);
                return specificDatesListView;
            }

            private void addDate(SpecificDateViewAdapter specificDateViewAdapter) {
                LocalDate today = LocalDate.now();
                int year = today.getYear();
                int month = today.getMonthOfYear();
                int day = today.getDayOfMonth();

                specificDateViewAdapter.add(String.format(DATE_FORMAT, day, month, year));

            }

            private void addHours(HoursListViewAdapter hoursAdapter) {
                hoursAdapter.add(new PlanHours(DateTime.now().getHourOfDay(), DateTime.now().getMinuteOfHour()));
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

            public void setUpWeekButtons(RelativeLayout weeklyView, int buttonID, int dayOfWeek, ArrayList<Boolean> weeksActive) {
                TextView b = weeklyView.findViewById(buttonID);
                b.setActivated(false);
                b.setOnClickListener(e -> changeWeekButtonSate(weeksActive, dayOfWeek, b));

            }
        });

        // Treatment Form Spinner
        SpinnerAdapter formSpinAdapter = new SpinnerAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.form_type_array), true);
        formSpinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        planBinding.formTypeSpinner.setAdapter(formSpinAdapter);

        planBinding.formTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 2:
                        planBinding.quantityEditText.setVisibility(View.VISIBLE);
                        planBinding.quantityTextView.setVisibility(View.VISIBLE);
                        planBinding.quantityMetricSpinner.setVisibility(View.VISIBLE);

                        SpinnerAdapter quantityLiquidAdapter = new SpinnerAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.quantity_metric_array_liquid), false);
                        planBinding.quantityMetricSpinner.setAdapter(quantityLiquidAdapter);
                        break;
                    case 5:
                        planBinding.quantityEditText.setVisibility(View.VISIBLE);
                        planBinding.quantityTextView.setVisibility(View.VISIBLE);
                        planBinding.quantityMetricSpinner.setVisibility(View.VISIBLE);
                        SpinnerAdapter quantityInjectionAdapter = new SpinnerAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.quantity_metric_array_injection), false);
                        planBinding.quantityMetricSpinner.setAdapter(quantityInjectionAdapter);
                        break;
                    case 6:
                        planBinding.quantityEditText.setVisibility(View.INVISIBLE);
                        planBinding.quantityTextView.setVisibility(View.INVISIBLE);
                        planBinding.quantityMetricSpinner.setVisibility(View.INVISIBLE);
                        break;
                    default:
                        planBinding.quantityMetricSpinner.setVisibility(View.INVISIBLE);
                        planBinding.quantityEditText.setVisibility(View.VISIBLE);
                        planBinding.quantityTextView.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Save Button
        planBinding.savePlanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Checking important data is introduced
                // TODO check for frequency
                if (planBinding.nameTextInput.getText() == null || planBinding.nameTextInput.getText().length() < 3) {
                    Toast.makeText(getContext(), R.string.fillOutName, Toast.LENGTH_SHORT).show();
                } else if (planBinding.freqTypeSpinner.getSelectedItemPosition() == 0) {
                    Toast.makeText(getContext(), R.string.selectFreq, Toast.LENGTH_SHORT).show();
                } else if (planBinding.formTypeSpinner.getSelectedItemPosition() == 0) {
                    Toast.makeText(getContext(), R.string.selectForm, Toast.LENGTH_SHORT).show();
                } else {
                    savePlan();
                    getParentFragmentManager().beginTransaction().remove(PlanFragment.this).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).commit();
                }
            }
        });

        // Cancel button because of reasons anyways....
        planBinding.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCanceled = true;
                getParentFragmentManager().beginTransaction().remove(PlanFragment.this).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).commit();
            }
        });

        if (!isNew) loadPlanInfo(plan);
    }

    // Negating the boolean representing the active state of the button and assigning that state to the button so that it changes bg dynamically
    private void changeWeekButtonSate(ArrayList<Boolean> daysActive, int dayOfWeek, TextView weekdayButton) {
        daysActive.set(dayOfWeek, !daysActive.get(dayOfWeek));
        weekdayButton.setActivated(daysActive.get(dayOfWeek));
    }

    // uhhhhh size??
    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((WindowManager.LayoutParams) params);
    }

    private void savePlan() {
        // Setting the new plan to active by default
        if (isNew) plan.setActive(true);
        // Saving name without checking because the check occurs earlier
        plan.setName(planBinding.nameTextInput.getText().toString());

        // Catching nullpointer exception that results from trying to get empty text from an EditText
        try {
            plan.setDetails(planBinding.detailsTextInput.getText().toString());
        } catch (NullPointerException e) {
            plan.setDetails("");
        }

        plan.setFrequencyMetric(planBinding.freqTypeSpinner.getSelectedItemPosition());

        // Saving star date if it's needed
        if (planBinding.startDate.getVisibility() == View.VISIBLE) {
            /*
             * TODO Magane different formats
             * */
            String startDateText = planBinding.startDate.getText().toString();
            String[] starDateArray = startDateText.split("/");
            int year = Integer.parseInt(starDateArray[2]);
            int month = Integer.parseInt(starDateArray[1]);
            int day = Integer.parseInt(starDateArray[0]);
            plan.setStartDate(new LocalDate(year, month, day));
        }

        plan.setForm(planBinding.formTypeSpinner.getSelectedItemPosition());

        if (plan.getForm() == TreatmentForm.LIQUID || plan.getForm() == TreatmentForm.INJECTION) {
            plan.setQuantity(Float.parseFloat(planBinding.quantityEditText.getText().toString()));
            plan.setQuantityMetric(planBinding.quantityMetricSpinner.getSelectedItem().toString());
        } else if (plan.getForm() != TreatmentForm.CREAM) {
            plan.setQuantity(Float.parseFloat(planBinding.quantityEditText.getText().toString()));
        }

        // Notifying the Listview of changes
        if (!isNew) {
            TreatmentsArrayAdapter adapter = (TreatmentsArrayAdapter) planList.getAdapter();
            adapter.notifyDataSetChanged();
        }

    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    private void loadPlanInfo(Plan plan) {
        planBinding.nameTextInput.setText(plan.getName());
        if (!plan.getDetails().isEmpty()) planBinding.detailsTextInput.setText(plan.getDetails());

        switch (plan.getFrequencyMetric()) {
            case HOURLY:
                planBinding.freqTypeSpinner.setSelection(1);
                break;
            case DAILY:
                planBinding.freqTypeSpinner.setSelection(2);
                break;
            case WEEKLY:
                planBinding.freqTypeSpinner.setSelection(3);
                break;
            case MONTHLY:
                planBinding.freqTypeSpinner.setSelection(4);
                break;
            case YEARLY:
                planBinding.freqTypeSpinner.setSelection(5);
                break;
            case ASNEEDED:
                planBinding.freqTypeSpinner.setSelection(6);
                break;
            case SPECIFFICDATES:
                planBinding.freqTypeSpinner.setSelection(7);
                break;
            default:
                planBinding.freqTypeSpinner.setSelection(0);
                break;
        }

        LocalDate startDate = plan.getStartDate();
        planBinding.startDate.setText(String.format(Locale.getDefault(), DATE_FORMAT, startDate.dayOfMonth().get(), startDate.monthOfYear().get(), startDate.year().get()));

        switch (plan.getForm()) {
            case PILLS:
                planBinding.formTypeSpinner.setSelection(1);
                break;
            case LIQUID:
                planBinding.formTypeSpinner.setSelection(2);
                break;
            case DROPS:
                planBinding.formTypeSpinner.setSelection(3);
                break;
            case INHALER:
                planBinding.formTypeSpinner.setSelection(4);
                break;
            case INJECTION:
                planBinding.formTypeSpinner.setSelection(5);
                break;
            case CREAM:
                planBinding.formTypeSpinner.setSelection(6);
                break;
            case PATCHES:
                planBinding.formTypeSpinner.setSelection(7);
                break;
            case PACKET:
                planBinding.formTypeSpinner.setSelection(8);
                break;
        }

        DecimalFormat format = new DecimalFormat("#.##");
        planBinding.quantityEditText.setText(format.format(plan.getQuantity()));
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public boolean isNew() {
        return isNew;
    }

    public Plan getPlan() {
        return plan;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (closeListener != null)
            closeListener.OnClose(isCanceled);
    }
}

