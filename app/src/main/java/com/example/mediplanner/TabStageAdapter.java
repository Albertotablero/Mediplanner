package com.example.mediplanner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.mediplanner.calendar.CalendarTabFragment;
import com.example.mediplanner.treatments.TreatmentsTabFragment;
import com.example.mediplanner.utils.MediPlannerConstants;

public class TabStageAdapter extends FragmentStateAdapter {
    public TabStageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new CalendarTabFragment();
            case 1:
                return new TreatmentsTabFragment();
            default:
                return new CalendarTabFragment();
        }

    }

    @Override
    public int getItemCount() {
        return MediPlannerConstants.TAB_COUNT;
    }
}
