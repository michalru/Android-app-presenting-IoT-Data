package com.example.micha.myapplication.Others;


import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


//Format osi X bez aggregacji
public class XAxisValueFormatter implements IAxisValueFormatter {

    private long referenceTimestamp;

    private Date mDate;
    private int aggregate;


    public XAxisValueFormatter(long referenceTimestamp, int aggregate) {
        this.referenceTimestamp = referenceTimestamp;
        this.aggregate = aggregate;

        this.mDate = new Date();
    }


    @Override
    public String getFormattedValue(float value, AxisBase axis) {

        long convertedTimestamp = (long) value;


        long originalTimestamp = referenceTimestamp + convertedTimestamp;

        String[] Months = new String[12];
        Months[0] = "Styczeń";
        Months[1] = "Luty";
        Months[2] = "Marzec";
        Months[3] = "Kwiecień";
        Months[4] = "Maj";
        Months[5] = "Czerwiec";
        Months[6] = "Lipiec";
        Months[7] = "Sierpień";
        Months[8] = "Wrzesień";
        Months[9] = "Październik";
        Months[10] = "Listopad";
        Months[11] = "Grudzień";


        switch (aggregate) {
            case 0:

                return  getHour(originalTimestamp)+" "+getDayWithoutYear(originalTimestamp);

            case 1:

                return getDay(originalTimestamp);

            case 2:

                return Months[Integer.valueOf(getMonth(originalTimestamp)) - 1] + " " + getYear(originalTimestamp);

            case 3:

                return getDay(originalTimestamp);
        }

        return "Error";
    }


    private String getHour(long timestamp) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
            mDate.setTime(timestamp * 1000);
            return dateFormat.format(mDate);
        } catch (Exception ex) {
            return "xx";
        }
    }

    private String getDay(long timestamp) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("d'.'MM'.'yy", Locale.ENGLISH);
            mDate.setTime(timestamp * 1000);
            return dateFormat.format(mDate);
        } catch (Exception ex) {
            return "xx";
        }
    }

    private String getDayWithoutYear(long timestamp) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("d'.'MM", Locale.ENGLISH);
            mDate.setTime(timestamp * 1000);
            return dateFormat.format(mDate);
        } catch (Exception ex) {
            return "xx";
        }
    }

    private String getMonth(long timestamp) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("M", Locale.ENGLISH);
            mDate.setTime(timestamp * 1000);
            return dateFormat.format(mDate);
        } catch (Exception ex) {
            return "xx";
        }
    }

    private String getYear(long timestamp) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yy", Locale.ENGLISH);
            mDate.setTime(timestamp * 1000);
            return dateFormat.format(mDate);
        } catch (Exception ex) {
            return "xx";
        }
    }
}