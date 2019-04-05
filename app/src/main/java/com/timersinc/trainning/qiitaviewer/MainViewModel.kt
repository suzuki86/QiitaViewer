package com.timersinc.trainning.qiitaviewer

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber

class MainViewModel(application: Application) : AndroidViewModel(application) {
    val items = MutableLiveData<List<Item>>()
    fun load() {
        val client = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor())
            .addInterceptor { chain ->
                val newReq = chain.request()
                    .newBuilder()
                    .addHeader("Authorization", "Bearer ${BuildConfig.QIITA_TOKEN}")
                    .build()
                return@addInterceptor chain.proceed(newReq)
            }
            .build()

        // https://qiita.com/api/v2/items?page=1&tag
        val retrofit = Retrofit.Builder()
            .baseUrl("https://qiita.com/")
            .addConverterFactory(MoshiConverterFactory.create())
            .client(client)
            .build()

        val api = retrofit.create(QiitaApiService::class.java)

        GlobalScope.launch(Dispatchers.Default) {
            try {
                val response = api.getItems(1).execute()
                items.postValue(response.body())
            } catch (e: Throwable) {
                Timber.e(e)
            }
        }
    }
}
