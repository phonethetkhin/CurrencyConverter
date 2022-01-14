package com.wavemoney.currencyconverter.model.response

data class AvailableListResponseModel(
    val currencies: LinkedHashMap<String, String>,
    val success: Boolean
)