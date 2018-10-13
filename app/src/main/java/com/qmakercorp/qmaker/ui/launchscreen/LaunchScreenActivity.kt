package com.qmakercorp.qmaker.ui.launchscreen

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.qmakercorp.qmaker.R
import com.qmakercorp.qmaker.helper.MODE
import com.qmakercorp.qmaker.helper.Preferences
import com.qmakercorp.qmaker.ui.login.LoginActivity
import com.qmakercorp.qmaker.ui.reader.CodeReaderActivity


class LaunchScreenActivity:
        AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        when(Preferences(this).mode()) {
            MODE.TEACHER -> goToLogin(null)
            MODE.STUDENT -> goToCodeReader(null)
            MODE.NONE -> setContentView(R.layout.activity_launch_screen)
        }
    }

    /** PUBLIC **/

    fun goToCodeReader(view: View?) {
//        Preferences(this).saveMode(MODE.TEACHER)
        startActivity(Intent(this,
                CodeReaderActivity::class.java))
        finish()
    }

    fun goToLogin(view: View?) {
        Preferences(this).saveMode(MODE.TEACHER)
        startActivity(Intent(this,
                LoginActivity::class.java))
        finish()
    }


}
