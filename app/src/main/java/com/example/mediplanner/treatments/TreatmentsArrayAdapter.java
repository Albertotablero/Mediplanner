package com.example.mediplanner.treatments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mediplanner.R;
import com.example.mediplanner.plan.Plan;

import java.util.ArrayList;

public class TreatmentsArrayAdapter extends ArrayAdapter<Plan> {

    private final ArrayList<Plan> plans;

    public TreatmentsArrayAdapter(@NonNull Context context, ArrayList<Plan> planList) {
        super(context, 0, planList);

        plans = planList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.treatment_list_card, parent, false);

        TextView name = convertView.findViewById(R.id.nameTreatmentsTextView);
        String name1 = getItem(position).getName();
        if (name1 == null) name1 = "";
        name.setText(name1);

        TextView details = convertView.findViewById(R.id.descriptionTreatments);

        String details1 = getItem(position).getDetails();
        if (details1 == null) details1 = "";
        details.setText(details1);


        return convertView;
    }

    @Override
    public void add(@Nullable Plan object) {
        super.add(object);
    }
}
