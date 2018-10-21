package com.qmakercorp.qmaker.ui.student

import android.content.Intent
import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.marginTop
import com.qmakercorp.qmaker.R
import com.qmakercorp.qmaker.data.dao.StudentAnswersDao
import com.qmakercorp.qmaker.data.model.Question
import kotlinx.android.synthetic.main.activity_answer.*
import android.content.res.ColorStateList
import com.google.firebase.auth.FirebaseAuth
import com.qmakercorp.qmaker.components.Alert
import com.qmakercorp.qmaker.data.model.Student
import com.qmakercorp.qmaker.utils.*


class AnswerActivity : AppCompatActivity() {

    private var quizId: String? = null
    private var userId: String? = null
    private var name: String? = null
    private var classGroup: String? = null
    private var questionIndex = 0
    private var questions = mutableListOf<Question>()
    private var checkedAnswer = -1
    private var answersChecked = mutableListOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_answer)
        readIntentData()
        initializeViews()
    }

    override fun onStop() {
        FirebaseAuth.getInstance()
                .signOut()
        super.onStop()
    }

    private fun readIntentData() {
        intent.extras?.let { bundle ->
            quizId = bundle.getString(PARCEL_QUIZ_ID)
            userId = bundle.getString(PARCEL_USER_ID)
            name = bundle.getString(PARCEL_NAME)
            classGroup = bundle.getString(PARCEL_CLASS)
            safeLet(userId, quizId, name, classGroup) { it0, it1, it2, it3 ->
                questionIndex = bundle.getInt(PARCEL_QUESTION_INDEX)
                if (questionIndex == 0) {
                    getQuestions(it0, it1)
                } else {
                    val list = intent.getParcelableArrayListExtra<Question>(PARCEL_QUESTIONS)
                    questions = list.toMutableList()
                    answersChecked = intent.getIntegerArrayListExtra(PARCEL_ANSWERS_CHECKED).toMutableList()
                    showQuestion(questions[questionIndex])
                }
            }
        }
    }

    private fun initializeViews() {
        if (questionIndex == questions.size - 1)
            button_next.text = getString(R.string.finish)
    }

    private fun getQuestions(userId: String, quizId: String) {
        progress_bar.visibility = View.VISIBLE
        StudentAnswersDao().getQuestions(userId, quizId) { listQuestions ->
            progress_bar.visibility = View.INVISIBLE
            questions = listQuestions
            showQuestion(listQuestions.first())
        }
    }

    private fun showQuestion(question: Question) {
        tv_step.text = "${questionIndex+1}/${questions.size}"
        tv_question.text = question.description
        question.answers.forEachIndexed { index, description ->
            val layoutParams = RadioGroup.LayoutParams(this, null)
            layoutParams.setMargins(16, 16,16,16)
            val colorStateList = ColorStateList(arrayOf(intArrayOf(android.R.attr.state_enabled)),
                    intArrayOf(resources.getColor(R.color.white)))
            val radioButton = RadioButton(this)
            radioButton.layoutParams = layoutParams
            radioButton.text = description
            radioButton.textSize = 22f
            radioButton.buttonTintList = colorStateList
            radioButton.highlightColor = ContextCompat.getColor(this, R.color.white)
            radioButton.setTextColor(ContextCompat.getColor(this, R.color.white))
            radioButton.tag = index
            rg_answers.addView(radioButton)
        }
        rg_answers.setOnCheckedChangeListener { group, checkedId ->
            val radioButton = group.findViewById<RadioButton>(checkedId)
            checkedAnswer = radioButton.tag as Int
        }
    }

    fun onClickNext(view: View) {
        if (checkedAnswer >= 0) {
            answersChecked.add(checkedAnswer)
            if (questionIndex+1 < questions.size) {
                callNextQuestion()
            } else {
                val student = Student(name!!, classGroup!!, answersChecked)
                StudentAnswersDao().saveAnswers(quizId!!, student) {
                    if (!it)
                        Alert(this, R.string.error_save_answers).show {
                            finishAffinity()
                        }
                    else
                        finishAffinity()
                }
            }
        } else
            Alert(this, R.string.answer_not_checked_message).show()
    }

    private fun callNextQuestion() {
        val intent = Intent(this, AnswerActivity::class.java)
        val bundle = Bundle().apply {
            putString(PARCEL_QUIZ_ID, quizId)
            putString(PARCEL_USER_ID, userId)
            putString(PARCEL_NAME, name)
            putString(PARCEL_CLASS, classGroup)
            putInt(PARCEL_QUESTION_INDEX, questionIndex+1)
            putParcelableArrayList(PARCEL_QUESTIONS, ArrayList<Question>(questions))
            putIntegerArrayList(PARCEL_ANSWERS_CHECKED, ArrayList(answersChecked))
        }
        intent.putExtras(bundle)
        startActivity(intent)
        finish()
    }

}
