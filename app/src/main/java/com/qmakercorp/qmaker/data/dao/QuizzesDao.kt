package com.qmakercorp.qmaker.data.dao

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.qmakercorp.qmaker.data.model.Question
import com.qmakercorp.qmaker.data.model.Quiz

class QuizzesDao {

    private val database = FirebaseFirestore.getInstance()

    fun getQuizzes(completion: (MutableList<Quiz>)->Unit) {
        FirebaseAuth.getInstance().currentUser?.let { user ->
            database.collection("users")
                    .document(user.uid)
                    .collection("quizzes")
                    .orderBy("name")
                    .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                        querySnapshot?.let {
                            val list = mutableListOf<Quiz>()
                            querySnapshot.documents.forEach { documentSnapshot ->
                                val quiz = documentSnapshot.toObject(Quiz::class.java)
                                quiz?.let { it -> list.add(it) }
                            }
                            completion(list)
                        }
                    }
        }
    }

    fun publishQuiz(quiz: Quiz, completion: (Boolean)->Unit) {
        FirebaseAuth.getInstance().currentUser?.let {user ->
            val code = if (quiz.code.isEmpty())
                quiz.id.subSequence(0, 6).toString().toUpperCase()
            else ""
            database.collection("users")
                    .document(user.uid)
                    .collection("quizzes")
                    .document(quiz.id)
                    .update("code", code)
                    .addOnSuccessListener { completion(true) }
                    .addOnFailureListener { completion(false) }
        }
    }

    fun saveQuiz(quiz: Quiz, result: (Boolean) -> Unit) {
        FirebaseAuth.getInstance().currentUser?.let { user ->
            database.collection("users")
                    .document(user.uid)
                    .collection("quizzes")
                    .document(quiz.id)
                    .set(quiz.toMap())
                    .addOnSuccessListener { result(true) }
                    .addOnFailureListener { result(false) }
        }
    }

    fun removeQuiz(quiz: Quiz, result: (Boolean) -> Unit) {
        FirebaseAuth.getInstance().currentUser?.let { user ->
            Log.e("TESTE", quiz.id)
            database.collection("users")
                    .document(user.uid)
                    .collection("quizzes")
                    .document(quiz.id)
                    .delete()
                    .addOnSuccessListener { result(true) }
                    .addOnFailureListener { result(false) }
        }
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