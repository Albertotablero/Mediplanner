package com.example.mediplanner.treatments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mediplanner.R;
import com.example.mediplanner.plan.Plan;

import java.util.ArrayList;

public class TreatmentsRecyclerViewAdapter extends ArrayAdapter<Plan> {

    private final ArrayList<Plan> plans;

    public TreatmentsRecyclerViewAdapter(@NonNull Context context, ArrayList<Plan> planList) {
        super(context, 0);

        plans = planList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Toast.makeText(getContext(), "buh", Toast.LENGTH_SHORT).show();
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.treatment_list_card, parent, false);
        TextView name = convertView.findViewById(R.id.nameTreatmentsTextView);
        name.setText(plans.get(position).getName());
        Toast.makeText(getContext(), plans.get(position).getName(), Toast.LENGTH_SHORT).show();
        TextView details = convertView.findViewById(R.id.detailsTextView);
        name.setText(plans.get(position).getDetails());

        return convertView;
    }
}
