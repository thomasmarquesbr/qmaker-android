package com.qmakercorp.qmaker.data.model

import android.os.Parcelable
import android.util.Log
import kotlinx.android.parcel.Parcelize
import java.text.DecimalFormat


@Parcelize
data class QuizResult(val id: String,
                      val name: String,
                      var resultValue: Double,
                      var quiz: Quiz,
                      val questions: MutableList<Question>,
                      val students: MutableList<Student>): Parcelable {

    constructor(id: String, name: String, quiz: Quiz): this(id, name, 0.0, quiz, mutableListOf(), mutableListOf())

    init {
        calculateResultValue()
    }

    private fun calculateResultValue() {
        val trueAnswers= mutableListOf<MutableList<Int>>()
        questions.forEach { trueAnswers.add(it.trueAnswers) }
        var countStudentMedia = 0
        students.forEachIndexed { index, student ->
            var answerHits = 0
            student.answers.forEachIndexed { i, answer ->
                if (trueAnswers[i].contains(answer))
                    answerHits++
            }
            val media = calculatePercent(answerHits, trueAnswers.size)
            if (media > 60) countStudentMedia++
            students[index].media = media
        }
        this.resultValue = calculatePercent(countStudentMedia, students.size)
    }

    private fun calculatePercent(hits: Int, total: Int): Double {
        return if (total != 0)
            ((hits.toDouble() / total) * 100)
        else
            0.0
    }

}