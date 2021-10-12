package org.sussanacode.workmanagerapplication.team_wm.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private val myRetrofit by lazy {
        val loggingInterceptor = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://psmobitech.com/nba/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        retrofit   // return value of lambda expression
    }

    val apiService: ApiService by lazy {
        myRetrofit.create(ApiService::class.java)
    }
}