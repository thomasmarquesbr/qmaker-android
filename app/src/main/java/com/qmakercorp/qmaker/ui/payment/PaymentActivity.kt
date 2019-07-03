package com.qmakercorp.qmaker.ui.payment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import br.com.uol.pslibs.checkout_in_app.PSCheckout
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
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, PaymentFragment())
        fragmentTransaction.commitAllowingStateLoss()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //Fornece controle para LIB de Activity results
        PSCheckout.onActivityResult(this, requestCode, resultCode, data)//Controle Lib Activity Life Cycle
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        //Android 6+ fornece controle para LIB para request de permissões
        PSCheckout.onRequestPermissionsResult(this, requestCode, permissions, grantResults)//Controle Lib Activity Life Cycle
    }

    override fun onBackPressed() {
        if (PSCheckout.onBackPressed(this)) { //Controle Lib Button back
            super.onBackPressed()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                PSCheckout.onHomeButtonPressed(this) //Controle Lib Home Button
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        PSCheckout.onDestroy() //Controle Lib Activity Life Cycle
    }

    /** PUBLIC **/

    fun didTapFinish(view: View) {
        finish()
    }

}
