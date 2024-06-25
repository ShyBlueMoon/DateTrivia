package com.luanasilva.datetrivia.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitHelper {

    companion object  {
        val retrofitDateTrivia = Retrofit.Builder()
            .baseUrl("http://numbersapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}