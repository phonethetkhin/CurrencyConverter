package com.wavemoney.currencyconverter.retrofit

import com.wavemoney.currencyconverter.model.response.AvailableListResponseModel
import com.wavemoney.currencyconverter.model.response.CurrencyResponseModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface APIService {
    @GET("list")
    suspend fun getAvailableList(
        @Query("access_key") apiKey: String,
    ): Response<AvailableListResponseModel>

    @GET("live")
    suspend fun getLiveCurrencies(
        @Query("access_key") apiKey: String,
    ): Response<CurrencyResponseModel>

}
