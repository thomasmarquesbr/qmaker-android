package com.qmakercorp.qmaker.data.model

data class Question(val id: String,
                    val order: Int) {

    var answers = mutableListOf<String>()
    var trueAnswers = mutableListOf<Int>()

}