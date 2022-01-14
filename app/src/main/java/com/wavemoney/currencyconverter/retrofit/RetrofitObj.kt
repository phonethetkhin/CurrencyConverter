package com.wavemoney.currencyconverter.retrofit

import com.wavemoney.currencyconverter.common.BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitObj {
    val API_SERVICE: APIService = Retrofit.Builder().baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(OkHttpClient())
        .build()
        .create(APIService::class.java)
}



