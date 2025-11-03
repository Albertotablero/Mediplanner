package com.example.mediplanner.treatmentPlansTab;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.mediplanner.MainActivity;
import com.example.mediplanner.R;
import com.example.mediplanner.databinding.FragmentTreatmentsTabBinding;
import com.example.mediplanner.plan.MorePlanInfoFragment;
import com.example.mediplanner.plan.PlanFragment;
import com.example.mediplanner.plan.TreatmentPlan;
import com.example.mediplanner.utils.MoreButtonListener;

import java.util.ArrayList;


public class TreatmentsTabFragment extends Fragment {

    private FragmentTreatmentsTabBinding binding;
    private ArrayList<TreatmentPlan> treatmentPlans;
    private int selectedListItem = -1;
    private TreatmentsArrayAdapter treatmentListVAdapter;

    public TreatmentsTabFragment() {
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

        treatmentListVAdapter = new TreatmentsArrayAdapter(getContext(), treatmentPlans, new MoreButtonListener() {
            @Override
            public void getMoreInfoFragment(TreatmentPlan treatmentPlan) {
                generateMoreInfoFragment(treatmentPlan);
            }
        });

        binding.treatmentPlansListView.setAdapter(treatmentListVAdapter);
        binding.treatmentPlansListView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

        // Storing the position of the selected item for reasons
        binding.treatmentPlansListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (selectedListItem != position) {
                    // All this just to change the text color of individual elements when the card is selected or not
                    if (selectedListItem != -1) {
                        View previous = binding.treatmentPlansListView.getChildAt(selectedListItem);
                        previous.findViewById(R.id.nameTreatmentsTextView).setActivated(false);
                        previous.findViewById(R.id.descriptionTreatments).setActivated(false);
                        previous.findViewById(R.id.quantityTextView).setActivated(false);
                        previous.findViewById(R.id.quantityMetricTextView).setActivated(false);

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
                    TreatmentPlan selectedPlan = MainActivity.getTreatmentPlanList().get(selectedListItem);
                    //Plan creator // editor fragment
                    PlanFragment planFragment = new PlanFragment(selectedPlan, binding.treatmentPlansListView);

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
                            binding.treatmentPlansListView.getChildAt(selectedListItem).setActivated(false);
                            selectedListItem = -1;
                        }
                    }).setNegativeButton(android.R.string.no, null).show();
                } else {
                    Toast.makeText(getContext(), R.string.no_item_selected_warning, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void generateMoreInfoFragment(TreatmentPlan treatmentPlan) {
        MorePlanInfoFragment moreInfoFragment = new MorePlanInfoFragment(treatmentPlan);
        getParentFragmentManager().beginTransaction().add(moreInfoFragment, "morePlanInfoF").setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).setReorderingAllowed(true).show(moreInfoFragment).commit();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTreatmentsTabBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    public FragmentTreatmentsTabBinding getBinding() {
        return binding;
    }
}