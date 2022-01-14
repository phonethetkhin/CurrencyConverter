package com.wavemoney.currencyconverter.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.wavemoney.currencyconverter.roomdb.dao.AvailableDao
import com.wavemoney.currencyconverter.roomdb.dao.LiveRatesDao
import com.wavemoney.currencyconverter.roomdb.entity.AvailableEntity
import com.wavemoney.currencyconverter.roomdb.entity.LiveRatesEntity

@Database(
    entities = [AvailableEntity::class, LiveRatesEntity::class],
    version = 1,
    exportSchema = false
)
abstract class CurrencyDB : RoomDatabase() {
    abstract fun availableDao(): AvailableDao
    abstract fun liveRatesDao(): LiveRatesDao

    companion object {
        @Volatile
        private var INSTANCE: CurrencyDB? = null
        fun getInstance(context: Context): CurrencyDB =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                CurrencyDB::class.java, "CurrencyConverter.db"
            ).build()
    }
}