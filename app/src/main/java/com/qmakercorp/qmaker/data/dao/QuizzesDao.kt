package com.qmakercorp.qmaker.data.dao

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.qmakercorp.qmaker.data.model.Quiz
import com.qmakercorp.qmaker.utils.*

class QuizzesDao {

    private val database = FirebaseFirestore.getInstance()

    fun getQuizzes(completion: (MutableList<Quiz>)->Unit) {
        FirebaseAuth.getInstance().currentUser?.let { user ->
            database.collection(USERS)
                    .document(user.uid)
                    .collection(QUIZZES)
                    .orderBy(NAME)
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
            val quizRef = database.collection(USERS)
                    .document(user.uid)
                    .collection(QUIZZES)
                    .document(quiz.id)
            val publicQuizzesRef = database.collection(PUBLIC_QUIZZES)
                    .document(quiz.id)
            val batch = database.batch()
            if (quiz.code.isEmpty()) { // publish
                val code = quiz.id.subSequence(0, 6).toString().toUpperCase()
                batch.update(quizRef, CODE, code)
                val data = mapOf(CODE to code)
                batch.set(publicQuizzesRef, data)
                batch.update(publicQuizzesRef, CODE, code)
                batch.update(publicQuizzesRef, USER, user.uid)
            } else { // unpublish
                batch.update(quizRef, CODE, "")
                batch.delete(publicQuizzesRef)
            }
            batch.commit()
                    .addOnSuccessListener { completion(true) }
                    .addOnFailureListener { completion(false) }
        }
    }

    fun saveQuiz(quiz: Quiz, result: (Boolean) -> Unit) {
        FirebaseAuth.getInstance().currentUser?.let { user ->
            database.collection(USERS)
                    .document(user.uid)
                    .collection(QUIZZES)
                    .document(quiz.id)
                    .set(quiz.toMap())
                    .addOnSuccessListener { result(true) }
                    .addOnFailureListener { result(false) }
        }
    }

    fun removeQuiz(quiz: Quiz, result: (Boolean) -> Unit) {
        FirebaseAuth.getInstance().currentUser?.let { user ->
            database.collection(USERS)
                    .document(user.uid)
                    .collection(QUIZZES)
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