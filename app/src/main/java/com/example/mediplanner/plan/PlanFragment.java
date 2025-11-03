package com.example.mediplanner.plan;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.mediplanner.R;
import com.example.mediplanner.databinding.PlanFragmentViewBinding;
import com.example.mediplanner.date.PlanHours;
import com.example.mediplanner.treatmentPlansTab.TreatmentsArrayAdapter;
import com.example.mediplanner.utils.CloseListener;
import com.example.mediplanner.utils.MediPConstants;
import com.example.mediplanner.utils.SpinnerAdapter;
import com.example.mediplanner.utils.TreatmentForm;

import org.joda.time.LocalDate;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Locale;

public class PlanFragment extends DialogFragment {

    public static final String DATE_FORMAT = MediPConstants.DATE_FORMAT;
    private PlanFragmentViewBinding planBinding;
    private TreatmentPlan treatmentPlan;
    private DatePickerDialog datePickerDialog;
    private ListView planList;

    // Check if creating plan or editing existing
    private boolean isNew;
    private boolean isCanceled = false;

    private CloseListener closeListener;

    public PlanFragment(CloseListener closeListener) {
        super();
        isNew = true;
        treatmentPlan = new TreatmentPlan();
        this.closeListener = closeListener;
    }

    public PlanFragment(TreatmentPlan treatmentPlan, ListView treatmentPlansListView) {
        super();
        isNew = false;
        this.treatmentPlan = treatmentPlan;
        planList = treatmentPlansListView;
    }

    public PlanFragment(TreatmentPlan treatmentPlan, ListView treatmentPlansListView, CloseListener closeListener) {
        super();
        this.isNew = false;
        this.treatmentPlan = treatmentPlan;
        this.planList = treatmentPlansListView;
        this.closeListener = closeListener;
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

        planBinding.detailsTextInput.setHint(getResources().getString(R.string.treatment_details_prompt));

        // Setting up a date picker cause why not I fucking guess
        if (isNew) {
            LocalDate startDate = LocalDate.now();
            treatmentPlan.setStartDate(startDate);
            planBinding.startDateInput.setText(String.format(Locale.getDefault(), DATE_FORMAT, startDate.dayOfMonth().get(), startDate.monthOfYear().get(), startDate.year().get()));

        }

        planBinding.startDateCalendarDialogB.setOnClickListener(new View.OnClickListener() {
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
                        planBinding.startDateInput.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }

                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        planBinding.endDCalendarDialogB.setOnClickListener(new View.OnClickListener() {
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
                        planBinding.endDateTextView.setText(String.format(Locale.getDefault(), MediPConstants.DATE_FORMAT, dayOfMonth, month, year));
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
                    if (planBinding.startDateInput.getVisibility() != View.VISIBLE) {
                        planBinding.startDateInput.setVisibility(View.VISIBLE);
                        planBinding.StartDateTextView.setVisibility(View.VISIBLE);
                        planBinding.startDateCalendarDialogB.setVisibility(View.VISIBLE);
                    }
                }

                View child = getFrequencyView(treatmentPlan, position);

                if (child != null) planBinding.frequencyBox.addView(child);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        // Permanent or end Date
        planBinding.isPermanentcheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int visibility = isChecked ? View.GONE : View.VISIBLE;
                planBinding.endDateTextView.setVisibility(visibility);
                planBinding.endDCalendarDialogB.setVisibility(visibility);
                planBinding.endDateInput.setVisibility(visibility);
                treatmentPlan.setPermanent(isChecked);
            }
        });
        if (isNew) planBinding.isPermanentcheckBox.setChecked(true);

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
                    //if (closeListener != null) closeListener.OnClose(isCanceled);
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

