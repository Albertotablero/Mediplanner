package com.example.mediplanner.calendar;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.mediplanner.MainActivity;
import com.example.mediplanner.R;
import com.example.mediplanner.databinding.FragmentCalendarTabBinding;
import com.example.mediplanner.databinding.FragmentMonthPickerDialogBinding;
import com.example.mediplanner.date.MonthPickerDialogFragment;
import com.example.mediplanner.utils.CloseListener;

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

        selectedMonth = getMonth(initDate.getMonthOfYear(), initDate.getYear());

        populateCalendar(initDate);
        // TODO Load plan data, good luck Alberto

        weekOfSelectedDay = findInitWeek(initDate);
        TableRow selectedDayRow = (TableRow) binding.calendarTL.getChildAt(weekOfSelectedDay + 1);
        int initDayOfWeek = initDate.getDayOfWeek();
        View child = selectedDayRow.getChildAt(initDayOfWeek - 1);
        child.callOnClick();

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MonthPickerDialogFragment monthPicker = new MonthPickerDialogFragment(selectedMonth, new NumberPicker.Formatter() {
                    @Override
                    public String format(int value) {
                        return getMonthName(value);
                    }
                }, new CloseListener() {
                    @Override
                    public void OnClose(boolean isCanceled) {
                    }

                    @Override
                    public void OnClose(int month, int year) {
                        selectedMonth = getMonth(month, year);
                        selectedDay = null;
                        weekOfSelectedDay = 0;
                        selectedDayCell = null;
                        binding.monthDislpayed.setText(getMonthName(selectedMonth.getMonthOfYear()));
                        binding.yeardisplayed.setText(String.format(Locale.getDefault(), "%4d", selectedMonth.getYear()));
                        clearCalendar();
                        arrayAdapter.clear();
                        arrayAdapter.notifyDataSetChanged();
                        populateCalendar(new LocalDate(selectedMonth.getYear(), selectedMonth.getMonthOfYear(), 1));
                    }
                });
                monthPicker.show(getParentFragmentManager(), "MonthPickerDialogFragment");
            }
        };

        binding.yearAndMonthView.setOnClickListener(listener);

    }

    private void clearCalendar() {
        for (int i = 1; i < binding.calendarTL.getChildCount(); i++) {
            TableRow row = (TableRow) binding.calendarTL.getChildAt(i);
            row.removeAllViews();
        }
    }

    private void populateCalendar(LocalDate date) {
        // Populating the Calendar with the required rows and cells
        int dayOfWeek = 0;
        TableRow weekRow = null;
        for (int i = 0; i < selectedMonth.getWeeks().size(); i++) {
            weekRow = (TableRow) binding.calendarTL.getChildAt(i + 1);
            Week week = selectedMonth.getWeeks().get(i);


            int dayN = 0;
            dayOfWeek = 0;
            // If month doesn't start on monday, populate previews cells with previows month's last days
            int firstDayOfWeek = week.getDaysOfWeek().get(0).getDayOfWeek();
            if (i == 0 && firstDayOfWeek != 1) {
                LocalDate previousMonthDate = date.minusMonths(1);
                Month previowsMonth = getMonth(previousMonthDate.getMonthOfYear(), previousMonthDate.getYear());

                int k = previowsMonth.getDays().get(previowsMonth.getDays().size() - (firstDayOfWeek - 1)).getDayOfMonth();
                for (; dayOfWeek < firstDayOfWeek - 1; k++) {
                    Day day = previowsMonth.getDays().get(k - 1);
                    generateCell(weekRow, dayOfWeek++, day, String.format(Locale.getDefault(), "%2d", day.getDayOfMonth()), false);
                }
                dayOfWeek = firstDayOfWeek - 1;
            }
            try {
                for (; dayOfWeek < 7; dayOfWeek++) {

                    Day day = week.getDaysOfWeek().get(dayN++);

                    String dayOfMonth = String.format(Locale.getDefault(), "%2d", day.getDayOfMonth());

                    generateCell(weekRow, dayOfWeek, day, dayOfMonth, true);

                }
            } catch (IndexOutOfBoundsException e) {

            }

            weekRow.setVisibility(View.VISIBLE);
        }
        if (dayOfWeek < 7) {
            LocalDate nextMonthDate = date.minusMonths(1);
            Month nextMonth = getMonth(nextMonthDate.getMonthOfYear(), nextMonthDate.getYear());

            int k = nextMonth.getDays().get(0).getDayOfMonth();
            for (; dayOfWeek < 7; k++) {
                Day day = nextMonth.getDays().get(k - 1);
                generateCell(weekRow, dayOfWeek++, day, String.format(Locale.getDefault(), "%2d", day.getDayOfMonth()), false);
            }
        }
    }

    private int findInitWeek(LocalDate initDate) {
        for (int i = 0; i < selectedMonth.getWeeks().size(); i++) {
            for (Day d : selectedMonth.getWeeks().get(i).getDaysOfWeek()) {
                if (d.getDayOfMonth() == initDate.getDayOfMonth()) return i;
            }
        }
        return 1;
    }

    private Month getMonth(int monthNumber, int year) {
        String monthTag = String.format(Locale.getDefault(), "%1$2d/%2$4d", monthNumber, year);
        return MainActivity.getStoredMonths().containsKey(monthTag) ? MainActivity.getStoredMonths().get(monthTag) : MainActivity.addMonth(new Month(monthNumber, year), monthTag);
    }

    private void generateCell(TableRow row, int j, Day day, String dayOfMonth, boolean isCurrentMonth) {
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
        view.setActivated(isCurrentMonth);
        row.addView(view);
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