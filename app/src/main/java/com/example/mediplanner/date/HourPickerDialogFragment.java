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

import com.example.mediplanner.databinding.FragmentHourPickerDialogBinding;
import com.example.mediplanner.utils.CloseListener;

import org.joda.time.DateTime;

import java.util.Locale;

public class HourPickerDialogFragment extends DialogFragment {

    private FragmentHourPickerDialogBinding binding;

    private int initHour = 0;
    private int initMinute = 0;

    private CloseListener positiveListener;

    public HourPickerDialogFragment() {
        super();
    }

    public HourPickerDialogFragment(int initHour, int initMinute, CloseListener listener) {
        super();
        this.initHour = initHour;
        this.initMinute = initMinute;
        this.positiveListener = listener;
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
        binding = FragmentHourPickerDialogBinding.inflate(getLayoutInflater(), null, false);

        // Setting up Number Pickers for hours and minutes with possible values
        setupNumberPicker(0, 23, binding.hoursPicker);

        setupNumberPicker(0, 50, binding.minutesPicker);

        // Setting initial hour and minute of picker to one provided, or the current time
        int h = initHour != 0 ? initHour : DateTime.now().getHourOfDay();
        int m = initMinute != 0 ? initMinute : DateTime.now().getMinuteOfDay();
        binding.hoursPicker.setValue(initHour);
        binding.minutesPicker.setValue(initMinute);


        builder.setView(binding.hoursPickersLayout);
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                positiveListener.OnClose(false);
            }
        });

        return builder.create();
    }

    private void setupNumberPicker(int min, int max, NumberPicker nPicker) {
        nPicker.setMinValue(min);
        nPicker.setMaxValue(max);
        nPicker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return String.format(Locale.getDefault(), "%02d", value);
            }
        });
    }

    public FragmentHourPickerDialogBinding getBinding() {
        return binding;
    }

    public void setInitHour(int initHour) {
        this.initHour = initHour;
    }

    public void setInitMinute(int initMinute) {
        this.initMinute = initMinute;
    }
}
