package com.example.micha.myapplication.Others;

import com.example.micha.myapplication.Pojo.PojoData;
import com.example.micha.myapplication.Pojo.PojoGroup;
import com.example.micha.myapplication.Pojo.PojoParameter;
import com.example.micha.myapplication.Pojo.PojoSubgroup;
import com.example.micha.myapplication.Pojo.PojoTime;

import io.reactivex.Observable;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
//Endpointy i przypisanie ich do POJO
public interface MyWebService {
    @GET("/~mruminski/RestController.php?view=group") // deklarujemy endpoint oraz metodę
    Observable<PojoGroup> getGroup();

    @GET("/~mruminski/RestController.php?view=subgroup") // deklarujemy endpoint oraz metodę
    Observable<PojoSubgroup> getSubgroup();
    @GET("/~mruminski/RestController.php?view=time") // deklarujemy endpoint oraz metodę
    Observable<PojoTime> getTime();
    @GET("/~mruminski/RestController.php?view=parameters") // deklarujemy endpoint oraz metodę
    Observable<PojoParameter> getParameter();
    @GET("/~mruminski/RestController.php") // deklarujemy endpoint oraz metodę
    Observable<PojoData> getData(@Query("view") String view, @Query("id") String id, @Query("startend") String startend);
}
