package com.qmakercorp.qmaker.ui.main.fragments.tabs


import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.qmakercorp.qmaker.BuildConfig

import com.qmakercorp.qmaker.R
import com.qmakercorp.qmaker.components.Alert
import com.qmakercorp.qmaker.helper.Preferences
import com.qmakercorp.qmaker.ui.login.LoginActivity
import com.wajahatkarim3.easyvalidation.core.view_ktx.nonEmpty
import com.wajahatkarim3.easyvalidation.core.view_ktx.textEqualTo
import kotlinx.android.synthetic.main.change_password_dialog.*
import kotlinx.android.synthetic.main.fragment_settings.*
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.AuthCredential
import com.qmakercorp.qmaker.utils.safeLet
import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import com.google.android.material.snackbar.Snackbar


class SettingsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()
    }

    /** PRIVATE **/

    private fun initializeViews() {
        (activity as AppCompatActivity).supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(false)
            it.title = getString(R.string.settings)
        }
        button_change_plan.setOnClickListener { changePlan() }
        button_change_password.setOnClickListener { changePassword() }
        button_logout.setOnClickListener { goLogout() }
    }

    private fun changePlan() {
        context?.let { context ->
            val dialog = Dialog(context)
            with(dialog) {
                setContentView(R.layout.change_plan_dialog)
                show()
            }
        }
    }

    private fun validateFields(dialog: Dialog): Boolean {
        var isValid = true
        dialog.et_current_password.nonEmpty {
            dialog.et_current_password.error = getString(R.string.error_required_field)
            isValid = false
        }
        dialog.et_new_password.nonEmpty {
            dialog.et_new_password.error = getString(R.string.error_required_field)
            isValid = false
        }
        dialog.et_password_repeat.nonEmpty {
            dialog.et_password_repeat.error = getString(R.string.error_required_field)
            isValid = false
        }
        dialog.et_password_repeat.textEqualTo(dialog.et_new_password.text.toString()) {
            dialog.et_password_repeat.error = getString(R.string.error_password_confirmation)
            isValid = false
        }
        return isValid
    }

    private fun changePassword() {
        context?.let { context ->
            val dialog = Dialog(context)
            with(dialog) {
                setContentView(R.layout.change_password_dialog)
                button_cancel.setOnClickListener { dialog.dismiss() }
                button_add.setOnClickListener {
                    hideKeyboardFrom(it)
                    if (validateFields(dialog)) {
                        progress_bar.visibility = View.VISIBLE
                        reauthenticateAndUpdatePassword(dialog, context)
                    }
                }
                show()
            }
        }
    }

    private fun reauthenticateAndUpdatePassword(dialog: Dialog, context: Context) {
        FirebaseAuth.getInstance().currentUser?.let { user ->
            val credential = EmailAuthProvider
                    .getCredential(user.email.toString(), dialog.et_current_password.text.toString())
            user.reauthenticate(credential).addOnSuccessListener {
                user.updatePassword(dialog.et_new_password.text.toString()).addOnSuccessListener {
                    Snackbar.make(view!!, R.string.message_password_changed, Snackbar.LENGTH_LONG).show()
                    dialog.dismiss()
                }.addOnFailureListener { e ->
                    Snackbar.make(view!!, e.localizedMessage, Snackbar.LENGTH_LONG).show()
                }
                dialog.progress_bar.visibility = View.VISIBLE
            }.addOnFailureListener {
                Alert(context, it.localizedMessage).show()
                dialog.progress_bar.visibility = View.VISIBLE
            }
        }
    }

    private fun goLogout() {
        this.activity?.let {
            Alert(it, R.string.message_logout).show(onClickYes = {
                if (BuildConfig.DEBUG)
                    Preferences(it).clearMode()
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(it, LoginActivity::class.java))
                it.finish()
            }, onClickNo = {})
        }
    }

    private fun hideKeyboardFrom(view: View) {
        activity?.let {
            val imm = it.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

}
