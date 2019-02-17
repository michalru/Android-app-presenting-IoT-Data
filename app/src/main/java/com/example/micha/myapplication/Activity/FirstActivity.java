package com.example.micha.myapplication.Activity;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.Button;

import android.widget.ExpandableListView;
import android.widget.Toast;


import com.example.micha.myapplication.Model.ConstantManager;
import com.example.micha.myapplication.Model.DataItem;
import com.example.micha.myapplication.Model.LocalPositGroup;
import com.example.micha.myapplication.Model.NodeSubgroup;
import com.example.micha.myapplication.Model.ParamIdName;
import com.example.micha.myapplication.Model.SubCategoryItem;
import com.example.micha.myapplication.Others.MyCategoriesExpandableListAdapter;
import com.example.micha.myapplication.Others.MyWebService;
import com.example.micha.myapplication.Pojo.GroupOutput;
import com.example.micha.myapplication.Pojo.ParameterOutput;
import com.example.micha.myapplication.Pojo.PojoAllFirstActivity;
import com.example.micha.myapplication.Pojo.PojoGroup;
import com.example.micha.myapplication.Pojo.PojoParameter;
import com.example.micha.myapplication.Pojo.PojoSubgroup;
import com.example.micha.myapplication.Pojo.PojoTime;
import com.example.micha.myapplication.Pojo.SubgroupOutput;
import com.example.micha.myapplication.R;

import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function4;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

//Aktywnosc 1
public class FirstActivity extends AppCompatActivity {

    private static final String TAG = "FirstActivity";

    private Retrofit retrofit;
    private MyWebService service;

    private Button btn;
    private ExpandableListView lvCategory;
    private MyCategoriesExpandableListAdapter myCategoriesExpandableListAdapter;

    private ArrayList<LocalPositGroup> localPositGroups;
    private ArrayList<NodeSubgroup> nodeSubgroups;
    private ArrayList<ArrayList<Integer>> groupSubgroup;
    private ArrayList<ParamIdName> paramIdNames = new ArrayList<>();

    private ArrayList<DataItem> arCategory;
    private ArrayList<SubCategoryItem> arSubCategory;

    private ArrayList<HashMap<String, String>> parentItems;
    private ArrayList<ArrayList<HashMap<String, String>>> childItems;
    private ArrayList<ArrayList<HashMap<String, String>>> childItemsclone;


    private long intentMinDay;
    private long intentMaxDay;
    private ArrayList<NodeSubgroup> intentNodeSubgroups;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setTitle("Wybierz czujniki");

        //Przypisywanie od zmiennych widokow z layoutu
        lvCategory = findViewById(R.id.lvCategory);
        btn = findViewById(R.id.btn);

        //Przygotowanie bibliotek do polaczenia z endpointem
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClientBuilder.addInterceptor(logging);

