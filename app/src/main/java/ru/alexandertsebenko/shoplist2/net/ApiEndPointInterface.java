package ru.alexandertsebenko.shoplist2.net;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import ru.alexandertsebenko.shoplist2.datamodel.PeoplePleaseBuy;
import ru.alexandertsebenko.shoplist2.datamodel.Ppb;

public interface ApiEndpointInterface {
    // Request method and URL specified in the annotation
    // Callback for the parsed response is the last parameter

    @GET("ppb/{number}")
    Call<PeoplePleaseBuy> getPeoplePleaseBuy(@Path("number") String number);

    @POST("ppb/")
    Call<PeoplePleaseBuy> createPeoplePleaseBuy(@Body PeoplePleaseBuy user);
}
