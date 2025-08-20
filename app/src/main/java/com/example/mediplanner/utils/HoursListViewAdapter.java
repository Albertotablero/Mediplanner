package com.example.mediplanner.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mediplanner.R;

import java.util.ArrayList;
import java.util.Locale;

public class HoursListViewAdapter extends ArrayAdapter<String> {
    private ArrayList<String> specifiedHours;
    private ArrayList<String> hours;
    private ArrayList<String> minutes;

    public HoursListViewAdapter(@NonNull Context context, @NonNull ArrayList<String> objects) {
        super(context, 0, objects);

        specifiedHours = objects;

        //preparing hours and minutes for spinners
        hours = new ArrayList<>();
        minutes = new ArrayList<>();
        fillHoursAndMinutes(hours, minutes);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String hour = getItem(position);
        String[] time = hour.split(":");
        int h = Integer.parseInt(time[0]);
        int m = Integer.parseInt(time[1]);

        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.hours_list_item, parent, false);

        //Setting up spinners with hours and minutes
        Spinner hourSpinner = convertView.findViewById(R.id.spinnerHours);
        String[] helpme1 = new String[hours.size()];
        SpinnerAdapter hourSpinAdpt = new SpinnerAdapter(getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, hours.toArray(helpme1), false);

        hourSpinner.setAdapter(hourSpinAdpt);
        hourSpinner.setSelection(h);
        // Updating the value in the stored list on item selection, otherwise selection changes to previous stored value when adding more elements to the list
        hourSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                specifiedHours.set(position, pos + ":" + m);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner minuteSpinner = convertView.findViewById(R.id.spinnerMinutes);
        String[] helpme = new String[minutes.size()];
        SpinnerAdapter minuteSpinAdpt = new SpinnerAdapter(getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, minutes.toArray(helpme), false);

        minuteSpinner.setAdapter(minuteSpinAdpt);
        minuteSpinner.setSelection(m);
        minuteSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                specifiedHours.set(position, h + ":" + pos);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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


    static public void fillHoursAndMinutes(ArrayList<String> h, ArrayList<String> m) {
        String format = "%02d";

        for (int i = 0; i <= 24; i++) {
            h.add(String.format(Locale.getDefault(), format, i));
        }

        for (int i = 0; i < 60; i++) {
            m.add(String.format(Locale.getDefault(), format, i));
        }
    }

}
