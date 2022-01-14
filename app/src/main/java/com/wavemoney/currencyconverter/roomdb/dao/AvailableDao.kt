package com.wavemoney.currencyconverter.roomdb.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wavemoney.currencyconverter.roomdb.entity.AvailableEntity


@Dao
interface AvailableDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllAvailable(
        availableList: List<AvailableEntity>
    )

    @Query("SELECT * FROM tbl_available")
    suspend fun getAllAvailable(): List<AvailableEntity>

    @Query("DELETE FROM tbl_available")
    suspend fun deleteAllAvailable()

}