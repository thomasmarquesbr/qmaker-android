package com.qmakercorp.qmaker.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Quiz(var id: String,
                var name: String,
                var code: String) : Parcelable {

    var questions = mutableListOf<Question>()

    constructor() : this("", "", "")

}