        retrofit = new Retrofit.Builder()
                // Adres API
                .baseUrl("http://pluton.kt.agh.edu.pl")
                .client(okHttpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        // Tworzymy klienta
        service = retrofit.create(MyWebService.class);

        //Przygotowanie requestow
        Observable<PojoGroup> observablePojoGroup = service.getGroup();
        Observable<PojoSubgroup> observablePojoSubgroup = service.getSubgroup();
        Observable<PojoTime> observablePojoTime = service.getTime();
        Observable<PojoParameter> observablePojoParameter = service.getParameter();

        //Zebranie wynikow
        Observable<PojoAllFirstActivity> combined = Observable.zip(observablePojoGroup, observablePojoSubgroup, observablePojoTime, observablePojoParameter, new Function4<PojoGroup, PojoSubgroup, PojoTime, PojoParameter, PojoAllFirstActivity>() {
            @Override
            public PojoAllFirstActivity apply(PojoGroup pojoGroup, PojoSubgroup pojoSubgroup, PojoTime pojoTime, PojoParameter pojoParameter) throws Exception {
                return new PojoAllFirstActivity(pojoGroup, pojoSubgroup, pojoTime, pojoParameter);
            }
        });
        // Przypisanie wynikow do wlasciwych zmiennych
        Consumer<PojoAllFirstActivity> subscriber = new Consumer<PojoAllFirstActivity>() {
            @Override
            public void accept(PojoAllFirstActivity pojoAllFirstActivity) throws Exception {

                //Przypisywanie do zmiennych grup
                localPositGroups = new ArrayList<>();
                for (GroupOutput g : pojoAllFirstActivity.getPojoGroup().getGroupOutput()) {
                    localPositGroups.add(new LocalPositGroup(g.getLoc() + "-" + g.getPos(), Integer.valueOf(g.getId())));
                }

                //Przypisywanie do zmiennych podgrup
                nodeSubgroups = new ArrayList<>();
                for (SubgroupOutput s : pojoAllFirstActivity.getPojoSubgroup().getSubgroupOutputs()) {
                    nodeSubgroups.add(new NodeSubgroup(Integer.valueOf(s.getId()), Integer.valueOf(s.getPositionid()), Integer.valueOf(s.getNodeid()), Integer.valueOf(s.getParameterid()), Integer.valueOf(s.getLocalid()), s.getDescription()));
                }

                //Przypisywanie do zmiennych czasu
                intentMinDay = Long.valueOf(pojoAllFirstActivity.getPojoTime().getTimeOutputs().get(0).getMin());
                intentMaxDay = Long.valueOf(pojoAllFirstActivity.getPojoTime().getTimeOutputs().get(0).getMax());


                //Przypisywanie do zmiennych parametrow
                for (ParameterOutput p : pojoAllFirstActivity.getPojoParameter().getParameterOutputs()) {
                    paramIdNames.add(new ParamIdName(Integer.valueOf(p.getId()), p.getName()));
                }

                invalidateOptionsMenu();
                setupReferences();
            }
        };


        combined.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

        //Po kliknieciu przycisku zbierane sa dane do przeslania do nastepnej aktywnosci i nastepuje przejscie do niej
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Sprawdzanie ktore sensory sa zaznaczone i zapisanie ich do Arraylist
                Log.d(TAG, "Parent items: ");
                intentNodeSubgroups = new ArrayList<>();
                for (int i = 0; i < ConstantManager.parentItems.size(); i++) {
                    for (int j = 0; j < ConstantManager.childItems.get(i).size(); j++) {
                        if (ConstantManager.childItems.get(i).get(j).get(ConstantManager.Parameter.IS_CHECKED).equals(ConstantManager.CHECK_BOX_CHECKED_TRUE)) {
                            for (NodeSubgroup n : nodeSubgroups) {
                                if (Integer.valueOf(ConstantManager.childItems.get(i).get(j).get(ConstantManager.Parameter.ID)) == n.getId()) {
                                    intentNodeSubgroups.add(n);
                                    break;
                                }
                            }
                        }
                    }

                }



                if (intentNodeSubgroups.size() > 0) {
                    Intent intent = new Intent(FirstActivity.this, SecondActivity.class);
                    intent.putExtra("IntentNodeSubgroups", intentNodeSubgroups);
                    intent.putExtra("MinDay", intentMinDay);
                    intent.putExtra("MaxDay", intentMaxDay);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplication(), "Nie wybrano żadnego sensora",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    //Stworzenie menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        for (int i = 0; i < paramIdNames.size(); i++) {
            menu.add(0, i, 0, paramIdNames.get(i).getName()).setCheckable(true).setChecked(true);
        }
        return true;
    }

