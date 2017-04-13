package com.act_ex.a1221.serchtesthh;

import android.support.annotation.NonNull;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by brost_host17 on 13.04.2017.
 */

public class ApiFactory {


    @NonNull
    public static ApiInterface getVacanciesService() {
        return getRetrofit().create(ApiInterface.class);
    }

    @NonNull
    private static Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("https://api.hh.ru/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}