package com.example.mediplanner.date;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;

public class DatePicker {
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        private static LocalDate date;
        private int year;
        private int month;
        private int day;


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker.
            final Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);

            date = LocalDate.of(year, month, day);

            // Create a new instance of DatePickerDialog and return it.
            return new DatePickerDialog(requireContext(), this, year, month, day);
        }

        public void onDateSet(android.widget.DatePicker view, int year, int month, int day) {
            // Do something with the date the user picks.
            date = LocalDate.of(year, month, day);
            
        }


        public LocalDate getDate() {
            return date;
        }
    }
}
