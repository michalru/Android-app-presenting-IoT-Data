package com.example.micha.myapplication.Others;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.example.micha.myapplication.R;
import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
//Znacznik po wybraniu punktu
public class MyMarkerView extends MarkerView implements IMarker {

    private static final String TAG = "MyMarkerView";
    private TextView tvContent;
    private long referenceTimestamp;  // minimum timestamp in your data set
    private DateFormat mDataFormat;
    private Date mDate;
    private int aggregate;


    public MyMarkerView (Context context, int layoutResource, long referenceTimestamp,int aggregate) {
        super(context, layoutResource);
        // this markerview only displays a textview
        tvContent = (TextView) findViewById(R.id.tvContent);
        this.referenceTimestamp = referenceTimestamp;
        this.aggregate=aggregate;
        this.mDate = new Date();
    }


    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        long currentTimestamp = (int)e.getX() + referenceTimestamp;
        String pattern;
        String[] Months= new String[12];
        float wartosc = e.getY();
        java.text.DecimalFormat df=new java.text.DecimalFormat();
        df.setMaximumFractionDigits(2);

        switch (aggregate){
            case 0:
                pattern="dd-MM-yyyy HH:mm";
                this.mDataFormat = new SimpleDateFormat(pattern, Locale.ENGLISH);
                tvContent.setText(df.format(wartosc)+ " w " + getTimedate(currentTimestamp)); // set the entry-value as the display text
                break;
            case 1:
                pattern="W";

                Months[0]="Stycznia";
                Months[1]="Lutego";
                Months[2]="Marca";
                Months[3]="Kwietnia";
                Months[4]="Maja";
                Months[5]="Czerwca";
                Months[6]="Lipca";
                Months[7]="Sierpnia";
                Months[8]="Września";
                Months[9]="Października";
                Months[10]="Listopada";
                Months[11]="Grudnia";


                this.mDataFormat = new SimpleDateFormat(pattern, Locale.ENGLISH);
                tvContent.setText(df.format(wartosc) + " w " + getTimedate(currentTimestamp)+" tygodniu "+Months[Integer.valueOf(getMonth(currentTimestamp))-1]+" "+getYear(currentTimestamp)); // set the entry-value as the display text
                break;
            case 2:
                pattern="M";
                Months[0]="Styczniu";
                Months[1]="Lutym";
                Months[2]="Marcu";
                Months[3]="Kwietniu";
                Months[4]="Maju";
                Months[5]="Czerwcu";
                Months[6]="Lipcu";
                Months[7]="Sierpniu";
                Months[8]="Wrześniu";
                Months[9]="Październiku";
                Months[10]="Listopadzie";
                Months[11]="Grudniu";
                this.mDataFormat = new SimpleDateFormat(pattern, Locale.ENGLISH);
                tvContent.setText(df.format(wartosc)+ " w " + Months[Integer.valueOf(getTimedate(currentTimestamp))-1]+" "+getYear(currentTimestamp)); // set the entry-value as the display text
                break;
            case 3:
                pattern="d'.'MM'.'yy";
                this.mDataFormat = new SimpleDateFormat(pattern, Locale.ENGLISH);
                tvContent.setText(df.format(wartosc) + " w " + getTimedate(currentTimestamp)); // set the entry-value as the display text
                break;
        }

        super.refreshContent(e, highlight);
    }


    private MPPointF mOffset;

    @Override
    public MPPointF getOffset() {

        if(mOffset == null) {
            // center the marker horizontally and vertically
            mOffset = new MPPointF(-(getWidth() / 2), -getHeight() - 10);
        }

        return mOffset;
    }




    private String getTimedate(long timestamp){

        try{
            mDate.setTime(timestamp*1000);
            String check = mDataFormat.format(mDate);

            return check;
            }
        catch(Exception ex){
            return "xx";
        }
    }
    private String getMonth(long timestamp){

        try{
            DateFormat dateFormat = new SimpleDateFormat("M", Locale.ENGLISH);
            mDate.setTime(timestamp*1000);
            String check = dateFormat.format(mDate);

            return check;
        }
        catch(Exception ex){
            return "xx";
        }
    }
    private String getYear(long timestamp){

        try{
            DateFormat dateFormat = new SimpleDateFormat("yy", Locale.ENGLISH);
            mDate.setTime(timestamp*1000);
            String check = dateFormat.format(mDate);

            return check;
        }
        catch(Exception ex){
            return "xx";
        }
    }
}
