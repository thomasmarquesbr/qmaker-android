package com.qmakercorp.qmaker.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Quiz(var id: String,
                var name: String,
                var code: String) : Parcelable {

    constructor() : this("", "", "")

    fun toMap() : Map<String, Any> {
        val map = mutableMapOf<String, Any>()
        map["id"] = id
        map["name"] = name
        map["code"] = code
        return map
    }

}