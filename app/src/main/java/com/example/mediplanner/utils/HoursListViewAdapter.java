package com.example.mediplanner.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.example.mediplanner.R;
import com.example.mediplanner.date.HourPickerDialogFragment;
import com.example.mediplanner.date.PlanHours;

import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.joda.time.Minutes;

import java.util.ArrayList;
import java.util.Locale;

public class HoursListViewAdapter extends ArrayAdapter<PlanHours> {
    private ArrayList<PlanHours> specifiedHours;
    private Hours hour;
    private Minutes minutes;
    private HourPickerDialogFragment hourPickerDialogFragment;
    private FragmentManager fragmentManager;

    public HoursListViewAdapter(@NonNull Context context, @NonNull ArrayList<PlanHours> objects, FragmentManager fManager) {
        super(context, 0, objects);
        fragmentManager = fManager;
        specifiedHours = objects;
        hour = Hours.hours(DateTime.now().getHourOfDay());
        minutes = Minutes.minutes(DateTime.now().getMinuteOfHour());
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.hours_list_item, parent, false);

        PlanHours item = getItem(position);

        //Setting up pickers with hours and minutes
        String f = "%02d";
        Locale l = Locale.getDefault();

        TextView hour = convertView.findViewById(R.id.pickedHour);
        hour.setText(String.format(l, f, item.hours.getHours()));

        TextView minutes = convertView.findViewById(R.id.pickedMinute);
        minutes.setText(String.format(l, f, item.minutes.getMinutes()));

        LinearLayout hoursView = convertView.findViewById(R.id.hoursLayout);

        hoursView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hourPickerDialogFragment = new HourPickerDialogFragment(item.hours.getHours(), item.minutes.getMinutes(), new CloseListener() {
                    @Override
                    public void OnClose(boolean isCanceled) {
                        item.hours = Hours.hours(hourPickerDialogFragment.getBinding().hoursPicker.getValue());
                        item.minutes = Minutes.minutes(hourPickerDialogFragment.getBinding().minutesPicker.getValue());
                        notifyDataSetChanged();
                    }

                    @Override
                    public void OnClose(int month, int year) {

                    }
                });
                hourPickerDialogFragment.show(fragmentManager, "meh");
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
                notifyDataSetChanged();
            }
        }).setNegativeButton(android.R.string.no, null).show();
    }


}
