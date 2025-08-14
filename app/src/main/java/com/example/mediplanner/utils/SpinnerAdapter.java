package com.example.mediplanner.utils;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class SpinnerAdapter extends ArrayAdapter<String> {
    private boolean hasAPrompt = false;

    public SpinnerAdapter(@NonNull Context context, int resource, String[] items, boolean hasPrompt) {
        super(context, resource, items);
        hasAPrompt = hasPrompt;
    }

    // Dissabling the 1º element to use as hint
    @Override
    public boolean isEnabled(int position) {
        boolean respone = true;
        if (hasAPrompt && position == 0) respone = false;
        return respone;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView view = (TextView) super.getDropDownView(position, convertView, parent);
        if (hasAPrompt && position == 0) {
            view.setTextColor(Color.GRAY);
        }


        return view;
    }

}
