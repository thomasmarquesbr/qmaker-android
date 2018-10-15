package com.qmakercorp.qmaker.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Question(val id: String,
                    val order: Int) : Parcelable {

    var answers = mutableListOf<String>()
    var trueAnswers = mutableListOf<Int>()

    constructor(): this("", -1)

}