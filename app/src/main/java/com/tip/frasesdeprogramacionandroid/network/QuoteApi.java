package com.tip.frasesdeprogramacionandroid.network;

import com.tip.frasesdeprogramacionandroid.model.Frase;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface QuoteApi {
    @GET("api/random")
    Call<List<Frase>> getRandomQuote();

    @GET("api/quotes")
    Call<List<Frase>> getListQuote();
}
