package com.wavemoney.currencyconverter.roomdb.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_available", indices = [Index(value = ["id"], unique = true)])
data class AvailableEntity(
    @ColumnInfo(name = "available") val available: String,
    @PrimaryKey(autoGenerate = true) var id: Int = 0//last so that we don't have to pass an ID value or named arguments
)