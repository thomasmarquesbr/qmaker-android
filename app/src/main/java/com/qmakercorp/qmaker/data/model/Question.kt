package com.qmakercorp.qmaker.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Question(val id: String,
                    val description: String,
                    var order: Int,
                    var answers: MutableList<String>,
                    var trueAnswers: MutableList<Int>): Parcelable {

//    var answers = mutableListOf<String>()
//    var trueAnswers = mutableListOf<Int>()

    constructor(): this("", "",- 1, mutableListOf<String>(), mutableListOf<Int>())
    constructor(id: String, description: String, order: Int): this(id, description, order, mutableListOf<String>(), mutableListOf<Int>())

    fun toMap(): Map<String, Any> {
        val map = mutableMapOf<String, Any>()
        map["id"] = id
        map["description"] = description
        map["order"] = order
        map["answers"] = answers
        map["trueAnswers"] = trueAnswers
        return map
    }

}