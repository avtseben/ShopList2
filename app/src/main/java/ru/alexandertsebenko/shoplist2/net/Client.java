package ru.alexandertsebenko.shoplist2.net;

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
import ru.alexandertsebenko.shoplist2.datamodel.PeoplePleaseBuy;
import ru.alexandertsebenko.shoplist2.datamodel.Pinstance;
import ru.alexandertsebenko.shoplist2.datamodel.Ppb;
import ru.alexandertsebenko.shoplist2.datamodel.ProductInstance;
import ru.alexandertsebenko.shoplist2.ui.fragment.ProductListFragment;

/**
 * Created by avtseben on 23.10.2016.
 */
public class Client  {

    public static final String BASE_URL = "http://yrsoft.cu.cc:8080/";
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

    public void postPpb(){

        Ppb ppb = newPpb();
        Call<Ppb> call = apiService.createPeoplePleaseBuy(ppb);
        call.enqueue(new Callback<Ppb>() {
            @Override
            public void onResponse(Call<Ppb> call, Response<Ppb> response) {
                System.out.println(response.body().getFrom());
            }
            @Override
            public void onFailure(Call<Ppb> call, Throwable t) {
                System.out.println("Fail!!!");
            }
        });
    }
    public void getPpb(){
        Call<Ppb> call = apiService.getPeoplePleaseBuy("9133166336");
        call.enqueue(new Callback<Ppb>() {
            @Override
            public void onResponse(Call<Ppb> call, Response<Ppb> response) {
                System.out.println(response.body().getFrom());
            }

            @Override
            public void onFailure(Call<Ppb> call, Throwable t) {
                System.out.println("Fail!!!");
            }
        });
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
