package com.qmakercorp.qmaker.data.dao

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.qmakercorp.qmaker.data.model.Question
import com.qmakercorp.qmaker.data.model.Quiz
import com.qmakercorp.qmaker.data.model.QuizResult
import com.qmakercorp.qmaker.data.model.Student
import com.qmakercorp.qmaker.utils.*

class QuizzesResultDao {

    private val database = FirebaseFirestore.getInstance()

    /** PRIVATE **/

    private fun getQuestions(userId: String, quiz: Quiz, completion: (MutableList<Question>) -> Unit) {
        database.collection(USERS)
                .document(userId)
                .collection(QUIZZES)
                .document(quiz.id)
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
                }
    }

    private fun getStudents(quiz: Quiz, completion: (MutableList<Student>) -> Unit) {
        database.collection(PUBLIC_QUIZZES)
                .document(quiz.id)
                .collection(STUDENTS)
                .orderBy(NAME)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    val list = mutableListOf<Student>()
                    querySnapshot.documents.forEach { document ->
                        val student = document.toObject(Student::class.java)
                        student?.let { list.add(it) }
                    }
                    completion(list)
                }
    }

    /** PUBLIC **/

    fun getQuizzes(completion: (MutableList<QuizResult>) -> Unit) {
        FirebaseAuth.getInstance().currentUser?.let { user ->
            database.collection(USERS)
                    .document(user.uid)
                    .collection(QUIZZES)
                    .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                        val list = mutableListOf<QuizResult>()
                        querySnapshot?.let { snap ->
                            snap.documents.forEach { quizDocument ->
                                quizDocument.toObject(Quiz::class.java)?.let { quiz ->

                                    getQuestions(user.uid, quiz) { questions ->

                                        getStudents(quiz) { students ->
                                            val quizResult = QuizResult(quiz.id, quiz.name, 0.0, quiz, questions, students)
                                            list.add(quizResult)
                                            completion(list)
                                        }

                                    }

                                }
                            }
                            if (snap.documents.size == 0)
                                completion(mutableListOf())
                        } ?: run { completion(mutableListOf()) }
                    }
        }
    }


}