package com.amme.aldunyaeyh

import retrofit2.Call
import retrofit2.http.GET

interface NewsCallable {
    @GET("/v2/top-headlines?category=general&apiKey=API_KEY&pageSize=75")
    fun getGeneralNews(): Call<News>

    @GET("/v2/top-headlines?category=sports&apiKey=API_KEY&pageSize=75")
    fun getSportsNews(): Call<News>
}