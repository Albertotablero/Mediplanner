package com.example.mediplanner.calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.mediplanner.MainActivity;
import com.example.mediplanner.R;
import com.example.mediplanner.databinding.FragmentCalendarTabBinding;
import com.example.mediplanner.date.MonthPickerDialogFragment;
import com.example.mediplanner.plan.MorePlanInfoFragment;
import com.example.mediplanner.plan.TreatmentPlan;
import com.example.mediplanner.plan.TreatmentUsage;
import com.example.mediplanner.utils.CloseListener;
import com.example.mediplanner.utils.MoreButtonListener;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;


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

        weekOfSelectedDay = selectedMonth.getDays().get(initDate.getDayOfMonth() - 1).getWeekOfMonth();
        View child = getTableRow(selectedMonth.getDays().get(initDate.getDayOfMonth() - 1).getWeekOfMonth()).getChildAt(selectedMonth.getDays().get(initDate.getDayOfMonth() - 1).getDayOfWeek() - 1);
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
            row.setVisibility(View.GONE);
        }
    }

    private void populateCalendar(LocalDate date) {
        // Populating the Calendar with the required rows and cells
        int dayCounter = 0;
        int weekCounter = 1;
        Day auxDay = selectedMonth.getDays().get(dayCounter);

        if (auxDay.getDayOfWeek() > 1) {
            LocalDate prevMonthDate = date.minusMonths(1);
            Month previousMonth = getMonth(prevMonthDate.getMonthOfYear(), prevMonthDate.getYear());
            // To find the first day of the week from the last week of the previous month the formula basically is:
            // Length of Previous month - (dayOfWeek of first day current month - 2) - 1 Because it's an array
            Day prevMonthDay = previousMonth.getDays().get(previousMonth.getDays().size() - (auxDay.getDayOfWeek() - 2) - 1);
            generateCell(getTableRow(weekCounter), prevMonthDay, false);
            for (int i = 1; i <= auxDay.getDayOfWeek() - 2; i++) {
                prevMonthDay = previousMonth.getDays().get(prevMonthDay.getDayOfMonth());
                generateCell(getTableRow(weekCounter), prevMonthDay, false);
            }
        }
        for (Day day : selectedMonth.getDays()) {
            auxDay = day;
            weekCounter = auxDay.getWeekOfMonth();
            generateCell(getTableRow(weekCounter), auxDay, true);
        }
        if (auxDay.getDayOfWeek() < 7) {
            LocalDate nextMonthDate = date.plusMonths(1);
            Month nextMonth = getMonth(nextMonthDate.getMonthOfYear(), nextMonthDate.getYear());
            int dayCount = 0;
            for (int i = 0; i < 7 - auxDay.getDayOfWeek(); i++) {
                generateCell(getTableRow(auxDay.getWeekOfMonth()), nextMonth.getDays().get(dayCount++), false);
            }
        }
    }

    private TableRow getTableRow(int weekCounter) {
        switch (weekCounter) {
            case 1:
                return binding.calendarRow1;
            case 2:
                return binding.calendarRow2;
            case 3:
                return binding.calendarRow3;
            case 4:
                return binding.calendarRow4;
            case 5:
                return binding.calendarRow5;
            case 6:
                return binding.calendarRow6;

        }
        return null;
    }


    private Month getMonth(int monthNumber, int year) {
        String monthTag = String.format(Locale.getDefault(), "%1$2d/%2$4d", monthNumber, year);
        return MainActivity.getStoredMonths().containsKey(monthTag) ? MainActivity.getStoredMonths().get(monthTag) : MainActivity.addMonth(new Month(monthNumber, year), monthTag);
    }

    private void generateCell(TableRow row, Day day, boolean isCurrentMonth) {
        // Inflating the cells for each day in the month
        DayFrameLayout view = (DayFrameLayout) getLayoutInflater().inflate(R.layout.calendar_day_cell_view, null, false);
        TextView dayText = view.findViewById(R.id.dayTextView);
        view.setCellDay(day);
        dayText.setText(String.format(Locale.getDefault(), "%2d", day.getDayOfMonth()));
        TableRow.LayoutParams params = (TableRow.LayoutParams) view.getLayoutParams();
        if (params != null) params.column = day.getDayOfWeek();

        view.setSelected(false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedDayCell != null) selectedDayCell.setSelected(false);
                selectedDayCell = (DayFrameLayout) v;
                selectedDayCell.setSelected(true);
                selectedDay = selectedDayCell.getCellDay();
                arrayAdapter = new DayTreatmentsArrayAdapter(getContext(), 0, new ArrayList<TreatmentUsage>(sortedTreatmentUsages()), selectedDay, new MoreButtonListener() {
                    @Override
                    public void getMoreInfoFragment(TreatmentPlan treatmentPlan) {
                        generateMoreInfoFragment(treatmentPlan);
                    }
                });
                binding.daysTreatmentList.setAdapter(arrayAdapter);
            }

            // Comparing the hours to sort the list (if no hour present then it goes to the end of the list)
            private @NonNull ArrayList<TreatmentUsage> sortedTreatmentUsages() {
                Stream<TreatmentUsage> treatmentStream = selectedDay.getTreatmentUsages().values().stream().sorted(new Comparator<TreatmentUsage>() {
                    @Override
                    public int compare(TreatmentUsage o1, TreatmentUsage o2) {
                        String format = "%1$d%2$02d";
                        int noHour = 2500; //This will always be greater when compared to hours
                        int val1 = o1.hour != null ? Integer.parseInt(String.format(Locale.getDefault(), format, o1.hour.hours.getHours(), o1.hour.minutes.getMinutes())) : noHour;
                        int val2 = o2.hour != null ? Integer.parseInt(String.format(Locale.getDefault(), format, o2.hour.hours.getHours(), o2.hour.minutes.getMinutes())) : noHour;

                        int compare = Integer.compare(val1, val2);
                        /*
                           If both have no Hour it counts as having the same hour.
                           If both have the same Hour, compares the name, otherwise the value is the result of the comparison.
                        * */
                        return compare == 0 ? o1.treatment.getName().compareToIgnoreCase(o2.treatment.getName()) : compare;
                    }
                });

                return (ArrayList<TreatmentUsage>) treatmentStream.collect(Collectors.toList());
            }
        });
        view.setActivated(isCurrentMonth);
        row.addView(view);
        if (row.getVisibility() != View.VISIBLE) row.setVisibility(View.VISIBLE);
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

    private void generateMoreInfoFragment(TreatmentPlan treatmentPlan) {
        MorePlanInfoFragment moreInfoFragment = new MorePlanInfoFragment(treatmentPlan);
        getParentFragmentManager().beginTransaction().add(moreInfoFragment, "morePlanInfoF").setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).setReorderingAllowed(true).show(moreInfoFragment).commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCalendarTabBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }
}