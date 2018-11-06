package com.qmakercorp.qmaker.ui.main.fragments.tabs


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.qmakercorp.qmaker.BuildConfig

import com.qmakercorp.qmaker.R
import com.qmakercorp.qmaker.components.Alert
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
        (activity as AppCompatActivity).supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(false)
            it.title = getString(R.string.settings)
        }
        button_change_password.setOnClickListener { changePassword() }
        button_logout.setOnClickListener { goLogout() }
    }

    private fun changePassword() {

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

}
