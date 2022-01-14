package com.wavemoney.currencyconverter.model.response

data class CurrencyResponseModel(
    val quotes: LinkedHashMap<String, Double>,
    val success: Boolean
)