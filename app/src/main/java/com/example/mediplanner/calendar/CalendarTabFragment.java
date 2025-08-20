package com.example.mediplanner.calendar;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mediplanner.R;
import com.example.mediplanner.databinding.FragmentCalendarTabBinding;
import com.example.mediplanner.databinding.FragmentTreatmentsTabBinding;

import org.joda.time.LocalDate;

import java.util.Locale;


public class CalendarTabFragment extends Fragment {

    FragmentCalendarTabBinding binding;
    private LocalDate selectedDate;

    public CalendarTabFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectedDate = LocalDate.now();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        binding.monthDislpayed.setText(getMonthName(selectedDate.getMonthOfYear()));
        binding.yeardisplayed.setText(String.format(Locale.getDefault(), "%4d", selectedDate.getYear()));

    }

    private String getMonthName(int monthNumber) {
        switch (monthNumber) {
            case 1:
                return getResources().getString(R.string.january);
            case 2:
                return getResources().getString(R.string.february);
            case 3:
                return getResources().getString(R.string.march);
            case 4:
                return getResources().getString(R.string.april);
            case 5:
                return getResources().getString(R.string.may);
            case 6:
                return getResources().getString(R.string.june);
            case 7:
                return getResources().getString(R.string.july);
            case 8:
                return getResources().getString(R.string.august);
            case 9:
                return getResources().getString(R.string.september);
            case 10:
                return getResources().getString(R.string.october);
            case 11:
                return getResources().getString(R.string.november);
            case 12:
                return getResources().getString(R.string.december);
            default:
                return "";
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCalendarTabBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }
}