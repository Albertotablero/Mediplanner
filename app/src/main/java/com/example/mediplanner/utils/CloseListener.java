package com.example.mediplanner.utils;

import com.example.mediplanner.calendar.Month;

public interface CloseListener {
    public void OnClose(boolean isCanceled);


    public abstract void OnClose(int month, int year);
}
