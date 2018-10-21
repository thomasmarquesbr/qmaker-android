package com.qmakercorp.qmaker.data.dao

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.qmakercorp.qmaker.data.model.Question
import com.qmakercorp.qmaker.data.model.Student
import com.qmakercorp.qmaker.utils.*

class StudentAnswersDao {

    private val database = FirebaseFirestore.getInstance()

    fun validateQuizCode(code: String, result: (Boolean, String?, String?)->Unit) {
        database.collection(PUBLIC_QUIZZES)
                .whereEqualTo(CODE, code)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    val idQuiz = querySnapshot.documents.first().id
                    val idUser = querySnapshot.documents.first().data?.get(USER) as String
                    safeLet(idQuiz, idUser) { quiz, user ->
                        result(true, quiz, user)
                    }
                }.addOnFailureListener { result(false, null, null) }
    }

    fun getQuestions(userId: String, quizId: String, completion: (MutableList<Question>)->Unit) {
        database.collection(USERS)
                .document(userId)
                .collection(QUIZZES)
                .document(quizId)
                .collection(QUESTIONS)
                .orderBy(ORDER)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    val list = mutableListOf<Question>()
                    querySnapshot.documents.forEach { document ->
                        val question = document.toObject(Question::class.java)
                        question?.let { list.add(it) }
                    }
                    completion(list)
                }.addOnFailureListener { completion(mutableListOf()) }
    }

    fun saveAnswers(quizId: String, student: Student, completion: (Boolean) -> Unit) {
        database.collection(PUBLIC_QUIZZES)
                .document(quizId)
                .collection(STUDENTS)
                .add(student)
                .addOnSuccessListener { completion(true) }
                .addOnFailureListener {
                    Log.e(this.javaClass.simpleName, it.localizedMessage)
                    completion(false) }
    }

}