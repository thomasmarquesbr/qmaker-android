package com.qmakercorp.qmaker.helper

import android.content.Context
import androidx.core.content.edit

enum class MODE {
    NONE, TEACHER, STUDENT
}

class Preferences(val context: Context) {

    companion object {
        private const val PREF_KEY = "pref_key"
        private const val MODE_KEY = "mode_key"
    }

    fun mode() : MODE {
        val value = context
                .getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
                .getString(MODE_KEY, MODE.NONE.name)
        return MODE.valueOf(value ?: MODE.NONE.name)
    }

    fun saveMode(value: MODE) {
        val sharedPreferences = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
        sharedPreferences.edit {
            putString(MODE_KEY, value.name).apply()
        }
    }

    fun clearMode() {
        val sharedPreferences = context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
        sharedPreferences.edit {
            remove(MODE_KEY)
        }
    }

}