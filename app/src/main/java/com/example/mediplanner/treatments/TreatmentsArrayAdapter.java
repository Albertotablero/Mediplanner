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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;

public class TreatmentsArrayAdapter extends ArrayAdapter<Plan> {

    private final ArrayList<Plan> plans;

    public TreatmentsArrayAdapter(@NonNull Context context, ArrayList<Plan> planList) {
        super(context, 0, planList);

        plans = planList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Setting up the cards for the treatments list view
        if (convertView == null)
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.treatment_list_card, parent, false);

        // Treatment Plan name
        TextView name = convertView.findViewById(R.id.nameTreatmentsTextView);
        String name1 = getItem(position).getName();
        if (name1 == null) name1 = "";
        name.setText(name1);
        name.setActivated(false);

        // Treatment Plan Description
        TextView details = convertView.findViewById(R.id.descriptionTreatments);

        String details1 = getItem(position).getDetails();
        if (details1 == null) details1 = "";
        details.setText(details1);
        details.setActivated(false);

        // Quantity
        TextView quantityTextView = convertView.findViewById(R.id.quantityTextView);
        float quantityNumber = getItem(position).getQuantity();
        DecimalFormat format = new DecimalFormat("0.##");
        String quantiyText = String.format(format.format(quantityNumber));
        quantityTextView.setText(quantiyText);

        TextView quantityMetricTextView = convertView.findViewById(R.id.quantityMetricTextView);
        String metric = getItem(position).getQuantityMetric();
        quantityMetricTextView.setText(metric);

        // More button
        TextView moreButton = convertView.findViewById(R.id.moreTreatmentsTextView);
        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return convertView;
    }

    @Override
    public void add(@Nullable Plan object) {
        super.add(object);
    }
}
