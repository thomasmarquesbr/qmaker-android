package com.qmakercorp.qmaker.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.qmakercorp.qmaker.R

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        NavigationUI.setupActionBarWithNavController(
                this, Navigation.findNavController(this, R.id.navHost)
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.navHost).navigateUp()
    }

}
