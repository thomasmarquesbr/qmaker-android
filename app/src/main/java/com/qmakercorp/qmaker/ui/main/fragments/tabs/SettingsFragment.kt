package com.qmakercorp.qmaker.ui.main.fragments.tabs


import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.qmakercorp.qmaker.BuildConfig
import com.qmakercorp.qmaker.R
import com.qmakercorp.qmaker.components.Alert
import com.qmakercorp.qmaker.data.dao.UserDao
import com.qmakercorp.qmaker.helper.Preferences
import com.qmakercorp.qmaker.ui.login.LoginActivity
import com.qmakercorp.qmaker.ui.payment.PaymentActivity
import com.qmakercorp.qmaker.utils.PLAN
import com.qmakercorp.qmaker.utils.Plan
import com.wajahatkarim3.easyvalidation.core.view_ktx.nonEmpty
import com.wajahatkarim3.easyvalidation.core.view_ktx.textEqualTo
import kotlinx.android.synthetic.main.change_password_dialog.*
import kotlinx.android.synthetic.main.change_plan_dialog.*
import kotlinx.android.synthetic.main.fragment_settings.*


class SettingsFragment : Fragment() {

    private var plan = Plan.BASIC

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()
        UserDao().getData { name, email, plan ->
            this@SettingsFragment.plan = plan
            changePlanInfo(plan)
        }
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

    private fun changePlanInfo(plan: Plan) {
        when (plan) {
            Plan.BASIC -> {
                tv_plan.setText(R.string.be_plus)
                tv_plan.setTypeface(null, Typeface.BOLD)
                iv_star1.visibility = View.VISIBLE
                iv_star2.visibility = View.INVISIBLE
            }
            Plan.PLUS_MONTHLY, Plan.PLUS_YEARLY -> {
                tv_plan.setText(R.string.be_premium)
                tv_plan.setTypeface(null, Typeface.BOLD)
                iv_star1.visibility = View.VISIBLE
                iv_star2.visibility = View.VISIBLE
            }
            Plan.PREMIUM_MONTHLY, Plan.PREMIUM_YEARLY -> {
                tv_plan.setText(R.string.change_plan)
                tv_plan.setTypeface(null, Typeface.NORMAL)
                iv_star1.visibility = View.INVISIBLE
                iv_star2.visibility = View.INVISIBLE
            }
        }
    }

    private fun changePlan() {
        context?.let { context ->
            val dialog = Dialog(context)
            with(dialog) {
                setContentView(R.layout.change_plan_dialog)
                basic_plan.setOnClickListener { callPaymentActivity(this, Plan.BASIC) }
                plus_plan_monthly.setOnClickListener { callPaymentActivity(this, Plan.PLUS_MONTHLY) }
                plus_plan_yearly.setOnClickListener { callPaymentActivity(this, Plan.PLUS_YEARLY) }
                premium_plan_monthly.setOnClickListener { callPaymentActivity(this, Plan.PREMIUM_MONTHLY) }
                premium_plan_yearly.setOnClickListener { callPaymentActivity(this, Plan.PREMIUM_YEARLY) }
                basic_plan.setOnClickListener {  }
                markViewCurrentPlan(this)
                setCanceledOnTouchOutside(true)
                show()
            }
        }
    }

    private fun callPaymentActivity(dialog: Dialog, plan: Plan) {
        activity?.let { activity ->
            if (plan == Plan.BASIC) {
                Alert(activity, R.string.message_change_to_basic_plan).show(onClickYes = {
                    activateBasicPlan()
                }, onClickNo = {})
            } else {
                val intent = Intent(activity, PaymentActivity::class.java)
                intent.putExtra(PLAN, plan.name)
                startActivity(intent)
            }
            dialog.dismiss()
        }
    }

    private fun activateBasicPlan() {

    }

    private fun markViewCurrentPlan(dialog: Dialog) {
        with(dialog) {
            when(plan) {
                Plan.BASIC -> {
                    basic_plan.setBackgroundColor(ContextCompat.getColor(dialog.context, R.color.lightGray))
                    basic_plan.isClickable = false
                    basic_plan.isFocusable = false
                }
                Plan.PLUS_MONTHLY -> {
                    plus_plan_monthly.setBackgroundColor(ContextCompat.getColor(dialog.context, R.color.lightGray))
                    plus_plan_monthly.isClickable = false
                    plus_plan_monthly.isFocusable = false
                }
                Plan.PLUS_YEARLY -> {
                    plus_plan_yearly.setBackgroundColor(ContextCompat.getColor(dialog.context, R.color.lightGray))
                    plus_plan_yearly.isClickable = false
                    plus_plan_yearly.isFocusable = false
                }
                Plan.PREMIUM_MONTHLY -> {
                    premium_plan_monthly.setBackgroundColor(ContextCompat.getColor(dialog.context, R.color.lightGray))
                    premium_plan_monthly.isClickable = false
                    premium_plan_monthly.isFocusable = false
                }
                Plan.PREMIUM_YEARLY -> {
                    premium_plan_yearly.setBackgroundColor(ContextCompat.getColor(dialog.context, R.color.lightGray))
                    premium_plan_yearly.isClickable = false
                    premium_plan_yearly.isFocusable = false
                }
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
