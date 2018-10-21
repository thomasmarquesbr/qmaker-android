package com.qmakercorp.qmaker.data.dao

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.qmakercorp.qmaker.data.model.Answer
import com.qmakercorp.qmaker.data.model.Question
import com.qmakercorp.qmaker.data.model.Quiz
import com.qmakercorp.qmaker.utils.ORDER
import com.qmakercorp.qmaker.utils.QUESTIONS
import com.qmakercorp.qmaker.utils.QUIZZES
import com.qmakercorp.qmaker.utils.USERS

class QuizDao {

    private val database = FirebaseFirestore.getInstance()

    fun getQuestions(idQuiz: String, completion: (MutableList<Question>) -> Unit) {
        FirebaseAuth.getInstance().currentUser?.let { user ->
            database.collection(USERS)
                    .document(user.uid)
                    .collection(QUIZZES)
                    .document(idQuiz)
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
    }

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
            database.collection(USERS)
                    .document(user.uid)
                    .collection(QUIZZES)
                    .document(quiz.id)
                    .collection(QUESTIONS)
                    .document(question.id)
                    .set(question)
                    .addOnSuccessListener { result(null) }
                    .addOnFailureListener { result(it.localizedMessage) }
        }
    }

    fun removeQuestion(quiz: Quiz, question: Question, result: (String?) -> Unit) {
        FirebaseAuth.getInstance().currentUser?.let { user ->
            database.collection(USERS)
                    .document(user.uid)
                    .collection(QUIZZES)
                    .document(quiz.id)
                    .collection(QUESTIONS)
                    .document(question.id)
                    .delete()
                    .addOnSuccessListener { result(null) }
                    .addOnFailureListener { result(it.localizedMessage) }
        }
    }

    fun generateId(): String {
        return FirebaseFirestore.getInstance().collection(USERS).document().id
    }

    fun saveQuestionsOrder(quiz: Quiz, questions: MutableList<Question>) {
        FirebaseAuth.getInstance().currentUser?.let { user ->
            val batch = database.batch()
            questions.forEachIndexed { index, question ->
                val docRef = database.collection(USERS)
                        .document(user.uid)
                        .collection(QUIZZES)
                        .document(quiz.id)
                        .collection(QUESTIONS)
                        .document(question.id)
                batch.update(docRef, ORDER, index)
            }
            batch.commit()
        }
    }

}