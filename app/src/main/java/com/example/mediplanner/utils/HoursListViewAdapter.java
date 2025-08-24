package com.example.mediplanner.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.icu.number.IntegerWidth;
import android.icu.number.LocalizedNumberFormatter;
import android.icu.number.NumberFormatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mediplanner.R;
import com.example.mediplanner.date.PlanHours;

import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.joda.time.LocalDate;
import org.joda.time.Minutes;

import java.util.ArrayList;
import java.util.Locale;

public class HoursListViewAdapter extends ArrayAdapter<PlanHours> {
    private ArrayList<PlanHours> specifiedHours;
    private Hours hour;
    private Minutes minutes;

    public HoursListViewAdapter(@NonNull Context context, @NonNull ArrayList<PlanHours> objects) {
        super(context, 0, objects);

        specifiedHours = objects;
        hour = Hours.hours(DateTime.now().getHourOfDay());
        minutes = Minutes.minutes(DateTime.now().getMinuteOfHour());

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.hours_list_item, parent, false);

        //Setting up pickers with hours and minutes
        NumberPicker hoursPicker = convertView.findViewById(R.id.pickerHours);
        hoursPicker.setMinValue(00);
        hoursPicker.setMaxValue(23);
        hoursPicker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return String.format(Locale.getDefault(), "%02d", value);
            }
        });
        hoursPicker.setOnScrollListener(new NumberPicker.OnScrollListener() {
            @Override
            public void onScrollStateChange(NumberPicker view, int scrollState) {
                hour = Hours.hours(view.getValue());
            }
        });

        NumberPicker minutesPicker = convertView.findViewById(R.id.pickerMinutes);
        minutesPicker.setMinValue(00);
        minutesPicker.setMaxValue(59);

        minutesPicker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return String.format(Locale.getDefault(), "%02d", value);
            }
        });
        minutesPicker.setOnScrollListener(new NumberPicker.OnScrollListener() {
            @Override
            public void onScrollStateChange(NumberPicker view, int scrollState) {
                minutes = Minutes.minutes(view.getValue());
            }
        });
        hoursPicker.setValue(hour.getValue(0));
        minutesPicker.setValue(minutes.getValue(0));

        // Manage button for removing an hour
        TextView removeItem = convertView.findViewById((R.id.removeHourButon));
        removeItem.setOnClickListener(e -> removeHour(e, position));

        return convertView;
    }

    private void removeHour(View e, int position) {
        // Confirmation prompt for deletion
        new AlertDialog.Builder(getContext()).setTitle(R.string.removeHourWarningTitle).setMessage(R.string.removeHourPrompt).setIcon(android.R.drawable.ic_dialog_alert).setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int whichButton) {
                remove(getItem(position));
            }
        }).setNegativeButton(android.R.string.no, null).show();
    }


}
