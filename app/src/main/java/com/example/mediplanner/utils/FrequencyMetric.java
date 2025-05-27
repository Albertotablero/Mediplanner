package com.example.mediplanner.utils;

public enum FrequencyMetric {
    HOURLY(0), DAILY(1), WEEKLY(2), MONTHLY(3), YEARLY(4), SPECIFFICDATES(5), ASNEEDED(6);

    public int getValue() {
        return value;
    }

    private int value;

    FrequencyMetric(int i) {
        value = i;
    }
}
