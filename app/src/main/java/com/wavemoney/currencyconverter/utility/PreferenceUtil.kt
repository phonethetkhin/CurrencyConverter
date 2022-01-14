package com.wavemoney.currencyconverter.utility

import android.content.Context

/**
 * This function is setting the boolean pref.
 *
 * @param context (Context)
 * @param name (String)
 * @param key (String)
 * @param value (Boolean)
 */
fun setBooleanPref(context: Context, name: String, key: String, value: Boolean) {
    val pref = context.getSharedPreferences(name, Context.MODE_PRIVATE)
    val editor = pref.edit()
    editor.putBoolean(key, value)
    editor.apply()
}

/**
 * This function is getting the boolean pref.
 *
 * @param context (Context)
 * @param name (String)
 * @param key (String)
 * @param defaultValue (Boolean)
 * @return  Boolean
 */
fun getBooleanPref(context: Context, name: String, key: String, defaultValue: Boolean): Boolean {
    val pref = context.getSharedPreferences(name, Context.MODE_PRIVATE)
    return pref.getBoolean(key, defaultValue)
}
