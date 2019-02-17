
package com.example.micha.myapplication.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TimingLogger;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.CombinedChart.DrawOrder;
import com.example.micha.myapplication.Model.NodeSubgroup;
import com.example.micha.myapplication.Model.SensorWithData;
import com.example.micha.myapplication.Model.ZoomAndMove;
import com.example.micha.myapplication.Others.MyMarkerView;
import com.example.micha.myapplication.Others.MyWebService;
import com.example.micha.myapplication.Pojo.DataOutput;
import com.example.micha.myapplication.Pojo.Datum;
import com.example.micha.myapplication.Pojo.PojoAllSecondActivity;
import com.example.micha.myapplication.Pojo.PojoData;
import com.example.micha.myapplication.R;
import com.example.micha.myapplication.Others.XAxisValueFormatter;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.components.LimitLine.LimitLabelPosition;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.EntryXComparator;
import com.github.mikephil.charting.utils.MPPointD;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.gson.Gson;


import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;


import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


//Aktywnosc 3
public class FinalActivity extends AppCompatActivity {
    private static final String TAG = "FinalActivity";

    private ListView lv;
    private ProgressDialog processDialog;

    private Retrofit retrofit;
    private MyWebService service;

    private long referenceTimestamp = 0;

    private XAxisValueFormatter xAxisFormatter;

    private ArrayList<SensorWithData> sensors;
    private HashMap<String, Integer> nodeIdLocalIdToSensors;
    private HashSet<Integer> parameters;
    private HashMap<Integer, ArrayList<Integer>> parametersnumber;
    private HashSet<Integer> nodesId;

    private ArrayList<ZoomAndMove> zoomAndMoves;
    private long maxDay;
    private long minDay;

    private ArrayList<LineData> list;

    private DateFormat dateFormatMonth;
    private DateFormat dateFormatWeek;
    private DateFormat dateFormatDay;
    private Date date;

    private int aggregate;

    //Zmienne w funkcji agregacyjnej
    private float count = 0;
    private float sum = -1;
    private int previous = -1;
    private boolean firstcycle = true;

    private int colors[] = new int[15];
    private int colornumber;

    private TimingLogger timings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_final_activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setTitle("Wykresy");

        timings = new TimingLogger("MyTag", "CalaAplikacja");
        timings.reset();

        Utils.init(getApplicationContext());

        colors[0] = Color.rgb(233, 110, 126);
        colors[1] = Color.rgb(108, 222, 140);
        colors[2] = Color.rgb(0, 151, 0);
        colors[3] = Color.rgb(0, 0, 203);
        colors[4] = Color.rgb(0, 197, 197);
        colors[5] = Color.rgb(158, 157, 242);
        colors[6] = Color.rgb(158, 157, 242);
        colors[7] = Color.rgb(219, 157, 242);
        colors[8] = Color.rgb(219, 225, 153);
        colors[9] = Color.rgb(219, 144, 53);
        colors[10] = Color.rgb(130, 158, 155);
        colors[11] = Color.rgb(27, 195, 213);
        colors[12] = Color.rgb(237, 195, 161);
        colors[13] = Color.rgb(237, 238, 89);
        colors[14] = Color.rgb(117, 79, 15);


        // Przypisanie zmiennej do listy wykresow
        lv = findViewById(R.id.listView1);

        //Utworzenie formatow dat
        dateFormatMonth = new SimpleDateFormat("M", Locale.ENGLISH);
        dateFormatWeek = new SimpleDateFormat("W", Locale.ENGLISH);
        dateFormatDay = new SimpleDateFormat("D", Locale.ENGLISH);
        date = new Date();


        //Przygotowanie bibliotek do polaczenia z endpointem
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        //Stworzenie pliku na cache
        File httpCacheDirectory = new File(getCacheDir(), "offlineCache");
        Cache cache = new Cache(httpCacheDirectory, 10 * 1024 * 1024);


        OkHttpClient httpClient = new OkHttpClient.Builder()
                .cache(cache)                                    //Cachowanie
                .addInterceptor(httpLoggingInterceptor)
                .addNetworkInterceptor(provideCacheInterceptor())
                .addInterceptor(provideOfflineCacheInterceptor())
                .build();


        retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .client(httpClient)
                .baseUrl("http://pluton.kt.agh.edu.pl")
                .build();

        // Tworzymy klienta
        service = retrofit.create(MyWebService.class);

        //Pobieramy dane z poprzedniej aktywnosci i przypisujemy je do zmiennych
        Intent intent = getIntent();
        ArrayList<NodeSubgroup> receivedSensors = intent.getParcelableArrayListExtra("IntentNodeSubgroups");