    //Dzialanie menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.isChecked()) {
            removeParameter(item.getItemId());
            item.setChecked(false);
        } else {
            addParameter(item.getItemId());
            item.setChecked(true);
        }
        return super.onOptionsItemSelected(item);
    }

    //Utworzenie listy rozwijanej z checkboxami
    private void setupReferences() {


        arCategory = new ArrayList<>();
        arSubCategory = new ArrayList<>();
        parentItems = new ArrayList<>();
        childItems = new ArrayList<>();
        childItemsclone = new ArrayList<>();

        DataItem dataItem;

        groupSubgroup = new ArrayList<>();
        for (LocalPositGroup l : localPositGroups) {
            ArrayList<Integer> arrayList = new ArrayList<>();
            groupSubgroup.add(arrayList);
        }


        for (int i = 0; i < localPositGroups.size(); i++) {
            dataItem = new DataItem();
            dataItem.setCategoryName(localPositGroups.get(i).getName());


            arSubCategory = new ArrayList<>();

            for (int j = 0; j < nodeSubgroups.size(); j++) {
                if (nodeSubgroups.get(j).getPositionId() == localPositGroups.get(i).getIdp()) {
                    for (ParamIdName p : paramIdNames) {
                        if (nodeSubgroups.get(j).getParameterId() == p.getId()) {
                            p.addNodeAdd(i, nodeSubgroups.get(j).getDescription(), String.valueOf(nodeSubgroups.get(j).getId()), String.valueOf(nodeSubgroups.get(j).getParameterId()));

                        }
                    }
                    groupSubgroup.get(i).add(j);
                    SubCategoryItem subCategoryItem = new SubCategoryItem();
                    subCategoryItem.setIdp(Integer.toString(nodeSubgroups.get(j).getParameterId()));
                    subCategoryItem.setIsChecked(ConstantManager.CHECK_BOX_CHECKED_FALSE);
                    subCategoryItem.setSubCategoryName(nodeSubgroups.get(j).getDescription());
                    subCategoryItem.setId(Integer.toString(nodeSubgroups.get(j).getId()));
                    arSubCategory.add(subCategoryItem);
                }
            }
            dataItem.setSubCategory(arSubCategory);
            arCategory.add(dataItem);

        }


        for (DataItem data : arCategory) {

            ArrayList<HashMap<String, String>> childArrayList = new ArrayList<HashMap<String, String>>();
            ArrayList<HashMap<String, String>> childArrayListclone = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> mapParent = new HashMap<String, String>();


            mapParent.put(ConstantManager.Parameter.CATEGORY_NAME, data.getCategoryName());


            int countIsChecked = 0;
            for (SubCategoryItem subCategoryItem : data.getSubCategory()) {

                HashMap<String, String> mapChild = new HashMap<String, String>();
                HashMap<String, String> mapChildclone = new HashMap<String, String>();

                mapChild.put(ConstantManager.Parameter.SUB_CATEGORY_NAME, subCategoryItem.getSubCategoryName());
                mapChild.put(ConstantManager.Parameter.IS_CHECKED, subCategoryItem.getIsChecked());
                mapChild.put(ConstantManager.Parameter.ID, subCategoryItem.getId());
                mapChild.put(ConstantManager.Parameter.IDP, subCategoryItem.getIdp());
                mapChildclone.put(ConstantManager.Parameter.SUB_CATEGORY_NAME, subCategoryItem.getSubCategoryName());
                mapChildclone.put(ConstantManager.Parameter.IS_CHECKED, subCategoryItem.getIsChecked());
                mapChildclone.put(ConstantManager.Parameter.ID, subCategoryItem.getId());
                mapChildclone.put(ConstantManager.Parameter.IDP, subCategoryItem.getIdp());

                if (subCategoryItem.getIsChecked().equalsIgnoreCase(ConstantManager.CHECK_BOX_CHECKED_TRUE)) {

                    countIsChecked++;
                }
                childArrayList.add(mapChild);
                childArrayListclone.add(mapChildclone);
            }

            if (countIsChecked == data.getSubCategory().size()) {

                data.setIsChecked(ConstantManager.CHECK_BOX_CHECKED_TRUE);
            } else {
                data.setIsChecked(ConstantManager.CHECK_BOX_CHECKED_FALSE);
            }

            mapParent.put(ConstantManager.Parameter.IS_CHECKED, data.getIsChecked());
            childItemsclone.add(childArrayListclone);
            childItems.add(childArrayList);
            parentItems.add(mapParent);

        }

        ConstantManager.parentItems = parentItems;
        ConstantManager.childItems = childItems;

        myCategoriesExpandableListAdapter = new MyCategoriesExpandableListAdapter(this, parentItems, childItems, false);
        lvCategory.setAdapter(myCategoriesExpandableListAdapter);
    }

    //Usuwanie sensorow mierzacych dany parametr po odznaczeniu w menu
    void removeParameter(int id) {
        String paramid = String.valueOf(paramIdNames.get(id).getId());
        childItemsclone = deepChildItemsCopy();

        //Usuwanie sensorow z listy
        int i = 0;
        for (ArrayList<HashMap<String, String>> a : childItemsclone) {
            int j = 0;

            for (HashMap<String, String> h : a) {
                if (h.get(ConstantManager.Parameter.IDP) == paramid) {
                    childItems.get(i).remove(j);
                    j--;
                }
                j++;
            }
            i++;
        }

        ConstantManager.parentItems = parentItems;
        ConstantManager.childItems = childItems;

        myCategoriesExpandableListAdapter.notifyDataSetChanged();
        childItemsclone = deepChildItemsCopy();

        //Aktualizacja zaznaczenia w grupie(lokacja-pozycja)
        int count;
        for (int j = 0; j < parentItems.size(); j++) {
            count = 0;
            for (int k = 0; k < childItems.get(j).size(); k++) {
                if (childItems.get(j).get(k).get(ConstantManager.Parameter.IS_CHECKED).equalsIgnoreCase(ConstantManager.CHECK_BOX_CHECKED_TRUE)) {
                    count++;
                }
            }
            if (childItems.get(j).size() != 0 && count == childItems.get(j).size()) {
                parentItems.get(j).put(ConstantManager.Parameter.IS_CHECKED, ConstantManager.CHECK_BOX_CHECKED_TRUE);
                myCategoriesExpandableListAdapter.notifyDataSetChanged();
            } else {
                parentItems.get(j).put(ConstantManager.Parameter.IS_CHECKED, ConstantManager.CHECK_BOX_CHECKED_FALSE);
                myCategoriesExpandableListAdapter.notifyDataSetChanged();
            }
        }
        ConstantManager.parentItems = parentItems;
        ConstantManager.childItems = childItems;

    }

    //Dodawanie sensorow mierzacych dany parametr po zaznaczeniu w menu
    void addParameter(int id) {

        //Dodawanie sensorow do listy
        for (ParamIdName.NodeAdd n : paramIdNames.get(id).getNodeAdds()) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put(ConstantManager.Parameter.SUB_CATEGORY_NAME, n.hashmap.get(ConstantManager.Parameter.SUB_CATEGORY_NAME));
            hashMap.put(ConstantManager.Parameter.IS_CHECKED, n.hashmap.get(ConstantManager.Parameter.IS_CHECKED));
            hashMap.put(ConstantManager.Parameter.ID, n.hashmap.get(ConstantManager.Parameter.ID));
            hashMap.put(ConstantManager.Parameter.IDP, n.hashmap.get(ConstantManager.Parameter.IDP));
            childItems.get(n.parentId).add(hashMap);

        }

        ConstantManager.parentItems = parentItems;
        ConstantManager.childItems = childItems;

        myCategoriesExpandableListAdapter.notifyDataSetChanged();

        //Aktualizacja zaznaczenia w grupie(lokacja-pozycja)
        int count;
        for (int j = 0; j < parentItems.size(); j++) {
            count = 0;
            for (int k = 0; k < childItems.get(j).size(); k++) {
                if (childItems.get(j).get(k).get(ConstantManager.Parameter.IS_CHECKED).equalsIgnoreCase(ConstantManager.CHECK_BOX_CHECKED_TRUE)) {
                    count++;
                }
            }
            if (childItems.get(j).size() != 0 && count == childItems.get(j).size()) {
                parentItems.get(j).put(ConstantManager.Parameter.IS_CHECKED, ConstantManager.CHECK_BOX_CHECKED_TRUE);
                myCategoriesExpandableListAdapter.notifyDataSetChanged();
            } else {
                parentItems.get(j).put(ConstantManager.Parameter.IS_CHECKED, ConstantManager.CHECK_BOX_CHECKED_FALSE);
                myCategoriesExpandableListAdapter.notifyDataSetChanged();
            }
        }

        //Aktualizacja childItemsclone do postaci childItems
        ConstantManager.parentItems = parentItems;
        ConstantManager.childItems = childItems;

        for (ParamIdName.NodeAdd n : paramIdNames.get(id).getNodeAdds()) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put(ConstantManager.Parameter.SUB_CATEGORY_NAME, n.hashmap.get(ConstantManager.Parameter.SUB_CATEGORY_NAME));
            hashMap.put(ConstantManager.Parameter.IS_CHECKED, n.hashmap.get(ConstantManager.Parameter.IS_CHECKED));
            hashMap.put(ConstantManager.Parameter.ID, n.hashmap.get(ConstantManager.Parameter.ID));
            hashMap.put(ConstantManager.Parameter.IDP, n.hashmap.get(ConstantManager.Parameter.IDP));
            childItemsclone.get(n.parentId).add(hashMap);

        }


    }

    //Tworzenie glebokiej kopii ChildItems
    private ArrayList<ArrayList<HashMap<String, String>>> deepChildItemsCopy() {
        ArrayList<ArrayList<HashMap<String, String>>> result = new ArrayList<>();

        int k = 0;
        for (ArrayList<HashMap<String, String>> a : childItems) {
            ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
            result.add(arrayList);
            for (HashMap<String, String> h : a) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(ConstantManager.Parameter.SUB_CATEGORY_NAME, h.get(ConstantManager.Parameter.SUB_CATEGORY_NAME));
                hashMap.put(ConstantManager.Parameter.IS_CHECKED, h.get(ConstantManager.Parameter.IS_CHECKED));
                hashMap.put(ConstantManager.Parameter.ID, h.get(ConstantManager.Parameter.ID));
                hashMap.put(ConstantManager.Parameter.IDP, h.get(ConstantManager.Parameter.IDP));
                result.get(k).add(hashMap);

            }
            k++;
        }
        return result;
    }
}



