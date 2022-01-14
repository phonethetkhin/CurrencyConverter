package com.wavemoney.currencyconverter.repository

import android.content.Context
import android.util.Log
import com.wavemoney.currencyconverter.common.API_KEY
import com.wavemoney.currencyconverter.retrofit.APIService
import com.wavemoney.currencyconverter.roomdb.CurrencyDB
import com.wavemoney.currencyconverter.roomdb.entity.AvailableEntity
import com.wavemoney.currencyconverter.roomdb.entity.LiveRatesEntity
import com.wavemoney.currencyconverter.utility.showToast

class HomeRepository(
    private val context: Context,
    private val apiService: APIService,
    private val currencyDB: CurrencyDB
) {

    suspend fun getAvailableList(): ArrayList<AvailableEntity> {
        val availableList = ArrayList<AvailableEntity>()
        val response = apiService.getAvailableList(API_KEY)
        Log.d("Hello", "API Avaialbe Triggered")

        if (response.isSuccessful) {
            response.body()?.let { responseBody ->
                availableList.addAll(responseBody.currencies.entries.mapIndexed { index, it ->
                    AvailableEntity("${index + 1}. ${it.key} = ${it.value}")
                }.toCollection(ArrayList()))
                if (currencyDB.liveRatesDao().getAllRates().isNotEmpty()) {
                    currencyDB.availableDao().deleteAllAvailable()
                }
                currencyDB.availableDao().insertAllAvailable(availableList)
            }
        } else {
            context.showToast("Some Error Occurred ${response.body()!!.success}")
        }
        return availableList
    }

    suspend fun getAvailableFromDB(): ArrayList<AvailableEntity> {
        Log.d("Hello", "DB Avaialbe Triggered")

        return currencyDB.availableDao().getAllAvailable().toCollection(ArrayList())
    }


    suspend fun getLiveRates(): ArrayList<LiveRatesEntity> {
        Log.d("Hello", "API LiveRates Triggered")

        val ratesList = ArrayList<LiveRatesEntity>()
        val response = apiService.getLiveCurrencies(API_KEY)
        if (response.isSuccessful) {
            response.body()?.let { responseBody ->
                responseBody.quotes.map {
                    ratesList.add(LiveRatesEntity(it.key.drop(3), it.value))
                }
                if (currencyDB.liveRatesDao().getAllRates().isNotEmpty()) {
                    currencyDB.liveRatesDao().deleteAllRates()
                }
                currencyDB.liveRatesDao().insertAllRates(ratesList)
            }
        } else {
            context.showToast("Some Error Occurred ${response.body()!!.success}")
        }
        return ratesList
    }

    suspend fun getLiveRatesFromDB(): ArrayList<LiveRatesEntity> {
        Log.d("Hello", "DB LiveRates Triggered")

        return currencyDB.liveRatesDao().getAllRates().toCollection(ArrayList())
    }

    suspend fun getRateByName(name: String) = currencyDB.liveRatesDao().getRateByName(name)
}