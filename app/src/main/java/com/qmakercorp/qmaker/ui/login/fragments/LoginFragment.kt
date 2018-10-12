package com.qmakercorp.qmaker.ui.login.fragments


import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.ValidationStyle
import com.qmakercorp.qmaker.R
import com.qmakercorp.qmaker.data.model.Student
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment() {

    private val regexPassword = ".{6,}"
    private val validator = AwesomeValidation(ValidationStyle.BASIC)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()
    }

    /** PRIVATE **/

    private fun initializeViews() {
        with(activity as AppCompatActivity) {
            supportActionBar?.hide()
        }
        button_signup.setOnClickListener {
            val args = Bundle().apply {
                putParcelable("student", Student("thomas", 28))
            }
            it.findNavController().navigate(R.id.action_loginFragment_to_signupFragment, args)
        }
        button_login.setOnClickListener {
            if (validator.validate()) loginUser()
        }
        with(validator) {
            addValidation(activity, R.id.til_email, Patterns.EMAIL_ADDRESS, R.string.error_email)
            addValidation(activity, R.id.et_password, regexPassword, R.string.invalid_password)
        }
    }

    private fun loginUser() {

    }

}
