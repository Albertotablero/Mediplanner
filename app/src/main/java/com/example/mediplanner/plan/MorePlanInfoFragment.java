package com.example.mediplanner.plan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.mediplanner.MainActivity;
import com.example.mediplanner.R;
import com.example.mediplanner.databinding.FragmentMorePlanInfoBinding;
import com.example.mediplanner.utils.CloseListener;
import com.example.mediplanner.utils.FrequencyMetric;
import com.example.mediplanner.utils.MediPConstants;

import org.joda.time.LocalDate;

import java.text.DecimalFormat;
import java.util.Locale;


public class MorePlanInfoFragment extends DialogFragment {

    private TreatmentPlan treatmentPlan;
    private FragmentMorePlanInfoBinding binding;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        fillOutDisplayInfo();

        binding.editPlanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListView treatmentPlansListView = MainActivity.getTreatmentsTabFragment().getBinding().treatmentPlansListView;
                PlanFragment planFragment = new PlanFragment(treatmentPlan, treatmentPlansListView, new CloseListener() {
                    @Override
                    public void OnClose(boolean isCanceled) {
                        fillOutDisplayInfo();
                    }

                    @Override
                    public void OnClose(int month, int year) {

                    }
                });
                getParentFragmentManager().beginTransaction().add(planFragment, "PlanCreation").setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).show(planFragment).commit();
            }
        });

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    private void fillOutDisplayInfo() {
        //Setting the Name and Details Display Text
        binding.nameTextView.setText(treatmentPlan.getName());
        binding.detailsTextView.setText(treatmentPlan.getDetails());

        // Populating the container for Frequency information
        View child = getFrequencyView(treatmentPlan, treatmentPlan.getFrequencyMetric());
        if (child != null) binding.frequencyContainer.addView(child);

        // Setting the Start and End date Display Text
        binding.startDateDisplayText.setText(getDateString(treatmentPlan.getStartDate()));

        binding.endDateDisplayText.setText(treatmentPlan.isPermanent() ? getResources().getText(R.string.permanent) :
                getDateString(treatmentPlan.getEndDate()));

        // Form and Quantity
        String[] formTextArray = getResources().getStringArray(R.array.form_type_array);
        binding.formDisplayText.setText(formTextArray[treatmentPlan.getForm().getValue()]);

        // Setting the Quantity Display Text depending on the treatment form for the Treatment Plan
        String quantitydisplayString = "";
        DecimalFormat f = new DecimalFormat("####.##");
        String quantity = f.format(treatmentPlan.getQuantity());

        switch (treatmentPlan.getForm()) {
            case LIQUID:
            case INJECTION:
                quantitydisplayString = String.format(Locale.getDefault(), "%1$s - %2$s", quantity, treatmentPlan.getQuantityMetric());
                break;
            case CREAM:
                break;
            default:
                quantitydisplayString = String.format(Locale.getDefault(), "%s", quantity);
                break;
        }
        binding.quantityDisplayText.setText(quantitydisplayString);
    }

    private String getDateString(LocalDate date) {
        return String.format(Locale.getDefault(), MediPConstants.DATE_FORMAT, date.dayOfMonth().get(), date.monthOfYear().get(), date.year().get());
    }

    public View getFrequencyView(TreatmentPlan treatmentPlan, FrequencyMetric frequency) {
        View view = null;
        switch (frequency) {
            // EVERY X HOURS
            case HOURLY:
                view = FrequencyViewGenerator.setUpHoursIntervalDisplayView(getLayoutInflater(), getContext(), treatmentPlan);
                break;
            // DAILY
            case DAILY:
                view = FrequencyViewGenerator.setUpDailyDisplayView(getLayoutInflater(), getContext(), treatmentPlan, false, getResources().getStringArray(R.array.freq_type_array));
                break;
            // WEEKLY
            case WEEKLY:
                //view = FrequencyViewGenerator.setUpWeeklyDisplayView(getLayoutInflater(), getContext(), treatmentPlan);
                break;
            // SPECIFIC DATES
            case SPECIFFICDATES:
                //view = FrequencyViewGenerator.setUpSpecificDateDisplayView(getContext(), treatmentPlan);
                break;
            default:
                break;

        }
        return view;
    }

    public MorePlanInfoFragment(TreatmentPlan treatmentPlan) {
        this.treatmentPlan = treatmentPlan;
    }

    public MorePlanInfoFragment() {

    }

    public static MorePlanInfoFragment newInstance() {
        return new MorePlanInfoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMorePlanInfoBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes(params);
    }
}