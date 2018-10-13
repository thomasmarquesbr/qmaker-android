package com.qmakercorp.qmaker.ui.login


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.firebase.auth.FirebaseAuth
import com.qmakercorp.qmaker.R
import com.qmakercorp.qmaker.ui.main.MainActivity


class LoginActivity : AppCompatActivity() {

    private val authListener = FirebaseAuth.AuthStateListener {
        it.currentUser?.let {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        NavigationUI.setupActionBarWithNavController(
                this, Navigation.findNavController(this, R.id.navHost)
        )
    }

    override fun onStart() {
        super.onStart()
        FirebaseAuth.getInstance()
                .addAuthStateListener(authListener)
    }

    override fun onStop() {
        super.onStop()
        FirebaseAuth.getInstance()
                .removeAuthStateListener(authListener)
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.navHost).navigateUp()
    }

}
