package com.example.mediplanner.plan;

public enum TreatmentForm {
    PILLS(1), LIQUID(2), DROPS(3), INHALER(4), INJECTION(5), CREAM(6), PATCHES(7), PACKET(8);

    public int getValue() {
        return value;
    }

    private int value;

    TreatmentForm(int i) {
        value = i;
    }
}
