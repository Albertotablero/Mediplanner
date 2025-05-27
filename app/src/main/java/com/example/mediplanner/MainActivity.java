package com.example.mediplanner;


import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import com.example.mediplanner.databinding.ActivityMainBinding;
import com.example.mediplanner.plan.PlanFragment;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mainActivityBinding;

    public FragmentManager fragmentManager;

    //Fragments
    private TabStageAdapter tabStageAdapter;
    private ViewPager2 viewPager;

    private Fragment calendarTabFragment;

    private Fragment treatmentsTabFragment;

    private PlanFragment planFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        //Stablishing acivity binding
        mainActivityBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View mainView = mainActivityBinding.getRoot();
        setContentView(mainView);

        //Setting up fragments
        fragmentManager = getSupportFragmentManager();

//        //Calendar Tab fragment
//        calendarTabFragment = fragmentManager.getFragmentFactory().instantiate(ClassLoader.getSystemClassLoader(), "com.example.mediplanner.calendar.CalendarTabFragment");
//
//        //Treatments Tab fragment
//        treatmentsTabFragment = fragmentManager.getFragmentFactory().instantiate(ClassLoader.getSystemClassLoader(), "com.example.mediplanner.treatments.TreatmentsTabFragment");


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Inserting tab fragments into the main view
        tabStageAdapter = new TabStageAdapter(this);
        viewPager = mainActivityBinding.pager;
        viewPager.setAdapter(tabStageAdapter);

        mainActivityBinding.mainTabView.addOnTabSelectedListener(
                new TabLayout.OnTabSelectedListener() {

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
                }
        );

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
                Log.d("testing", "hmm ok");
                //Plan creator // editor fragment
                planFragment = (PlanFragment) fragmentManager.getFragmentFactory().instantiate(ClassLoader.getSystemClassLoader(), "com.example.mediplanner.plan.PlanFragment");
                planFragment.setNew(true);
                fragmentManager.beginTransaction().add(planFragment, "planF").setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).setReorderingAllowed(true).show(planFragment).commit();
            }
        });

    }
}