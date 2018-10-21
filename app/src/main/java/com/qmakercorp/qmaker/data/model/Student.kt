package com.qmakercorp.qmaker.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Student(val name: String,
                   val classroom: String,
                   var answers: List<Int>): Parcelable {
}