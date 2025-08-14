package com.example.mediplanner.utils;

public enum FrequencyMetric {
    HOURLY(1), DAILY(2), WEEKLY(3), MONTHLY(4), YEARLY(5), SPECIFFICDATES(6), ASNEEDED(7);

    public int getValue() {
        return value;
    }

    private int value;

    FrequencyMetric(int i) {
        value = i;
    }
}
