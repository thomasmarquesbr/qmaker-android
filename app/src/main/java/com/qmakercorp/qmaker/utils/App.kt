package com.qmakercorp.qmaker.utils

import android.annotation.SuppressLint
import android.app.Application

private var _app: App? = null
val app: App get() = _app!!
//val prefs: Prefs get() = _prefs

const val USERS = "users"
const val USER = "user"
const val QUIZZES = "quizzes"
const val QUESTIONS = "questions"
const val CODE =  "code"
const val NAME = "name"
const val ORDER = "order"
const val PUBLIC_QUIZZES = "publicQuizzes"
const val STUDENTS = "students"

// Parcel keys

const val PARCEL_QUIZ_ID = "quiz_id"
const val PARCEL_USER_ID = "user_id"
const val PARCEL_NAME = "name"
const val PARCEL_CLASS = "class"
const val PARCEL_QUESTION_INDEX = "question_index"
const val PARCEL_QUESTIONS = "questions"
const val PARCEL_ANSWERS_CHECKED = "answers_checked"

fun <T1, T2> safeLet(value1: T1?, value2: T2?, bothNotNull: (T1, T2) -> (Unit)) {
    if (value1 != null && value2 != null) {
        bothNotNull(value1, value2)
    }
}

fun <T1, T2, T3> safeLet(value1: T1?, value2: T2?, value3: T3?, bothNotNull: (T1, T2, T3) -> (Unit)) {
    if (value1 != null && value2 != null && value3 != null) {
        bothNotNull(value1, value2, value3)
    }
}

fun <T1, T2, T3, T4> safeLet(value1: T1?, value2: T2?, value3: T3?, value4: T4?, bothNotNull: (T1, T2, T3, T4) -> (Unit)) {
    if (value1 != null && value2 != null && value3 != null && value4 != null) {
        bothNotNull(value1, value2, value3, value4)
    }
}

@SuppressLint("Registered")
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        _app = this
//        _prefs = Prefs(this)
    }
}