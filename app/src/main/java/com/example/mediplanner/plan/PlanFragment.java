package com.example.mediplanner.plan;

import android.app.DatePickerDialog;
import android.icu.text.ListFormatter;
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
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.mediplanner.R;
import com.example.mediplanner.databinding.PlanFragmentViewBinding;
import com.example.mediplanner.utils.HoursListViewAdapter;
import com.example.mediplanner.utils.SpinnerAdapter;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PlanFragment extends DialogFragment {

    private PlanFragmentViewBinding planBidnding;
    private Plan plan;
    private DatePickerDialog datePickerDialog;

    private boolean isNew;


    public PlanFragment() {
        super();
        plan = new Plan();
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
                datePickerDialog = new DatePickerDialog(requireContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker dp, int year, int month, int dayOfMonth) {
                                planBidnding.startDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                            }

                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        // Frequency Spinner
        SpinnerAdapter freqSpinAdapter = new SpinnerAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.freq_type_array));
        freqSpinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        planBidnding.freqTypeSpinner.setAdapter(freqSpinAdapter);

        // Change view based on selected frequency typ
        planBidnding.freqTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Removing the last child (Pending check before deleting and inserting the same view)
                if (planBidnding.frequencyBox.getChildCount() > 1)
                    planBidnding.frequencyBox.removeViewAt(1);

                View child = null;
                switch (position) {
                    case 1:
                        EditText hoursFreq = new EditText(getContext());
                        hoursFreq.setHint(R.string.dailyFreqHintText);
                        hoursFreq.setInputType(InputType.TYPE_CLASS_DATETIME);

                        child = hoursFreq;
                        break;
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
                    default:
                        break;

                }
                if (child != null) planBidnding.frequencyBox.addView(child);
            }

            private void addHours(HoursListViewAdapter hoursAdapter) {
                hoursAdapter.add(DateTime.now().getHourOfDay() + ":" + DateTime.now().getMinuteOfHour());
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d("Meh", "onNothingSelected: ");
            }
        });

        // Treatment Form Spinner
        SpinnerAdapter formSpinAdapter = new SpinnerAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.form_type_array));
        formSpinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        planBidnding.formTypeSpinner.setAdapter(formSpinAdapter);


        //Save Button
        planBidnding.savePlanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // rellenar esta mierda
                getParentFragmentManager().beginTransaction().remove(PlanFragment.this).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).commit();
                savePlan(isNew);

            }
        });
    }

    //uhhhhh size and shit??
    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((WindowManager.LayoutParams) params);


    }

    private void savePlan(boolean isNew) {
        plan.setName(planBidnding.nameTextInput.getText().toString());
        try {
            plan.setDetails(planBidnding.detailsTextInput.getText().toString());
        } catch (NullPointerException e) {
            plan.setDetails("");
        }
        plan.setActive(true);
        //Frequency i don't even know yet
        plan.setFrequencyMetric(planBidnding.freqTypeSpinner.getSelectedItemPosition());
        //plan.setStartDate(planBidnding.startDate.get);
    }


    public void setNew(boolean aNew) {
        isNew = aNew;
    }
}
