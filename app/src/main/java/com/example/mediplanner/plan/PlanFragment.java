package com.example.mediplanner.plan;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.mediplanner.R;
import com.example.mediplanner.databinding.PlanFragmentViewBinding;
import com.example.mediplanner.utils.HoursListViewAdapter;
import com.example.mediplanner.utils.SpecificDateViewAdapter;
import com.example.mediplanner.utils.SpinnerAdapter;
import com.example.mediplanner.utils.TreatmentForm;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Calendar;

public class PlanFragment extends DialogFragment {

    private PlanFragmentViewBinding planBidnding;
    private Plan plan;
    private DatePickerDialog datePickerDialog;

    private boolean isNew;


    public PlanFragment() {
        super();
        isNew = true;
        plan = new Plan();
    }

    public PlanFragment(Plan editPlan) {
        super();
        isNew = false;
        plan = editPlan;
    }

    //Setting up view with binging
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        planBidnding = PlanFragmentViewBinding.inflate(inflater, container, false);
        return planBidnding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        planBidnding.fragmentContainerView.setVisibility(View.VISIBLE);
        planBidnding.detailsTextInput.setHint(getResources().getString(R.string.treatment_details_prompt));

        // Setting up a date picker cause why not I fucking guess
        planBidnding.startDate.setText(Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "/" + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "/" + Calendar.getInstance().get(Calendar.YEAR));
        planBidnding.calendarDialogButton.setOnClickListener(new View.OnClickListener() {
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
                        planBidnding.startDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }

                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        // Frequency Spinner
        SpinnerAdapter freqSpinAdapter = new SpinnerAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.freq_type_array), true);
        freqSpinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        planBidnding.freqTypeSpinner.setAdapter(freqSpinAdapter);

        // Change view based on selected frequency typ
        planBidnding.freqTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Removing the last child (Pending check before deleting and inserting the same view)
                if (planBidnding.frequencyBox.getChildCount() > 1) {
                    planBidnding.frequencyBox.removeViewAt(1);

                    // resetting visibility of start date after changing from specific dates
                    if (planBidnding.startDate.getVisibility() != View.VISIBLE) {
                        planBidnding.startDate.setVisibility(View.VISIBLE);
                        planBidnding.StartDateTextView.setVisibility(View.VISIBLE);
                        planBidnding.calendarDialogButton.setVisibility(View.VISIBLE);
                    }
                }

                View child = null;
                switch (position) {
                    // EVERY X HOURS
                    case 1:
                        EditText hoursFreq = new EditText(getContext());
                        hoursFreq.setHint(R.string.dailyFreqHintText);
                        hoursFreq.setInputType(InputType.TYPE_CLASS_DATETIME);

                        child = hoursFreq;
                        break;
                    //   DAILY
                    case 2:
                        //Create list view to add or remove hours for daily treatments
                        ListView dailyHoursView = new ListView(getContext());

                        //Setting up adapter for list view
                        HoursListViewAdapter hoursAdapter = new HoursListViewAdapter(getContext(), plan.getDailyHours());
                        dailyHoursView.setAdapter(hoursAdapter);

                        //for adding another hour to the listview
                        Button addHours = new Button(getContext());
                        addHours.setText(R.string.add);
                        addHours.setOnClickListener(e -> addHours(hoursAdapter));

                        dailyHoursView.addFooterView(addHours);

                        child = dailyHoursView;
                        break;
                    // WEEKLY
                    case 3:
                        // Inflating a week day piker for the weekly frequency type
                        RelativeLayout weeklyView = (RelativeLayout) LayoutInflater.from(getContext()).inflate(R.layout.week_day_picker_widget, planBidnding.frequencyBox, false);
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
                        setUpWeekButtons(weeklyView, R.id.mondayButton, 6, weeksActive);

                        //Managing the hour for the treatment
                        ArrayList<String> hour = new ArrayList<>();
                        ArrayList<String> minute = new ArrayList<>();
                        HoursListViewAdapter.fillHoursAndMinutes(hour, minute);

                        Spinner hoursSpinner = weeklyView.findViewById(R.id.spinnerHours);
                        String[] hoursArray = new String[hour.size()];
                        SpinnerAdapter hourSpinAdpt = new SpinnerAdapter(getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, hour.toArray(hoursArray), false);
                        hoursSpinner.setAdapter(hourSpinAdpt);

                        Spinner minutesSpinner = weeklyView.findViewById(R.id.spinnerMinutes);
                        String[] minutesArray = new String[hour.size()];
                        SpinnerAdapter minutesSpinAdpt = new SpinnerAdapter(getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, hour.toArray(minutesArray), false);
                        minutesSpinner.setAdapter(minutesSpinAdpt);

                        child = weeklyView;
                        break;
                    case 6:
                        // Hiding star date
                        planBidnding.StartDateTextView.setVisibility(View.INVISIBLE);
                        planBidnding.startDate.setVisibility(View.INVISIBLE);
                        planBidnding.calendarDialogButton.setVisibility(View.INVISIBLE);
                        ListView specificDatesListView = new ListView(getContext());

                        String dateFormat = "%1$02d/%2$02d/%3$4d";
                        SpecificDateViewAdapter specificDateViewAdapter = new SpecificDateViewAdapter(getContext(), plan.getSpecificDates(), dateFormat);
                        specificDatesListView.setAdapter(specificDateViewAdapter);

                        Button addDateButton = new Button(getContext());
                        addDateButton.setText(R.string.add);
                        addDateButton.setOnClickListener(e -> addDate(specificDateViewAdapter, dateFormat));

                        specificDatesListView.addFooterView(addDateButton);

                        child = specificDatesListView;
                        break;
                    default:
                        break;

                }
                if (child != null) planBidnding.frequencyBox.addView(child);
            }

            private void addDate(SpecificDateViewAdapter specificDateViewAdapter, String dateFormat) {
                LocalDate today = LocalDate.now();
                int year = today.getYear();
                int month = today.getMonthOfYear();
                int day = today.getDayOfMonth();

                specificDateViewAdapter.add(String.format(dateFormat, day, month, year));

            }

            private void addHours(HoursListViewAdapter hoursAdapter) {
                hoursAdapter.add(DateTime.now().getHourOfDay() + ":" + DateTime.now().getMinuteOfHour());
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

            public void setUpWeekButtons(RelativeLayout weeklyView, int buttonID, int i, ArrayList<Boolean> weeksActive) {
                TextView b = weeklyView.findViewById(buttonID);
                b.setBackgroundColor(getResources().getColor(R.color.weekButtonOff, getActivity().getTheme()));
                b.setOnClickListener(e -> changeWeekButtonSate(weeksActive, 0, b));

            }
        });

        // Treatment Form Spinner
        SpinnerAdapter formSpinAdapter = new SpinnerAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.form_type_array), true);
        formSpinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        planBidnding.formTypeSpinner.setAdapter(formSpinAdapter);

        planBidnding.formTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 2:
                        planBidnding.quantityEditText.setVisibility(View.VISIBLE);
                        planBidnding.quantityTextView.setVisibility(View.VISIBLE);
                        planBidnding.quantityMetricSpinner.setVisibility(View.VISIBLE);

                        SpinnerAdapter quantityLiquidAdapter = new SpinnerAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.quantity_metric_array_liquid), false);
                        planBidnding.quantityMetricSpinner.setAdapter(quantityLiquidAdapter);
                        break;
                    case 5:
                        planBidnding.quantityEditText.setVisibility(View.VISIBLE);
                        planBidnding.quantityTextView.setVisibility(View.VISIBLE);
                        planBidnding.quantityMetricSpinner.setVisibility(View.VISIBLE);
                        SpinnerAdapter quantityInjectionAdapter = new SpinnerAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.quantity_metric_array_injection), false);
                        planBidnding.quantityMetricSpinner.setAdapter(quantityInjectionAdapter);
                        break;
                    case 6:
                        planBidnding.quantityEditText.setVisibility(View.INVISIBLE);
                        planBidnding.quantityTextView.setVisibility(View.INVISIBLE);
                        planBidnding.quantityMetricSpinner.setVisibility(View.INVISIBLE);
                        break;
                    default:

                        planBidnding.quantityMetricSpinner.setVisibility(View.INVISIBLE);
                        planBidnding.quantityEditText.setVisibility(View.VISIBLE);
                        planBidnding.quantityTextView.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Save Button
        planBidnding.savePlanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Checking importa data is introduced
                // TODO check for frequency
                if (planBidnding.nameTextInput.getText() == null || planBidnding.nameTextInput.getText().length() < 3) {
                    Toast.makeText(getContext(), R.string.fillOutName, Toast.LENGTH_SHORT).show();
                } else if (planBidnding.freqTypeSpinner.getSelectedItemPosition() == 0) {
                    Toast.makeText(getContext(), R.string.selectFreq, Toast.LENGTH_SHORT).show();
                } else if (planBidnding.formTypeSpinner.getSelectedItemPosition() == 0) {
                    Toast.makeText(getContext(), R.string.selectForm, Toast.LENGTH_SHORT).show();
                } else {
                    savePlan();
                    getParentFragmentManager().beginTransaction().remove(PlanFragment.this).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).commit();
                }
            }
        });
    }

    private void changeWeekButtonSate(ArrayList<Boolean> daysActive, int dayOfWeek, TextView weekdayButton) {
        daysActive.set(dayOfWeek, !daysActive.get(dayOfWeek));


        if (daysActive.get(dayOfWeek)) {
            weekdayButton.setBackgroundColor(getResources().getColor(R.color.weekButtonOn, getActivity().getTheme()));

        } else {
            weekdayButton.setBackgroundColor(getResources().getColor(R.color.weekButtonOff, getActivity().getTheme()));
        }
    }

    //uhhhhh size??
    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((WindowManager.LayoutParams) params);


    }

    private void savePlan() {
        plan.setActive(true);

        plan.setName(planBidnding.nameTextInput.getText().toString());
        try {
            plan.setDetails(planBidnding.detailsTextInput.getText().toString());
        } catch (NullPointerException e) {
            plan.setDetails("");
        }
        plan.setFrequencyMetric(planBidnding.freqTypeSpinner.getSelectedItemPosition());

        plan.setStartDate(LocalDate.parse(planBidnding.startDate.getText().toString()));
        plan.setForm(planBidnding.formTypeSpinner.getSelectedItemPosition());

        if (plan.getForm() == TreatmentForm.LIQUID || plan.getForm() == TreatmentForm.INJECTION) {
            plan.setQuantity(Float.parseFloat(planBidnding.quantityEditText.getText().toString()));
            plan.setQuantityMetric(planBidnding.quantityMetricSpinner.getSelectedItem().toString());
        } else if (plan.getForm() != TreatmentForm.CREAM) {
            plan.setQuantity(Float.parseFloat(planBidnding.quantityEditText.getText().toString()));
        }


    }


    public void setNew(boolean aNew) {
        isNew = aNew;
    }
}
