package com.example.mediplanner.plan;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;

import com.example.mediplanner.R;
import com.example.mediplanner.databinding.PlanFragmentViewBinding;
import com.example.mediplanner.date.DatePicker;
import com.example.mediplanner.utils.SpinnerAdapter;

import java.util.Calendar;

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
                            public void onDateSet(android.widget.DatePicker dp, int year, int month, int dayOfMonth) {
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
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);


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
        //Just testing git tbh
    }


    public void setNew(boolean aNew) {
        isNew = aNew;
    }
}
