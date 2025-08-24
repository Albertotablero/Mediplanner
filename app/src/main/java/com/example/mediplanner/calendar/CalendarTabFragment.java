package com.example.mediplanner.calendar;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.mediplanner.MainActivity;
import com.example.mediplanner.R;
import com.example.mediplanner.databinding.FragmentCalendarTabBinding;

import org.joda.time.LocalDate;

import java.util.Locale;


public class CalendarTabFragment extends Fragment {

    private FragmentCalendarTabBinding binding;

    private Month selectedMonth;
    private int weekOfSelectedDay = 0;
    private DayFrameLayout selectedDayCell;
    private Day selectedDay;

    private DayTreatmentsArrayAdapter arrayAdapter;

    public CalendarTabFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        LocalDate initDate = LocalDate.now();
        // Month to display above calendar (Represents selected month, current by default)
        binding.monthDislpayed.setText(getMonthName(initDate.getMonthOfYear()));
        binding.yeardisplayed.setText(String.format(Locale.getDefault(), "%4d", initDate.getYear()));

        // TODO check if Month already exist before doing this
        selectedMonth = getMonth(initDate.getMonthOfYear(), initDate.getYear());

        // Populating the Calendar with the required rows and cells
        for (int i = 0; i < selectedMonth.getWeeks().size(); i++) {
            TableRow row = (TableRow) binding.calendarTL.getChildAt(i + 1);
            Week week = selectedMonth.getWeeks().get(i);

            int j = 0;
            int y = 0;
            if (i == 0 && week.getDaysOfWeek().get(0).getDayOfWeek() != 1) {

                for (; j < week.getDaysOfWeek().get(0).getDayOfWeek(); j++) {
                    generateCell(row, j, null, "");
                }
            }
            for (; j < 7; j++) {
                String dayOfMonth = "";
                Day day = null;
                try {
                    day = week.getDaysOfWeek().get(y++);
                    dayOfMonth = String.format(Locale.getDefault(), "%2d", day.getDayOfMonth());
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
                generateCell(row, j, day, dayOfMonth);
            }
            row.setVisibility(View.VISIBLE);

        }
        // Load plan data, good luck Alberto

        weekOfSelectedDay = findInitWeek(initDate);
        TableRow row = (TableRow) binding.calendarTL.getChildAt(weekOfSelectedDay + 1);
        int dayOfWeek = initDate.getDayOfWeek();
        View child = row.getChildAt(dayOfWeek - 1);
        child.callOnClick();

    }

    private int findInitWeek(LocalDate initDate) {
        for (int i = 0; i < selectedMonth.getWeeks().size(); i++) {
            for (Day d :
                    selectedMonth.getWeeks().get(i).getDaysOfWeek()) {
                if (d.getDayOfMonth() == initDate.getDayOfMonth())
                    return i;
            }
        }
        return 1;
    }

    private Month getMonth(int monthNumber, int year) {
        Month month;
        String monthTag = String.format(Locale.getDefault(), "%1$2d/%2$4d", monthNumber, year);

        if (MainActivity.getStoredMonths().containsKey(monthTag)) {
            month = MainActivity.getStoredMonths().get(monthTag);
        } else {
            month = new Month(monthNumber, year);

            MainActivity.addMonth(month, monthTag);
        }

        return month;
    }

    private void generateCell(TableRow row, int j, Day day, String dayOfMonth) {
        // Inflating the cells for each day in the month
        DayFrameLayout view = (DayFrameLayout) getLayoutInflater().inflate(R.layout.calendar_day_cell_view, null, false);
        TextView dayText = view.findViewById(R.id.dayTextView);
        view.setCellDay(day);
        dayText.setText(dayOfMonth);
        TableRow.LayoutParams params = (TableRow.LayoutParams) view.getLayoutParams();
        if (params != null) params.column = 1;

        view.setSelected(false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedDayCell != null) selectedDayCell.setSelected(false);
                selectedDayCell = (DayFrameLayout) v;
                selectedDayCell.setSelected(true);
                selectedDay = selectedDayCell.getCellDay();
                arrayAdapter = new DayTreatmentsArrayAdapter(getContext(), 0, selectedDay.getTreatmentusages(), selectedDay);
                binding.daysTreatmentList.setAdapter(arrayAdapter);
            }
        });
        row.addView(view, j);
    }

    public void onRowClicked(int i) {
        weekOfSelectedDay = i;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCalendarTabBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }
}