        if (!isNew) loadPlanInfo(treatmentPlan);
    }

    public View getFrequencyView(TreatmentPlan treatmentPlan, int frequency) {
        View view = null;
        switch (frequency) {
            // EVERY X HOURS
            case 1:
                view = FrequencyViewGenerator.setUpHoursIntervalEditView(getLayoutInflater(), getContext(), treatmentPlan);
                break;
            // DAILY
            case 2:
                view = FrequencyViewGenerator.setUpDailyEditView(getLayoutInflater(), getContext(), treatmentPlan, isNew, getParentFragmentManager());
                break;
            // WEEKLY
            case 3:
                view = FrequencyViewGenerator.setUpWeeklyEditView(getLayoutInflater(), getContext(), treatmentPlan, isNew, getParentFragmentManager());
                break;
            // SPECIFIC DATES
            case 6:
                planBinding.StartDateTextView.setVisibility(View.INVISIBLE);
                planBinding.startDateInput.setVisibility(View.INVISIBLE);
                planBinding.startDateCalendarDialogB.setVisibility(View.INVISIBLE);
                view = FrequencyViewGenerator.setUpSpecificDateEditView(getContext(), treatmentPlan);
                break;
            default:
                break;

        }
        return view;

    }


    // Screen Size
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
        if (isNew) treatmentPlan.setActive(true);
        // Saving name without checking because the check occurs earlier
        treatmentPlan.setName(planBinding.nameTextInput.getText().toString());

        // Catching nullPointer exception that results from trying to get empty text from an EditText
        try {
            treatmentPlan.setDetails(planBinding.detailsTextInput.getText().toString());
        } catch (NullPointerException e) {
            treatmentPlan.setDetails("");
        }

        treatmentPlan.setFrequencyMetric(planBinding.freqTypeSpinner.getSelectedItemPosition());
        switch (treatmentPlan.getFrequencyMetric()) {
            case HOURLY:
                treatmentPlan.setHourIntervalFrequency(Integer.parseInt(((EditText) planBinding.frequencyBox.findViewById(R.id.hourIntervalEditText)).getText().toString()));
                treatmentPlan.setInitialHour(new PlanHours(((NumberPicker) planBinding.frequencyBox.findViewById(R.id.hourPicker)).getValue(),
                        ((NumberPicker) planBinding.frequencyBox.findViewById(R.id.minutePicker)).getValue()));
                break;
            case DAILY:

                break;
        }

        // Saving star date if it's needed
        if (planBinding.startDateInput.getVisibility() == View.VISIBLE) {
            /*
             * TODO Magane different formats
             * */
            String startDateText = planBinding.startDateInput.getText().toString();
            String[] starDateArray = startDateText.split("/");
            int year = Integer.parseInt(starDateArray[2]);
            int month = Integer.parseInt(starDateArray[1]);
            int day = Integer.parseInt(starDateArray[0]);
            treatmentPlan.setStartDate(new LocalDate(year, month, day));
        }

        treatmentPlan.setForm(planBinding.formTypeSpinner.getSelectedItemPosition());

        if (treatmentPlan.getForm() == TreatmentForm.LIQUID || treatmentPlan.getForm() == TreatmentForm.INJECTION) {
            treatmentPlan.setQuantity(Float.parseFloat(planBinding.quantityEditText.getText().toString()));
            treatmentPlan.setQuantityMetric(planBinding.quantityMetricSpinner.getSelectedItem().toString());
        } else if (treatmentPlan.getForm() != TreatmentForm.CREAM) {
            treatmentPlan.setQuantity(Float.parseFloat(planBinding.quantityEditText.getText().toString()));
        }

        // Notifying the Listview of changes
        if (!isNew) {
            TreatmentsArrayAdapter adapter = (TreatmentsArrayAdapter) planList.getAdapter();
            adapter.notifyDataSetChanged();
        }

    }

    public void setPlan(TreatmentPlan treatmentPlan) {
        this.treatmentPlan = treatmentPlan;
    }

    private void loadPlanInfo(TreatmentPlan treatmentPlan) {
        planBinding.nameTextInput.setText(treatmentPlan.getName());
        if (!treatmentPlan.getDetails().isEmpty())
            planBinding.detailsTextInput.setText(treatmentPlan.getDetails());

        switch (treatmentPlan.getFrequencyMetric()) {
            case HOURLY:
                // On Hourly frequency it loads the interval and the initial hour
                planBinding.freqTypeSpinner.setSelection(1);
                EditText hourInterval = (EditText) planBinding.frequencyBox.getChildAt(0);
                hourInterval.setText(String.format(Locale.getDefault(), "%02d", treatmentPlan.getHourIntervalFrequency()));

                NumberPicker hourPicker = planBinding.frequencyBox.findViewById(R.id.hourPicker);
                NumberPicker minutePicker = planBinding.frequencyBox.findViewById(R.id.minutePicker);
                hourPicker.setValue(treatmentPlan.getInitialHour().hours.getHours());
                minutePicker.setValue(treatmentPlan.getInitialHour().minutes.getMinutes());

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

        LocalDate startDate = treatmentPlan.getStartDate();
        planBinding.startDateInput.setText(String.format(Locale.getDefault(), DATE_FORMAT, startDate.dayOfMonth().get(), startDate.monthOfYear().get(), startDate.year().get()));

        switch (treatmentPlan.getForm()) {
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
        planBinding.quantityEditText.setText(format.format(treatmentPlan.getQuantity()));
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public boolean isNew() {
        return isNew;
    }

    public TreatmentPlan getPlan() {
        return treatmentPlan;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (closeListener != null)
            closeListener.OnClose(isCanceled);
    }
}

