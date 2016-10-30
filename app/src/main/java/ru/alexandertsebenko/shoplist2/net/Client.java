package ru.alexandertsebenko.shoplist2.net;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.TaskStackBuilder;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ru.alexandertsebenko.shoplist2.R;
import ru.alexandertsebenko.shoplist2.datamodel.People;
import ru.alexandertsebenko.shoplist2.datamodel.PeoplePleaseBuy;
import ru.alexandertsebenko.shoplist2.datamodel.Pinstance;
import ru.alexandertsebenko.shoplist2.datamodel.Ppb;
import ru.alexandertsebenko.shoplist2.datamodel.ProductInstance;
import ru.alexandertsebenko.shoplist2.db.DataSource;
import ru.alexandertsebenko.shoplist2.ui.activity.ShopListActivity;
import ru.alexandertsebenko.shoplist2.ui.fragment.ProductListFragment;
import ru.alexandertsebenko.shoplist2.utils.MyApplication;

/**
 * Created by avtseben on 23.10.2016.
 */
public class Client {

    public static final String BASE_URL = "http://yrsoft.cu.cc:8080/";
    public static final int NOTIFICATION_ID = 5453;
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    ApiEndpointInterface apiService = retrofit.create(ApiEndpointInterface.class);

    private Ppb newPpb() {
        String from = "9133166336";
        List<String> to = Arrays.asList("9139209220", "9161112020");
        List pil = new ArrayList<Pinstance>();
        pil.add(new Pinstance("2342mlkmdc2", "Картошка", 1.5f, "кг"));
        pil.add(new Pinstance("23d2mlkgdc2", "Сметана", 2.0f, "пачка"));
        pil.add(new Pinstance("2342mlkfdc2", "Хлеб Бородинский", 1.0f, "булка"));

        Ppb ppb = new Ppb(from, to, pil);
        return ppb;
    }

    public void postPpb(PeoplePleaseBuy ppb) {
        Call<PeoplePleaseBuy> call = apiService.createPeoplePleaseBuy(ppb);
        call.enqueue(new Callback<PeoplePleaseBuy>() {
            @Override
            public void onResponse(Call<PeoplePleaseBuy> call, Response<PeoplePleaseBuy> response) {
                System.out.println("Async post" + response.body().getPil().get(0).getProduct());
            }

            @Override
            public void onFailure(Call<PeoplePleaseBuy> call, Throwable t) {
                System.out.println("Fail!!!");
            }
        });
    }

    public void getPpb(final String number) {
        Call<PeoplePleaseBuy> call = apiService.getPeoplePleaseBuy(number);
        call.enqueue(new Callback<PeoplePleaseBuy>() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onResponse(Call<PeoplePleaseBuy> call, Response<PeoplePleaseBuy> response) {
                if (response.body() != null) {
                    System.out.println("In Async catch ppb");
                    System.out.println(response.body().getFromWho());
                    catchListFromCloud(response.body());
                }
            }

            @Override
            public void onFailure(Call<PeoplePleaseBuy> call, Throwable t) {
                System.out.println("Fail!!!");
            }
        });
    }

    private void catchListFromCloud(PeoplePleaseBuy ppb) {
        Toast.makeText(MyApplication.getAppContext(), "Client gets list from:" + ppb.getFromWho(), Toast.LENGTH_SHORT).show();
        DataSource dataSource = new DataSource(MyApplication.getAppContext());
        dataSource.open();
        dataSource.saveNewListFromPpb(ppb);
    }
}
/*
{ "from" : "9133166336",
  "to" :    [
            "9139209220",
            "9139209221",
            "9139209222"
            ],
  "pil" :   [
            {"globalId" : "32342dewd3423dwdee32e",
             "product"  : "Картошка",
             "quantity" : 1.5,
             "measure"  : "кг"},
            {"globalId" : "32343dewd3423dwdee32e",
             "product"  : "Сметана",
             "quantity" : 1.0,
             "measure"  : "пачка"},
            {"globalId" : "32342fdwd3423dwdee32e",
             "product"  : "Хлеб Бородинский",
             "quantity" : 2.0,
             "measure"  : "булка"}
            ]
}
 */
