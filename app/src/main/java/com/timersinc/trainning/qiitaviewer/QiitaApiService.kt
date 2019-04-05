package com.timersinc.trainning.qiitaviewer

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface QiitaApiService {
    @GET("api/v2/items")
    fun getItems(@Query("page") page: Int): Call<List<Item>>
}
