package com.qmakercorp.qmaker.ui.login.fragments


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.qmakercorp.qmaker.R
import com.qmakercorp.qmaker.components.Alert
import com.qmakercorp.qmaker.data.model.Student
import com.wajahatkarim3.easyvalidation.core.collection_ktx.greaterThanList
import com.wajahatkarim3.easyvalidation.core.collection_ktx.nonEmptyList
import com.wajahatkarim3.easyvalidation.core.view_ktx.noSpecialCharacters
import com.wajahatkarim3.easyvalidation.core.view_ktx.textEqualTo
import com.wajahatkarim3.easyvalidation.core.view_ktx.validEmail
import kotlinx.android.synthetic.main.fragment_signup.*

class SignupFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()
    }

    /** PRIVATE **/

    @SuppressLint("RestrictedApi")
    private fun initializeViews() {
        with(activity as AppCompatActivity) {
            title = getString(R.string.sign_up)
            supportActionBar?.setShowHideAnimationEnabled(false)
            supportActionBar?.show()
        }
        button_signup.setOnClickListener { didTapSignup() }
    }

    private fun didTapSignup() {
        if (validateFields()) {
            progress_bar.visibility = View.VISIBLE
            FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(et_email.text.toString(), et_password.text.toString())
                    .addOnSuccessListener {
                        progress_bar.visibility = View.INVISIBLE
                        val profileUpdates = UserProfileChangeRequest.Builder()
                                .setDisplayName(et_name.text.toString())
                                .build()
                        FirebaseAuth.getInstance()
                                .currentUser?.updateProfile(profileUpdates)
                    }.addOnFailureListener {
                        progress_bar.visibility = View.INVISIBLE
                        this.context?.let {context ->
                            Alert(context, getString(R.string.error_login), it.localizedMessage)
                                    .show()
                        }
                    }
        }
    }

    private fun validateFields(): Boolean {
        var isValid = true
        et_name.noSpecialCharacters {
            et_name.error = getString(R.string.error_name)
            isValid = false
        }
        et_email.validEmail {
            et_email.error = getString(R.string.error_email)
            isValid = false
        }
        et_password_repeat.textEqualTo(et_password.text.toString()) {
            et_password_repeat.error = getString(R.string.error_password_confirmation)
            isValid = false
        }
        nonEmptyList(et_name, et_email, et_password, et_password_repeat) { view, _ ->
            view.error = getString(R.string.error_not_empty)
            isValid = false
        }
        greaterThanList(5, et_password, et_password_repeat) { view, _ ->
            view.error = getString(R.string.error_password_short)
            isValid = false
        }
        return isValid
    }

}
