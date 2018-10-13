package com.qmakercorp.qmaker.ui.main.fragments


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.qmakercorp.qmaker.BuildConfig

import com.qmakercorp.qmaker.R
import com.qmakercorp.qmaker.helper.Preferences
import com.qmakercorp.qmaker.ui.login.LoginActivity
import kotlinx.android.synthetic.main.fragment_settings.*


class SettingsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()
    }

    private fun initializeViews() {
        button_logout.setOnClickListener { goLogout() }
    }

    private fun goLogout() {
        this.activity?.let {
            if (BuildConfig.DEBUG)
                Preferences(it).clearMode()
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(it, LoginActivity::class.java))
            it.finish()
        }
    }


}
