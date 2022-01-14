package com.wavemoney.currencyconverter.roomdb.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_live_rates", indices = [Index(value = ["id"], unique = true)])
data class LiveRatesEntity(
    @ColumnInfo(name = "currency") val currency: String,
    @ColumnInfo(name = "rates") val rates: Double,
    @PrimaryKey(autoGenerate = true) var id: Int = 0//last so that we don't have to pass an ID value or named arguments
)