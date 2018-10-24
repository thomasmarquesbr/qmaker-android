package com.qmakercorp.qmaker.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Answer(var id: Int,
             var description: String,
             var isTrue: Boolean): Parcelable