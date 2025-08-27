package com.example.mediplanner.date;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.mediplanner.R;
import com.example.mediplanner.calendar.Month;
import com.example.mediplanner.databinding.FragmentHourPickerDialogBinding;
import com.example.mediplanner.databinding.FragmentMonthPickerDialogBinding;
import com.example.mediplanner.utils.CloseListener;

public class MonthPickerDialogFragment extends DialogFragment {

    private Month initMonth;
    private CloseListener closeListener;
    private NumberPicker.Formatter monthFormatter;
    private FragmentMonthPickerDialogBinding binding;
    private NumberPicker monthPicker;
    private NumberPicker yearPicker;

    public MonthPickerDialogFragment(Month initMonth, NumberPicker.Formatter monthFormatter, CloseListener closeListener) {
        this.initMonth = initMonth;
        this.monthFormatter = monthFormatter;
        this.closeListener = closeListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return binding.getRoot();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        binding = FragmentMonthPickerDialogBinding.inflate(getLayoutInflater(), null, false);

        monthPicker = binding.monthPicker;
        monthPicker.setMinValue(1);
        monthPicker.setMaxValue(12);
        monthPicker.setFormatter(monthFormatter);
        monthPicker.setValue(initMonth.getMonthOfYear());

        yearPicker = binding.yearPicker;
        yearPicker.setMinValue(2000);
        yearPicker.setMaxValue(3000);
        yearPicker.setValue(initMonth.getYear());

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (initMonth.getMonthOfYear() != monthPicker.getValue() || initMonth.getYear() != yearPicker.getValue())
                    closeListener.OnClose(monthPicker.getValue(), yearPicker.getValue());
            }
        });
        builder.setView(binding.monthPickerRoot);

        return builder.create();
    }

    public NumberPicker getMonthPicker() {
        return monthPicker;
    }

    public NumberPicker getYearPicker() {
        return yearPicker;
    }

    public FragmentMonthPickerDialogBinding getBinding() {
        return binding;
    }
}
