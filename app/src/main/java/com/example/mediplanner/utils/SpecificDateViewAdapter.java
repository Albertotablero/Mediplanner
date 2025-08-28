package com.example.mediplanner.utils;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mediplanner.R;

import java.util.ArrayList;

public class SpecificDateViewAdapter extends ArrayAdapter<String> {

    private ArrayList<String> specifiedDates;
    private DatePickerDialog datePickerDialog;
    private String dateFormat;

    public SpecificDateViewAdapter(@NonNull Context context, @NonNull ArrayList<String> objects, String datesFormat) {
        super(context, 0, objects);
        specifiedDates = objects;
        dateFormat = datesFormat;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.specific_dates_frequency_item, parent, false);

        EditText date = convertView.findViewById(R.id.startDateInput);
        date.setText(specifiedDates.get(position));

        ImageButton calendarDialogButton = convertView.findViewById(R.id.startDateCalendarDialogB);
        calendarDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] currentDateArray = specifiedDates.get(position).split("/");
                int mYear = Integer.parseInt(currentDateArray[2]);
                int mMonth = Integer.parseInt(currentDateArray[1]);
                int mDay = Integer.parseInt(currentDateArray[0]);

                // date picker dialog
                datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker dp, int year, int month, int dayOfMonth) {
                        specifiedDates.set(position, String.format(dateFormat, dayOfMonth, month + 1, year));
                        date.setText(specifiedDates.get(position));
                    }

                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        TextView removeDate = convertView.findViewById(R.id.removeDateButon);
        removeDate.setOnClickListener(e -> removeDate(e, position));
        return convertView;
    }

    private void removeDate(View e, int position) {
        // Confirmation prompt for deletion
        new AlertDialog.Builder(getContext()).setTitle(R.string.removeHourWarningTitle).setMessage(R.string.removeHourPrompt).setIcon(android.R.drawable.ic_dialog_alert).setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int whichButton) {
                remove(getItem(position));
            }
        }).setNegativeButton(android.R.string.no, null).show();
    }

}
