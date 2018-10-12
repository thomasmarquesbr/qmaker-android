package com.qmakercorp.qmaker.ui.launchscreen

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.qmakercorp.qmaker.R
import com.qmakercorp.qmaker.ui.login.LoginActivity


class LaunchScreenActivity:
        AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch_screen)
    }

    /** PUBLIC **/

    fun goToCodeReader(view: View) {
//        startActivity(Intent(this,
//                CodeReaderActivity::class.java))
//        finish()
    }

    fun goToLogin(view: View) {
        startActivity(Intent(this,
                LoginActivity::class.java))
        finish()
    }


}
