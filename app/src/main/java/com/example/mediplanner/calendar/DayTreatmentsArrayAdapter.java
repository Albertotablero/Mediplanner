package com.example.mediplanner.calendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mediplanner.R;
import com.example.mediplanner.date.PlanHours;
import com.example.mediplanner.plan.TreatmentPlan;
import com.example.mediplanner.plan.TreatmentUsage;
import com.example.mediplanner.utils.MoreButtonListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;

public class DayTreatmentsArrayAdapter extends ArrayAdapter<TreatmentUsage> {

    private Day day;
    private ArrayList<TreatmentUsage> treatments;
    private PlanHours hour;

    private MoreButtonListener moreButtonListener;

    public DayTreatmentsArrayAdapter(@NonNull Context context, int resource, @NonNull ArrayList<TreatmentUsage> objects, Day day, MoreButtonListener moreButtonListener) {
        super(context, 0, objects);
        day = day;
        treatments = objects;
        this.moreButtonListener = moreButtonListener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.selected_day_treatments_view, parent, false);
        convertView.setBackgroundColor(parent.getResources().getColor(position % 2 == 0 ? R.color.very_light_grey : R.color.white, parent.getContext().getTheme()));
        TreatmentPlan treatment = treatments.get(position).treatment;
        PlanHours hour = treatments.get(position).hour;

        TextView name = convertView.findViewById(R.id.daytreatmentName);
        name.setText(treatment.getName());

        TextView time = convertView.findViewById(R.id.dayTreatmentHout);

        String text = hour != null ? String.format(Locale.getDefault(), "%1$02d:%2$02d", hour.hours.getHours(), hour.minutes.getMinutes()) : "";
        time.setText(text);


        TextView quanitty = convertView.findViewById(R.id.dayTreatmentQuantity);
        DecimalFormat decimalFormat = new DecimalFormat("0.##");
        quanitty.setText(decimalFormat.format(treatment.getQuantity()));

        TextView quantityMetric = convertView.findViewById(R.id.dayTreatmentQuantityMetric);
        quanitty.setText(treatment.getQuantityMetric());
        TextView completeButton = convertView.findViewById(R.id.completeButton);

        TextView ignoreButton = convertView.findViewById(R.id.ignoreButton);

        TextView moreButton = convertView.findViewById(R.id.moreTreatmentsButton);
        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moreButtonListener.getMoreInfoFragment(treatment);
            }
        });

        return convertView;
    }
}
