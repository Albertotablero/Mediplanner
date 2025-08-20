package com.example.mediplanner.utils;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.mediplanner.MainActivity;
import com.example.mediplanner.calendar.CalendarTabFragment;
import com.example.mediplanner.treatments.TreatmentsTabFragment;

public class TabStageAdapter extends FragmentStateAdapter {
    public TabStageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                TreatmentsTabFragment treatmentsTabFragment = new TreatmentsTabFragment();
                MainActivity.setTreatmentsTabFragment(treatmentsTabFragment);
                return treatmentsTabFragment;
            default:
                CalendarTabFragment calendarTabFragment = new CalendarTabFragment();
                MainActivity.setCalendarTabFragment(calendarTabFragment);
                return calendarTabFragment;
        }

    }

    @Override
    public int getItemCount() {
        return MediPlannerConstants.TAB_COUNT;
    }

}
