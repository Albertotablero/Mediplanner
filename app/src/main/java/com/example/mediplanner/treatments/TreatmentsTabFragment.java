package com.example.mediplanner.treatments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.mediplanner.R;
import com.example.mediplanner.databinding.FragmentTreatmentsTabBinding;
import com.example.mediplanner.databinding.TreatmentListCardBinding;
import com.example.mediplanner.plan.Plan;
import com.example.mediplanner.utils.FrequencyMetric;

import org.joda.time.LocalDate;

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
        binding = FragmentTreatmentsTabBinding.inflate(getLayoutInflater());


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // TODO: Remove this test
        treatmentPlans = new ArrayList<>();


        TreatmentsRecyclerViewAdapter adapter = new TreatmentsRecyclerViewAdapter(getContext(), treatmentPlans);
        binding.treatmentPlansListView.setAdapter(adapter);
        adapter.add(new Plan("Hola que hace", "Pues esto no tiene mucho detalle sorry", true, 1f, 2, LocalDate.now(), false, 2f, FrequencyMetric.ASNEEDED));
        
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_treatments_tab, container, false);
    }
}