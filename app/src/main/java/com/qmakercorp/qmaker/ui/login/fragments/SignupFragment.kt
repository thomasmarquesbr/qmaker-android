package com.qmakercorp.qmaker.ui.login.fragments


import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.ValidationStyle

import com.qmakercorp.qmaker.R
import com.qmakercorp.qmaker.data.model.Student
import kotlinx.android.synthetic.main.fragment_signup.*

class SignupFragment : Fragment() {

    private val regexName = "([a-zA-Z]+\\s)*[a-zA-Z]+"
    private val regexPassword = ".{6,}"
    private val validator = AwesomeValidation(ValidationStyle.BASIC)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()
        val student: Student? = arguments?.getParcelable("student")
//        text_view.text = "${student?.name}  ${student?.age}"
    }

    /** PRIVATE **/

    private fun initializeViews() {
        with(activity as AppCompatActivity) {
            title = getString(R.string.sign_up)
            supportActionBar?.show()
        }
        button_signup.setOnClickListener {
            if (validator.validate()) signupUser()
        }
        with(validator) {
            addValidation(activity, R.id.et_name, regexName, R.string.error_name)
            addValidation(activity, R.id.et_email, Patterns.EMAIL_ADDRESS, R.string.error_email)
            addValidation(activity, R.id.password, regexPassword, R.string.invalid_password)
            addValidation(activity, R.id.et_password_repeat, R.id.et_password, R.string.error_password_confirmation)
        }
    }

    private fun signupUser() {

    }

}
