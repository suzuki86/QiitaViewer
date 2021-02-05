package com.timersinc.trainning.qiitaviewer

import android.arch.lifecycle.MutableLiveData
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.launch
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val qiitaToken = ""
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val newReq = chain.request()
                    .newBuilder()
                    .addHeader("Authorization", "Bearer $qiitaToken")
                    .build()
                return@addInterceptor chain.proceed(newReq)
            }
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://qiita.com/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        val api = retrofit.create(QiitaApiService::class.java)

        api.getItems(1).enqueue(object : Callback<List<Item>> {
            override fun onFailure(call: Call<List<Item>>, t: Throwable) {
                t.printStackTrace()
            }

            override fun onResponse(call: Call<List<Item>>, response: Response<List<Item>>) {
                val items: List<Item>? = response.body()
                items?.map { item ->
                    Log.d("test", item.title)
                }
            }
        })

        val items = MutableLiveData<List<Item>>()

        GlobalScope.launch(Dispatchers.Default) {
            try {
                val response = api.getItems(1).execute()
                items.postValue(response.body())
            } catch (e: Throwable) {
                Timber.e(e)
            }
        }
    }

    interface QiitaApiService {
        @GET("/api/v2/items")
        fun getItems(@Query("page") page: Int): Call<List<Item>>
    }

    @JsonClass(generateAdapter = true)
    data class Item(
        @Json(name = "title")
        val title: String,
        @Json(name = "url")
        val url: String,
        @Json(name = "user")
        val user: User
    )

    @JsonClass(generateAdapter = true)
    data class User(
        @Json(name = "description")
        val description: String?,
        @Json(name = "id")
        val id: String,
        @Json(name = "profile_image_url")
        val imageUrl: String
    )
}
