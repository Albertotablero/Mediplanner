package com.example.mediplanner.calendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mediplanner.R;
import com.example.mediplanner.plan.Plan;
import com.example.mediplanner.plan.TreatmentUsage;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class DayTreatmentsArrayAdapter extends ArrayAdapter<TreatmentUsage> {

    private Day day;
    private ArrayList<TreatmentUsage> treatments;

    public DayTreatmentsArrayAdapter(@NonNull Context context, int resource, @NonNull ArrayList<TreatmentUsage> objects, Day day) {
        super(context, 0, objects);
        day = day;
        treatments = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.selected_day_treatments_view, parent, false);

        Plan treatment = treatments.get(position).treatment;

        TextView name = convertView.findViewById(R.id.daytreatmentName);
        name.setText(treatment.getName());

        TextView details = convertView.findViewById(R.id.dayTreatmentHout);
        details.setText(treatment.getDetails());

        TextView quanitty = convertView.findViewById(R.id.dayTreatmentQuantity);
        DecimalFormat decimalFormat = new DecimalFormat("0.##");
        quanitty.setText(decimalFormat.format(treatment.getQuantity()));

        TextView quantityMetric = convertView.findViewById(R.id.dayTreatmentQuantityMetric);
        quanitty.setText(treatment.getQuantityMetric());

        TextView more = convertView.findViewById(R.id.moreTreatmentsButtons);

        TextView completeButton = convertView.findViewById(R.id.completeButton);

        TextView ignoreButton = convertView.findViewById(R.id.ignoreButton);

        return convertView;
    }
}
