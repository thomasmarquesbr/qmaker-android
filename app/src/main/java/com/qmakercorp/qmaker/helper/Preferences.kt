package com.qmakercorp.qmaker.helper

import android.content.Context
import androidx.core.content.edit


class Preferences(val context: Context) {

    companion object {
        private val PREF_KEY = "pref_key"
        private val MODE = "mode"
    }

    fun saveMode(context: Context, value: Boolean) {
        val sharedPreferences = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
        sharedPreferences.edit {
            putBoolean(MODE, value).apply()
        }
    }

    fun isTeacher(context: Context): Boolean {
        return context
                .getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
                .getBoolean(MODE, false)
    }

    fun isStudent(context: Context): Boolean {
        return !context
                .getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
                .getBoolean(MODE, false)
    }

}