package com.qmakercorp.qmaker.ui.payment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.qmakercorp.qmaker.R
import com.qmakercorp.qmaker.utils.PLAN
import com.qmakercorp.qmaker.utils.Plan

class PaymentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        title = getString(R.string.payment_method)
        intent?.extras?.getString(PLAN)?.let { value ->
            val plan = Plan.valueOf(value)
            Log.e("Teste", plan.name)
        }
    }


    /** PUBLIC **/

    fun didTapFinish(view: View) {
        finish()
    }

}