        maxDay = intent.getLongExtra("MaxDay", -1);
        minDay = intent.getLongExtra("MinDay", -1);
        aggregate = intent.getIntExtra("Aggregation", 0);


        //Tworzenie zmiennych potrzebnych pozniej
        nodeIdLocalIdToSensors = new HashMap<>();
        parameters = new HashSet<>();
        parametersnumber = new HashMap<>();
        nodesId = new HashSet<>();
        sensors = new ArrayList<>();
        xAxisFormatter = new XAxisValueFormatter(referenceTimestamp, aggregate);

        for (int i = 0; i < receivedSensors.size(); i++) {
            sensors.add(new SensorWithData(receivedSensors.get(i)));
            nodeIdLocalIdToSensors.put(String.valueOf(receivedSensors.get(i).getNodeId()) + String.valueOf(receivedSensors.get(i).getLocalId()), i);
            parameters.add(receivedSensors.get(i).getParameterId());
            nodesId.add(receivedSensors.get(i).getNodeId());
        }

        for (Integer p : parameters) {
            ArrayList<Integer> arrayList = new ArrayList<>();
            parametersnumber.put(p, arrayList);
        }
        for (int i = 0; i < sensors.size(); i++) {
            parametersnumber.get(sensors.get(i).getNodeSubgroup().getParameterId()).add(i);
        }


        //Okno ladowania
        processDialog = new ProgressDialog(FinalActivity.this);
        processDialog.setMessage("Trwa pobieranie danych ...");
        processDialog.setCancelable(false);
        processDialog.show();


        //Tworzymy liste requestow
        final List<Observable<PojoData>> requests = new ArrayList<>();

        //Tworzymy dynamicznie zapytania Retrofit i dodajemy je do listy
        Iterator iter = nodesId.iterator();
        while (iter.hasNext()) {
            int nodeNumber = (int) iter.next();
            requests.add(service.getData("data", String.valueOf(nodeNumber), String.valueOf(minDay) + "," + String.valueOf(maxDay)));
        }

        //Zebranie wynikow
        final Observable<PojoAllSecondActivity> combined = Observable.zip(requests, new Function<Object[], PojoAllSecondActivity>() {
            @Override
            public PojoAllSecondActivity apply(Object[] objects) throws Exception {

                ArrayList<PojoData> pojoDataArrayList = new ArrayList<>();
                for (Object o : objects) {
                    pojoDataArrayList.add((PojoData) o);

                }

                return new PojoAllSecondActivity(pojoDataArrayList);
            }
        });

        // Wykorzystanie wynikow
        Consumer<PojoAllSecondActivity> subscriber = new Consumer<PojoAllSecondActivity>() {
            @Override
            public void accept(PojoAllSecondActivity pojoAllSecondActivity) throws Exception {

                Iterator iter = nodesId.iterator();
                //Zapisanie danych do wykresow
                for (PojoData p : pojoAllSecondActivity.getPojoDataList()) {
                    int nodeNumber = (int) iter.next();
                    for (DataOutput d : p.getDataOutputs()) {
                        for (Datum datum : d.getData()) {
                            if (nodeIdLocalIdToSensors.containsKey(String.valueOf(nodeNumber) + datum.getLocalId())) {
                                sensors.get(nodeIdLocalIdToSensors.get(String.valueOf(nodeNumber) + datum.getLocalId())).addValue(datum.getValue());
                                sensors.get(nodeIdLocalIdToSensors.get(String.valueOf(nodeNumber) + datum.getLocalId())).addTimestamp(Long.valueOf(d.getCreatedat()));
                            }
                        }

                    }

                }


                list = new ArrayList<>();
                ArrayList<ArrayList<ILineDataSet>> sets = new ArrayList<>();
                //Tworzenie tylu wykresow ile parametrow
                for (Integer p : parameters) {
                    ArrayList<ILineDataSet> arrayList = new ArrayList<>();
                    sets.add(arrayList);
                }

                timings.addSplit("startaggreg ");
                //Agregacja danych
                switch (aggregate) {
                    case 0:
                        aggregate(dateFormatDay);
                        break;

                    case 1:
                        aggregate(dateFormatWeek);
                        break;

                    case 2:
                        aggregate(dateFormatMonth);
                        break;

                    case 3:
                        aggregate(dateFormatDay);
                        break;
                }
                timings.addSplit("pobieranieend ");

                //Utworzenie zestawow danych do wykresow z pobranych danych
                Iterator it = parameters.iterator();
                if (aggregate == 0) {
                    for (int i = 0; i < parameters.size(); i++) {

                        colornumber = -1;

                        if (it.hasNext()) {
                            for (Integer s : parametersnumber.get(it.next())) {
                                generateDataLine(sensors.get(s).getValues(), sensors.get(s).getTimestamps(), sensors.get(s).getNodeSubgroup().getDescription(), sets.get(i));

                            }
                        }
                        list.add(new LineData(sets.get(i)));

                    }
                } else {
                    for (int i = 0; i < parameters.size(); i++) {

                        colornumber = -1;

                        if (it.hasNext()) {
                            for (Integer s : parametersnumber.get(it.next())) {
                                generateDataLine(sensors.get(s).getAggregatedValues(), sensors.get(s).getAggregatedTimestamps(), sensors.get(s).getNodeSubgroup().getDescription(), sets.get(i));

                            }
                        }
                        list.add(new LineData(sets.get(i)));
                    }
                }


                //Zmienne zapamietujace przyblizenie i przesuniecie wykresow
                zoomAndMoves = new ArrayList<>();
                for (LineData l : list) {
                    ZoomAndMove zoomAndMove = new ZoomAndMove();
                    zoomAndMoves.add(zoomAndMove);
                }



                //Adapter do listy i utworzenie listy wykresow
                ChartDataAdapter cda = new ChartDataAdapter(getApplicationContext(), list);
                lv.setAdapter(cda);

                //Koniec liczenia czasu wykonania
                timings.addSplit("end ");
                timings.dumpToLog();

                //Usuniecie okna ladowania
                processDialog.dismiss();

            }
        };


