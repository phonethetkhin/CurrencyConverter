package com.wavemoney.currencyconverter.application

import android.app.Application
import com.wavemoney.currencyconverter.repository.HomeRepository
import com.wavemoney.currencyconverter.retrofit.RetrofitObj
import com.wavemoney.currencyconverter.roomdb.CurrencyDB
import com.wavemoney.currencyconverter.utility.ViewModelFactory
import com.wavemoney.currencyconverter.viewmodel.HomeViewModel
import org.kodein.di.*
import org.kodein.di.android.x.androidXModule

class CurrencyConverterApplication : Application(), DIAware {

    override val di by DI.lazy {
        import(androidXModule(this@CurrencyConverterApplication))

        //vmFactory
        bindSingleton { ViewModelFactory(di.direct) }

        //apiservice
        bindSingleton { RetrofitObj.API_SERVICE }

        //database
        bindSingleton { CurrencyDB.getInstance(instance()) }

        //repositories
        bindSingleton { HomeRepository(instance(), instance(), instance()) }


        //viewmodels
        bind<HomeViewModel>(HomeViewModel::class.java.simpleName) with provider {
            HomeViewModel(
                instance()
            )
        }

    }
}