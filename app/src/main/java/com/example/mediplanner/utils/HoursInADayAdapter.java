package com.example.mediplanner.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mediplanner.date.PlanHours;

import java.util.ArrayList;
import java.util.Locale;

public class HoursInADayAdapter extends ArrayAdapter<PlanHours> {
    public HoursInADayAdapter(@NonNull Context context, int resource, @NonNull ArrayList<PlanHours> objects) {
        super(context, 0, objects);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) convertView = new TextView(getContext());
        PlanHours h = getItem(position);

        convertView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        ((TextView) convertView).setText(String.format(Locale.getDefault(), "%1$02d:%2$02d", h.hours.getHours(), h.minutes.getMinutes()));

        return convertView;
    }
}
