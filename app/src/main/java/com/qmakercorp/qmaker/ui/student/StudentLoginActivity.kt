package com.qmakercorp.qmaker.ui.student

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.qmakercorp.qmaker.R
import com.qmakercorp.qmaker.components.Alert
import com.qmakercorp.qmaker.utils.*
import com.wajahatkarim3.easyvalidation.core.collection_ktx.nonEmptyList
import kotlinx.android.synthetic.main.activity_student_login.*

class StudentLoginActivity : AppCompatActivity() {

    private var idQuiz: String? = null
    private var idUser: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_login)
        intent.extras?.let { bundle ->
            idQuiz = bundle.getString(PARCEL_QUIZ_ID)
            idUser = bundle.getString(PARCEL_USER_ID)
        }
    }

    private fun validateFields(): Boolean {
        var isValid = true
        nonEmptyList(et_name, et_class) { view, _ ->
            view.error = getString(R.string.error_not_empty)
            isValid = false
        }
        return isValid
    }

    private fun callAnswerActivity() {
        val intent = Intent(this, AnswerActivity::class.java)
//        intent.putExtra("quiz_id", idQuiz)
//        intent.putExtra("user_id", idUser)
//        intent.putExtra("name", et_name.text.toString())
//        intent.putExtra("class", et_class.text.toString())
//        intent.putExtra("questions_index", 0)
        val bundle = Bundle().apply {
            putString(PARCEL_QUIZ_ID, idQuiz)
            putString(PARCEL_USER_ID, idUser)
            putString(PARCEL_NAME, et_name.text.toString())
            putString(PARCEL_CLASS, et_class.text.toString())
            putInt(PARCEL_QUESTION_INDEX, 0)
        }
        intent.putExtras(bundle)
        startActivity(intent)
        finish()
    }

    private fun loginAnonim() {
        progress_bar.visibility = View.VISIBLE
        FirebaseAuth.getInstance()
                .signInAnonymously()
                .addOnSuccessListener {
                    progress_bar.visibility = View.INVISIBLE
                    callAnswerActivity()
                }.addOnFailureListener {
                    progress_bar.visibility = View.INVISIBLE
                    Alert(this, it.localizedMessage).show()
                }
    }

    /** PUBLIC **/

    fun onClickStartQuiz(view: View) {
        if (validateFields())
            loginAnonim()
    }

}
