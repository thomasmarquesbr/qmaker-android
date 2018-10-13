package com.qmakercorp.qmaker.ui.login.fragments


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.qmakercorp.qmaker.R
import com.qmakercorp.qmaker.components.Alert
import com.qmakercorp.qmaker.data.model.Student
import com.wajahatkarim3.easyvalidation.core.collection_ktx.nonEmptyList
import com.wajahatkarim3.easyvalidation.core.view_ktx.greaterThan
import com.wajahatkarim3.easyvalidation.core.view_ktx.validEmail
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()
    }

    /** PRIVATE **/

    @SuppressLint("RestrictedApi")
    private fun initializeViews() {
        with(activity as AppCompatActivity) {
            supportActionBar?.setShowHideAnimationEnabled(false)
            supportActionBar?.hide()
        }
        button_signup.setOnClickListener { didTapSignup(it) }
        button_login.setOnClickListener { didTaplogin() }
        recovery_password.setOnClickListener { didTapRecoveryPassword() }
    }

    private fun didTaplogin() {
        if (validateFields()) {
            hideKeyboardFrom(button_login)
            progress_bar.visibility = View.VISIBLE
            FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(et_email.text.toString(), et_password.text.toString())
                    .addOnSuccessListener {
                        progress_bar.visibility = View.INVISIBLE
                    }.addOnFailureListener {
                        progress_bar.visibility = View.INVISIBLE
                        this.context?.let {context ->
                            Alert(context, getString(R.string.error_login), it.localizedMessage)
                                    .show()
                        }
                    }
        }
    }

    private fun didTapSignup(view: View) {
        val args = Bundle().apply {
            putParcelable("student", Student("thomas", 28))
        }
        view.findNavController().navigate(R.id.action_loginFragment_to_signupFragment, args)
    }

    private fun didTapRecoveryPassword() {
        this.context?.let {context ->
            Alert(context, getString(R.string.recovery_password_title), getString(R.string.recovery_password_message))
                    .showWithEdittext(getString(R.string.email_placeholder),
                            onPositiveClick =  {
                                Log.e("TESTE", "${it.text}")
                                resetPassword(context, it) },
                            onNegativeClick =  {})
        }
    }

    private fun resetPassword(context: Context, editTextEmail: EditText) {
        editTextEmail.validator()
                .validEmail()
                .addSuccessCallback {
                    FirebaseAuth.getInstance()
                            .sendPasswordResetEmail(editTextEmail.text.toString())
                            .addOnSuccessListener { Alert(context, getString(R.string.email_recovery_sended_successfull)).show() }
                            .addOnFailureListener { Alert(context, it.localizedMessage).show() }
                }
                .addErrorCallback {
                    Alert(context, getString(R.string.error_email)).show()
                }.check()
    }

    private fun validateFields(): Boolean {
        var isValid = true
        et_email.validEmail {
            et_email.error = getString(R.string.error_email)
            isValid = false
        }
        et_password.greaterThan(5) {
            et_password.error = getString(R.string.error_password_short)
            isValid = false
        }
        nonEmptyList(et_email, et_password) { view, _ ->
            view.error = getString(R.string.error_not_empty)
            isValid = false
        }
        return isValid
    }

    private fun hideKeyboardFrom(view: View) {
        activity?.let {
            val imm = it.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    /** PUBLIC **/

}
