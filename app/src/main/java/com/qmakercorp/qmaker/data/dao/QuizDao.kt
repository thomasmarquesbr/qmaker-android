package com.qmakercorp.qmaker.data.dao

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.qmakercorp.qmaker.data.model.Answer
import com.qmakercorp.qmaker.data.model.Question
import com.qmakercorp.qmaker.data.model.Quiz

class QuizDao {

    private val database = FirebaseFirestore.getInstance()

    fun saveAnswers(quiz: Quiz,
                    question: Question,
                    answers: MutableList<Answer>,
                    result: (String?)->Unit) {
        answers.sortBy { answer -> answer.id }
        question.answers = mutableListOf()
        question.trueAnswers = mutableListOf()
        answers.forEachIndexed { index, answer ->
            question.answers.add(index, answer.description)
            if (answer.isTrue)
                question.trueAnswers.add(index)
        }
        FirebaseAuth.getInstance().currentUser?.let { user ->
            database.collection("users")
                    .document(user.uid)
                    .collection("quizzes")
                    .document(quiz.id)
                    .collection("questions")
                    .document(question.id)
                    .set(question)
                    .addOnSuccessListener { result(null) }
                    .addOnFailureListener { result(it.localizedMessage) }
        }
    }

    fun removeQuestion(quiz: Quiz, question: Question, result: (String?) -> Unit) {
        FirebaseAuth.getInstance().currentUser?.let { user ->
            database.collection("users")
                    .document(user.uid)
                    .collection("quizzes")
                    .document(quiz.id)
                    .collection("questions")
                    .document(question.id)
                    .delete()
                    .addOnSuccessListener { result(null) }
                    .addOnFailureListener { result(it.localizedMessage) }
        }
    }

    fun generateId(): String {
        return FirebaseFirestore.getInstance().collection("users").document().id
    }

}