package com.example.mediplanner.treatments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mediplanner.MainActivity;
import com.example.mediplanner.databinding.FragmentTreatmentsTabBinding;
import com.example.mediplanner.plan.Plan;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TreatmentsTabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TreatmentsTabFragment extends Fragment {

    private FragmentTreatmentsTabBinding binding;
    private ArrayList<Plan> treatmentPlans;

    public TreatmentsTabFragment() {
        // Required empty public constructor
    }

    public static TreatmentsTabFragment newInstance() {
        return new TreatmentsTabFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        treatmentPlans = MainActivity.getTreatmentPlanList();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // Adding the treatment lists and adapter to the listView

        TreatmentsArrayAdapter adapter = new TreatmentsArrayAdapter(getContext(), treatmentPlans);

        binding.treatmentPlansListView.setAdapter(adapter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTreatmentsTabBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }
}