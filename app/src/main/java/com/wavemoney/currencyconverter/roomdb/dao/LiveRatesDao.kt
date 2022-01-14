package com.wavemoney.currencyconverter.roomdb.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wavemoney.currencyconverter.roomdb.entity.LiveRatesEntity


@Dao
interface LiveRatesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllRates(
        ratesList: List<LiveRatesEntity>
    )

    @Query("SELECT * FROM tbl_live_rates")
    suspend fun getAllRates(): List<LiveRatesEntity>

    @Query("SELECT * FROM tbl_live_rates WHERE currency=:name")
    suspend fun getRateByName(name: String): LiveRatesEntity?

    @Query("DELETE FROM tbl_live_rates")
    suspend fun deleteAllRates()

}