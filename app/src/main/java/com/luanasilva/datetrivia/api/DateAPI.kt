package com.luanasilva.datetrivia.api

import com.luanasilva.datetrivia.model.Date
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface DateAPI {

    //http://numbersapi.com/5/28/date?json
    @GET("{month}/{day}/date?json") //comments?postId=1
    suspend fun recuperarDataQuery(
        @Path("month") month: Int,
        @Path("day") day: Int
    ): Response<Date>
}