package com.example.mediplanner.treatments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.mediplanner.MainActivity;
import com.example.mediplanner.R;
import com.example.mediplanner.databinding.FragmentTreatmentsTabBinding;
import com.example.mediplanner.plan.Plan;
import com.example.mediplanner.plan.PlanFragment;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TreatmentsTabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TreatmentsTabFragment extends Fragment {

    private FragmentTreatmentsTabBinding binding;
    private ArrayList<Plan> treatmentPlans;
    private int selectedListItem = -1;
    private TreatmentsArrayAdapter treatmentListVAdapter;

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

        treatmentListVAdapter = new TreatmentsArrayAdapter(getContext(), treatmentPlans);

        binding.treatmentPlansListView.setAdapter(treatmentListVAdapter);
        binding.treatmentPlansListView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

        // Storing the position of the selected item for reasons
        binding.treatmentPlansListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (selectedListItem != position) {
                    // All this just to change the text color of individual elements when the card is selected or not
                    if (selectedListItem != -1) {
                        View previeous = binding.treatmentPlansListView.getChildAt(selectedListItem);
                        previeous.findViewById(R.id.nameTreatmentsTextView).setActivated(false);
                        previeous.findViewById(R.id.descriptionTreatments).setActivated(false);
                        previeous.findViewById(R.id.quantityTextView).setActivated(false);
                        previeous.findViewById(R.id.quantityMetricTextView).setActivated(false);

                    }
                    selectedListItem = position;

                    View current = binding.treatmentPlansListView.getChildAt(position);
                    current.findViewById(R.id.nameTreatmentsTextView).setActivated(true);
                    current.findViewById(R.id.descriptionTreatments).setActivated(true);
                    current.findViewById(R.id.quantityTextView).setActivated(true);
                    current.findViewById(R.id.quantityMetricTextView).setActivated(true);

                }

            }
        });

        // Adding functionality to the Edit Button
        binding.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedListItem == -1) {
                    Toast.makeText(getContext(), R.string.no_item_selected_warning, Toast.LENGTH_SHORT).show();
                } else {
                    Plan selectedplan = MainActivity.getTreatmentPlanList().get(selectedListItem);
                    //Plan creator // editor fragment
                    /*(PlanFragment) getParentFragmentManager().getFragmentFactory().instantiate(ClassLoader.getSystemClassLoader(), "com.example.mediplanner.plan.PlanFragment");*/
                    PlanFragment planFragment = new PlanFragment(selectedplan, binding.treatmentPlansListView);

                    //Show Fragment
                    FragmentTransaction planF = getParentFragmentManager().beginTransaction().add(planFragment, "planF").setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).setReorderingAllowed(true).show(planFragment);
                    planF.commit();


                }
            }
        });

        // Adding functionality to the Delete Button
        binding.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedListItem != -1) {
                    new AlertDialog.Builder(getContext()).setTitle(R.string.removeTreatmentWarningTitle).setMessage(R.string.removeTreatmentPrompt).setIcon(android.R.drawable.ic_dialog_alert).setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            treatmentPlans.remove(selectedListItem);
                            treatmentListVAdapter.notifyDataSetChanged();
                            selectedListItem = -1;
                        }
                    }).setNegativeButton(android.R.string.no, null).show();
                } else {
                    Toast.makeText(getContext(), R.string.no_item_selected_warning, Toast.LENGTH_SHORT).show();
                }
            }
        });
        Log.d("FragmentTag", "onViewCreated: " + getTag());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTreatmentsTabBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    public FragmentTreatmentsTabBinding getBinding() {
        return binding;
    }
}