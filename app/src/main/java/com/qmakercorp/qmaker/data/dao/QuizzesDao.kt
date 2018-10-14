package com.qmakercorp.qmaker.data.dao

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.qmakercorp.qmaker.data.model.Quiz

class QuizzesDao {

    private val database = FirebaseFirestore.getInstance()

    fun getQuizzes(quizzesList: (MutableList<Quiz>)->Unit) {
        FirebaseAuth.getInstance().currentUser?.let { user ->
            database.collection("users")
                    .document(user.uid)
                    .collection("quizzes")
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        val list = mutableListOf<Quiz>()
                        querySnapshot.documents.forEach { documentSnapshot ->
                            val quiz = documentSnapshot.toObject(Quiz::class.java)
                            quiz?.let { it -> list.add(it) }
                        }
                        quizzesList(list)
                    }.addOnFailureListener {
                        Log.e(javaClass.simpleName, it.localizedMessage)
                        quizzesList(mutableListOf())
                    }
        }
    }

    fun removeQuiz(quiz: Quiz) {

    }

    private fun quizzesMock(): MutableList<Quiz> {
        return mutableListOf(
                Quiz("teste", "description", "LASEKA"),
                Quiz("teste", "description", "LASEKA"),
                Quiz("teste", "description", "LASEKA"),
                Quiz("teste", "description", "LASEKA"),
                Quiz("teste", "description", "LASEKA"),
                Quiz("teste", "description", "LASEKA"),
                Quiz("teste", "description", "LASEKA"),
                Quiz("teste", "description", "LASEKA"),
                Quiz("teste", "description", "LASEKA"),
                Quiz("teste", "description", "LASEKA"),
                Quiz("teste", "description", "LASEKA"),
                Quiz("teste", "description", "LASEKA"),
                Quiz("teste", "description", "LASEKA"),
                Quiz("teste", "description", "LASEKA"),
                Quiz("teste", "description", "LASEKA"),
                Quiz("teste", "description", "LASEKA")
        )
    }

}