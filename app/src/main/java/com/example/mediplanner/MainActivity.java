package com.example.mediplanner;


import android.nfc.Tag;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import com.example.mediplanner.calendar.CalendarTabFragment;
import com.example.mediplanner.calendar.Month;
import com.example.mediplanner.databinding.ActivityMainBinding;
import com.example.mediplanner.treatments.TreatmentsTabFragment;
import com.example.mediplanner.utils.CloseListener;
import com.example.mediplanner.plan.Plan;
import com.example.mediplanner.plan.PlanFragment;
import com.example.mediplanner.treatments.TreatmentsArrayAdapter;
import com.example.mediplanner.utils.FrequencyMetric;
import com.example.mediplanner.utils.TabStageAdapter;
import com.google.android.material.tabs.TabLayout;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    // view binding
    private ActivityMainBinding mainActivityBinding;

    public FragmentManager fragmentManager;

    //Fragments
    private TabStageAdapter tabStageAdapter;
    private ViewPager2 viewPager;

    // Tab fragments
    private static CalendarTabFragment calendarTabFragment;
    private static TreatmentsTabFragment treatmentsTabFragment;

    // Plan creation fragment
    private PlanFragment planFragment;

    //List of all treatment plans created
    private static ArrayList<Plan> treatmentPlanList;

    // List of all months with data stored
    private static ArrayList<Month> savedMonths = new ArrayList<>();
    private static HashMap<String, Month> storedMonths = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        //Stablishing acivity binding
        mainActivityBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View mainView = mainActivityBinding.getRoot();
        setContentView(mainView);

        treatmentPlanList = new ArrayList<>();
        // TODO temp, remove after testing
        treatmentPlanList.add(new Plan("meh", "meh", true, 0, 1, 0, "Pill's", LocalDate.now(), true, 2, FrequencyMetric.ASNEEDED));
        treatmentPlanList.add(new Plan("meh", "meh", true, 2, 2, 200.5f, "Ml", LocalDate.now(), true, 2, FrequencyMetric.ASNEEDED));
        treatmentPlanList.add(new Plan("meh", "meh", true, 0, 1, 4, "Pill's", LocalDate.now(), true, 2, FrequencyMetric.ASNEEDED));
        treatmentPlanList.add(new Plan("meh", "meh", true, 3, 1, 2, "Pill's", LocalDate.now(), true, 2, FrequencyMetric.ASNEEDED));

        //Setting up fragments
        fragmentManager = getSupportFragmentManager();


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Inserting tab fragments into the main view
        tabStageAdapter = new TabStageAdapter(this);
        viewPager = mainActivityBinding.pager;
        viewPager.setAdapter(tabStageAdapter);
        viewPager.setOffscreenPageLimit(1);
        viewPager.setCurrentItem(0);

        mainActivityBinding.mainTabView.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mainActivityBinding.mainTabView.getTabAt(position).select();
            }
        });


        //Other Interface elements
        mainActivityBinding.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Plan creation fragment
                planFragment = new PlanFragment(new CloseListener() {
                    @Override
                    public void OnClose(boolean isCanceled) {
                        if (!isCanceled) {
                            TreatmentsArrayAdapter tAAdapter = (TreatmentsArrayAdapter) treatmentsTabFragment.getBinding().treatmentPlansListView.getAdapter();
                            tAAdapter.add(planFragment.getPlan());
                            tAAdapter.notifyDataSetChanged();
                        }
                    }

                });
                planFragment.setNew(true);
                planFragment.setCancelable(false);
                fragmentManager.beginTransaction().add(planFragment, "PlanCreation").setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).show(planFragment).commit();

            }
        });
        /*
         *  TODO Load list from save file
         * */
    }

    public static CalendarTabFragment getCalendarTabFragment() {
        return calendarTabFragment;
    }

    public static void setCalendarTabFragment(CalendarTabFragment calendarTabFragment) {
        MainActivity.calendarTabFragment = calendarTabFragment;
    }

    public static TreatmentsTabFragment getTreatmentsTabFragment() {
        return treatmentsTabFragment;
    }

    public static void setTreatmentsTabFragment(TreatmentsTabFragment treatmentsTabFragment) {
        MainActivity.treatmentsTabFragment = treatmentsTabFragment;
    }

    public static ArrayList<Plan> getTreatmentPlanList() {
        return treatmentPlanList;
    }

    public static ArrayList<Month> getSavedMonths() {
        return savedMonths;
    }

    public static HashMap<String, Month> getStoredMonths() {
        return storedMonths;
    }
}
