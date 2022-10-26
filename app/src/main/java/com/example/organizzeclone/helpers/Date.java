package com.example.organizzeclone.helpers;

import android.util.Log;

import java.text.SimpleDateFormat;

public class Date {

    public static String currentDate() {
        long date = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = simpleDateFormat.format(date);
        return dateString;
    }

    public static String monthlyAndYear(String date) {
        String splitDate[] = date.split("/");
        Log.d("TESTE DATA",  splitDate[0]);
        Log.d("TESTE DATA",  splitDate[1]);
        Log.d("TESTE DATA",  splitDate[2]);
        String newDate = splitDate[1] + splitDate[2];
        return newDate;

    }
}
