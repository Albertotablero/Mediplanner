package com.example.mediplanner.calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DayFrameLayout extends FrameLayout {

    private Day cellDay;

    public DayFrameLayout(@NonNull Context context) {
        super(context);
    }

    public DayFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DayFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DayFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public Day getCellDay() {
        return cellDay;
    }

    public void setCellDay(Day cellDay) {
        this.cellDay = cellDay;
    }
}
