package com.example.contacts.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface Api {

    String BASE_URL = "https://api.genderize.io/";
    @GET
    Call<Result> getGender(@Url String url);
}
