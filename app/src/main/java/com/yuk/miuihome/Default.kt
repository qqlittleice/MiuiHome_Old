package com.yuk.miuihome

import android.annotation.SuppressLint
import android.content.Context

class Default {
    val complete = 1
    val simple = 0
    val none = 0
    val folder = 0
    val maml = 0
    val smooth = 1
    val clock = 0
    val simplea = 0
    val icon = 0
    val transition = 10
    val DATAFILENAME = "Config"

    @SuppressLint("SetWorldReadable")
    fun saveData(context: Context, key: String, value: Int) {
        try {
            val sharedPreferences = context.getSharedPreferences(DATAFILENAME, Context.MODE_WORLD_READABLE)
            val editor = sharedPreferences.edit()
            editor.putInt(key, value)
            editor.apply()
        } catch (e: Exception) {
        }
    }

    fun getData(context: Context, key: String, defValue: Int): Int {
        try {
            val sharedPreferences =
                context.getSharedPreferences(DATAFILENAME, Context.MODE_WORLD_READABLE)
            return sharedPreferences.getInt(key, defValue)
        } catch (e: Exception) {
            if (key == "TEST_MODULE") {
                return 0
            }
        }
        return defValue
    }
}