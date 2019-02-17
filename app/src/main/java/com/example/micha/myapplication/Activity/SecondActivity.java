package com.example.micha.myapplication.Activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;


import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.example.micha.myapplication.Model.NodeSubgroup;
import com.example.micha.myapplication.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
//Aktywnosc 2
public class SecondActivity extends AppCompatActivity {
    private static final String TAG = "SecondActivity";

    private RadioGroup radioGroup;
    private Button button;
    private CrystalRangeSeekbar rangeSeekbar;
    private TextView tvMin;
    private TextView tvMax;

    private DateFormat dateFormat;
    private Date date;

    long maxTime;
    long minTime;

    private long intentMinDay;
    private long intentMaxDay;

    private int aggregation = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setTitle("Wybierz czas i agregacje");

        //Przypisywanie od zmiennych widokow z layoutu
        button = findViewById(R.id.btn);
        rangeSeekbar = (CrystalRangeSeekbar) findViewById(R.id.rangeSeekbar1);
        tvMin = (TextView) findViewById(R.id.tvMin);
        tvMax = (TextView) findViewById(R.id.tvMax);
        radioGroup = (RadioGroup) findViewById(R.id.rg_aggregation);

        //Format daty na ktory zmienimy timestamp
        dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        date = new Date();

        //Pobranie danych z poprzedniej aktywnosci i przypisanie ich do zmiennych
        Intent intent = getIntent();
        final ArrayList<NodeSubgroup> receivedSensors = intent.getParcelableArrayListExtra("IntentNodeSubgroups");

        maxTime = intent.getLongExtra("MaxDay", -1);
        minTime = intent.getLongExtra("MinDay", -1);


        rangeSeekbar.setMinValue(roundtoDays(minTime));
        rangeSeekbar.setMaxValue(roundtoDays(maxTime));
        tvMin.setText(getTimedate(minTime));
        tvMax.setText(getTimedate(maxTime));
        intentMaxDay = maxTime;
        intentMinDay = minTime;




        //Dzialanie wyboru agregacji
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {


            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_brak:
                        aggregation = 0;

                        break;
                    case R.id.rb_tydzien:
                        aggregation = 1;

                        break;
                    case R.id.rb_miesiac:
                        aggregation = 2;

                        break;
                    case R.id.rb_dzien:
                        aggregation = 3;

                        break;

                }
            }
        });

        // Dzialanie przy przesuwaniu suwakow
        rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                long minDay = (long) (minValue.floatValue() * 86400);
                long maxDay = (long) (maxValue.floatValue() * 86400);
                tvMin.setText(getTimedate(minDay));
                tvMax.setText(getTimedate(maxDay));

            }
        });

        // Dzialanie przy puszczeniu suwaka
        rangeSeekbar.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {
                Log.d("CRS=>", String.valueOf(minValue) + " : " + String.valueOf(maxValue));
                long minDay = (long) (minValue.floatValue() * 86400);
                long maxDay = (long) (maxValue.floatValue() * 86400);
                intentMinDay = minDay;
                intentMaxDay = maxDay;

            }
        });

        //Po wcisnieciu przycisku zapisywanie danych do wyslania i rozpoczecie nastepnej aktywnosci
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(SecondActivity.this, FinalActivity.class);
                intent.putExtra("IntentNodeSubgroups", receivedSensors);
                intent.putExtra("MinDay", intentMinDay);
                intent.putExtra("MaxDay", intentMaxDay);
                intent.putExtra("Aggregation", aggregation);
                startActivity(intent);
            }
        });
    }
     //Z znacznika czasowego na format daty
    private String getTimedate(long timestamp) {

        try {
            date.setTime(timestamp * 1000);
            String check = dateFormat.format(date);
            Log.d(TAG, "getTimedate: " + check);
            return check;
        } catch (Exception ex) {
            return "xx";
        }
    }
    //Zaokraglenie znacznika czasowego do dni
    private long roundtoDays(float f) {
        if (f % 86400 > 0) {
            return (long) (f / 86400) + 1;
        } else {
            return (long) f / 86400;
        }

    }

}