        //subscribe()
        combined.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);


    }


    //Adapter wykresow do listy wykresow
    private class ChartDataAdapter extends ArrayAdapter<LineData> {

        ChartDataAdapter(Context context, List<LineData> objects) {
            super(context, 0, objects);
        }

        @SuppressLint("InflateParams")

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            Typeface tfLight = Typeface.createFromAsset(getApplicationContext().getAssets(), "OpenSans-Light.ttf");
            Typeface tfRegular = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
            LineData data = getItem(position);

            final ViewHolder holder;

            if (convertView == null) {

                holder = new ViewHolder();

                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_linechart, null);
                holder.chart = convertView.findViewById(R.id.chart);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            //Rozne opcje wykresow
            if (data != null) {
                data.setValueTypeface(tfLight);
                data.setValueTextColor(Color.BLACK);
            }
            holder.chart.getDescription().setEnabled(false);
            holder.chart.setDrawGridBackground(false);
            holder.chart.fitScreen();
            XAxis xAxis = holder.chart.getXAxis();
            xAxis.setPosition(XAxisPosition.BOTTOM);
            xAxis.setTypeface(tfLight);
            xAxis.setDrawGridLines(false);

            xAxis.setLabelCount(5, true);


            YAxis leftAxis = holder.chart.getAxisLeft();
            leftAxis.setTypeface(tfLight);
            leftAxis.setLabelCount(5, false);
            leftAxis.setSpaceTop(0.5f);

            YAxis rightAxis = holder.chart.getAxisRight();
            rightAxis.setTypeface(tfLight);
            rightAxis.setLabelCount(5, false);
            rightAxis.setSpaceTop(0.5f);

            if (data.getDataSetByIndex(0).getLabel().contains("PM10")) {
                LimitLine ll1 = new LimitLine(50f, "Norma dobowa");
                ll1.setLineWidth(4f);
                ll1.enableDashedLine(10f, 10f, 0f);
                ll1.setLabelPosition(LimitLabelPosition.RIGHT_TOP);
                ll1.setTextSize(10f);
                ll1.setTypeface(tfRegular);
                leftAxis.addLimitLine(ll1);
            }


            //Ustawianie zapamietanego przyblizenia i przesuniecia
            holder.chart.zoom(zoomAndMoves.get(position).getZoomX(), zoomAndMoves.get(position).getZoomY(), 0, 0);
            holder.chart.moveViewTo(zoomAndMoves.get(position).getMoveX(), zoomAndMoves.get(position).getMoveY(), YAxis.AxisDependency.LEFT);


            holder.chart.setData(data);
            holder.chart.invalidate();
            holder.chart.setTouchEnabled(true);
            holder.chart.setDragEnabled(true);
            holder.chart.setScaleEnabled(true);
            holder.chart.setScaleXEnabled(true);
            holder.chart.setScaleYEnabled(true);
            holder.chart.setDrawMarkers(false);

            //Zachowanie przy zaznaczeniu punktu
            holder.chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(Entry e, Highlight h) {
                    holder.chart.setDrawMarkers(true);
                }

                @Override
                public void onNothingSelected() {

                }
            });

            //Zapisanie przyblizenia i przesuniecia
            holder.chart.setOnChartGestureListener(new OnChartGestureListener() {
                @Override
                public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

                }

                @Override
                public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
                    zoomAndMoves.get(position).setMoveX(holder.chart.getLowestVisibleX());
                    zoomAndMoves.get(position).setZoomX(holder.chart.getScaleX());
                    zoomAndMoves.get(position).setZoomY(holder.chart.getScaleY());
                    ViewPortHandler handler = holder.chart.getViewPortHandler();
                    MPPointD topLeft = holder.chart.getValuesByTouchPoint(handler.contentLeft(), handler.contentTop(), YAxis.AxisDependency.LEFT);
                    MPPointD bottomRight = holder.chart.getValuesByTouchPoint(handler.contentRight(), handler.contentBottom(), YAxis.AxisDependency.LEFT);
                    float centerY = (float) ((topLeft.y + bottomRight.y) / 2);
                    zoomAndMoves.get(position).setMoveY(centerY);
                    Log.d(TAG, "onChartGestureEnd: " + topLeft.y + " " + bottomRight.y + " " + zoomAndMoves.get(position).getMoveY() + " " + centerY);
                }

                @Override
                public void onChartLongPressed(MotionEvent me) {

                }

                @Override
                public void onChartDoubleTapped(MotionEvent me) {

                }

                @Override
                public void onChartSingleTapped(MotionEvent me) {

                }

                @Override
                public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

                }

                @Override
                public void onChartScale(MotionEvent me, float scaleX, float scaleY) {


                }

                @Override
                public void onChartTranslate(MotionEvent me, float dX, float dY) {


                }
            });

            //Ustawienie zakresu widoku wykresu
            long range = 0;
            if (aggregate == 0) {
                for (SensorWithData s : sensors) {
                    if (s.getTimestamps().size() >= 100) {
                        long temprange = s.getTimestamps().get(99) - s.getTimestamps().get(0);
                        if (temprange > range) {
                            range = temprange;
                        }
                    }
                }
                if (range != 0) {
                    holder.chart.setVisibleXRangeMaximum(range);
                }
            } else {
                for (SensorWithData s : sensors) {
                    if (s.getAggregatedTimestamps().size() >= 100) {
                        long temprange = s.getAggregatedTimestamps().get(99) - s.getAggregatedTimestamps().get(0);
                        if (temprange > range) {
                            range = temprange;
                        }
                    }
                }
                if (range != 0) {
                    holder.chart.setVisibleXRangeMaximum(range);
                }
            }


            xAxis.setValueFormatter(xAxisFormatter);


            MyMarkerView myMarkerView = new MyMarkerView(getApplicationContext(), R.layout.custom_marker_view, referenceTimestamp, aggregate);
            holder.chart.setMarkerView(myMarkerView);
            holder.chart.setHighlightPerDragEnabled(true);
            holder.chart.getLegend().setWordWrapEnabled(true);
            holder.chart.invalidate();

            return convertView;
        }

        private class ViewHolder {

            LineChart chart;
        }
    }


    //Stworzenie zestawu danych z podanych wartosci


    private void generateDataLine(ArrayList<Float> values, ArrayList<Long> timestamps, String dataSetName, ArrayList<ILineDataSet> set) {

        ArrayList<Entry> values1 = new ArrayList<>();
        int mycolor;
        colornumber++;

        for (int i = 0; i < values.size(); i++) {
            Entry entry = new Entry(timestamps.get(i), values.get(i));
            values1.add(entry);


        }
        Collections.sort(values1, new EntryXComparator());
        LineDataSet d1 = new LineDataSet(values1, dataSetName);
        d1.setLineWidth(2.5f);
        d1.setCircleRadius(4.5f);
        if (colornumber <= 14) {
            mycolor = colors[colornumber];
        } else {
            mycolor = Color.rgb(randomWithRange(1, 254), randomWithRange(1, 254), randomWithRange(1, 254));
        }
        d1.setColor(mycolor);
        d1.setCircleColor(mycolor);
        d1.setDrawValues(false);


        set.add(d1);
    }

    //Czesc do rysowania wykresow kolumnowych. !!!NIEEUZYWANA!!!
