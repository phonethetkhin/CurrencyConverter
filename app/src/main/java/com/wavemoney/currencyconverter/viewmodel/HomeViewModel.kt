package com.wavemoney.currencyconverter.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wavemoney.currencyconverter.repository.HomeRepository
import com.wavemoney.currencyconverter.roomdb.entity.AvailableEntity
import com.wavemoney.currencyconverter.roomdb.entity.LiveRatesEntity
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: HomeRepository) : ViewModel() {

    lateinit var availableListLiveData: MutableLiveData<ArrayList<AvailableEntity>>
    lateinit var liveRatesLiveData: MutableLiveData<ArrayList<LiveRatesEntity>>

    fun getAvailableLiveData(): LiveData<ArrayList<AvailableEntity>> {
        availableListLiveData = MutableLiveData()
        getAvailableList()
        return availableListLiveData
    }

    fun getAvailableList() = viewModelScope.launch {
        availableListLiveData.postValue(repository.getAvailableList())
    }

    fun getLiveRatesLiveData(): LiveData<ArrayList<LiveRatesEntity>> {
        liveRatesLiveData = MutableLiveData()
        getLiveRates()
        return liveRatesLiveData
    }

    fun getLiveRates() = viewModelScope.launch {
        liveRatesLiveData.postValue(repository.getLiveRates())
    }

    suspend fun getAvailableFromDB() = repository.getAvailableFromDB()


    suspend fun getLiveRatesFromDB() = repository.getLiveRatesFromDB()

    suspend fun getRateByName(name: String) = repository.getRateByName(name)
}