//    private BarData generateBarData(ArrayList<Float> values, ArrayList<Long> timestamps, String dataSetName) {
//
//        ArrayList<BarEntry> values1 = new ArrayList<>();
//        int mycolor;
//        colornumber++;
//        DateFormat dateFormatHour = new SimpleDateFormat("HH", Locale.ENGLISH);
//        DateFormat dateFormatMinute = new SimpleDateFormat("mm", Locale.ENGLISH);
//        for (int i = 0; i < values.size(); i++) {
//            BarEntry entry = new BarEntry(timestamps.get(i) - (Long.valueOf(getTimedate(timestamps.get(i), dateFormatHour)) * 60 * 60) - (Long.valueOf(getTimedate(timestamps.get(i), dateFormatMinute)) * 60) + 43200, values.get(i));
//            values1.add(entry);
//
//
//        }
//
//        BarDataSet set1 = new BarDataSet(values1, dataSetName + " (kolumnowy)");
//        if (colornumber <= 14) {
//            mycolor = colors[colornumber];
//        } else {
//            mycolor = Color.rgb(randomWithRange(1, 254), randomWithRange(1, 254), randomWithRange(1, 254));
//        }
//        set1.setColor(mycolor);
//        set1.setValueTextColor(Color.rgb(60, 220, 78));
//        set1.setValueTextSize(10f);
//        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
//
//
//        float groupSpace = 0.06f;
//        float barSpace = 0.02f; // x2 dataset
//        float barWidth = 80000f; // x2 dataset
//        // (0.45 + 0.02) * 2 + 0.06 = 1.00 -> interval per "group"
//        // make this BarData object grouped
//        BarData d = new BarData(set1);
//
//        d.setBarWidth(barWidth);
//
//
//        return d;
//    }


    //Funkcja losujaca
    int randomWithRange(int min, int max) {
        int range = (max - min) + 1;
        return (int) (Math.random() * range) + min;
    }


    //Zmiana znaczinka czasowego na format daty
    private String getTimedate(long timestamp, DateFormat dateFormat) {

        try {
            date.setTime(timestamp * 1000);
            String check = dateFormat.format(date);
            //Log.d(TAG, "getTimedate: " + check);
            return check;
        } catch (Exception ex) {
            return "xx";
        }
    }

    //Funkcja aggregujaca
    public void aggregate(DateFormat dateFormat) {

        for (SensorWithData s : sensors) {
            for (int i = 0; i < s.getTimestamps().size(); i++) {
                if (Integer.valueOf(getTimedate(s.getTimestamps().get(i), dateFormat)) != previous) {
                    //   Log.d(TAG, "Added timestamp in : "+ i);
                    s.getAggregatedTimestamps().add(s.getTimestamps().get(i));
                    previous = Integer.valueOf(getTimedate(s.getTimestamps().get(i), dateFormat));
                    if (firstcycle) {
                        sum = 0;
                        count = 0;
                        firstcycle = false;
                    } else {
                        s.getAggregatedValues().add(sum / count);
                        // Log.d(TAG, "Added value in : "+ i);
                        sum = 0;
                        count = 0;
                    }
                    sum += s.getValues().get(i);
                    count++;
                } else {
                    sum += s.getValues().get(i);
                    count++;
                }
            }

            s.getAggregatedValues().add(sum / count);
            firstcycle = true;
            previous = -1;
            //  Log.d(TAG, "Added timestamp on end : " );


        }

    }

    //Tworzenie Interceptora potrzebnego do cachowania
    private Interceptor provideCacheInterceptor() {

        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Response originalResponse = chain.proceed(request);
                String cacheControl = originalResponse.header("Cache-Control");

                if (cacheControl == null || cacheControl.contains("no-store") || cacheControl.contains("no-cache") ||
                        cacheControl.contains("must-revalidate") || cacheControl.contains("max-stale=0")) {


                    CacheControl cc = new CacheControl.Builder()
                            .maxStale(1, TimeUnit.DAYS)
                            .build();


                    request = request.newBuilder()
                            .cacheControl(cc)
                            .build();

                    return originalResponse.newBuilder()
                            .removeHeader("Pragma")
                            .header("Cache-Control", "public, max-age=" + 5000)
                            .build();

                } else {
                    return originalResponse;
                }
            }
        };

    }

    //Drugi interceptor
    private Interceptor provideOfflineCacheInterceptor() {

        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                try {
                    return chain.proceed(chain.request());
                } catch (Exception e) {


                    CacheControl cacheControl = new CacheControl.Builder()
                            .onlyIfCached()
                            .maxStale(1, TimeUnit.DAYS)
                            .build();

                    Request offlineRequest = chain.request().newBuilder()
                            .cacheControl(cacheControl)
                            .build();
                    return chain.proceed(offlineRequest);
                }
            }
        };
    }


}

