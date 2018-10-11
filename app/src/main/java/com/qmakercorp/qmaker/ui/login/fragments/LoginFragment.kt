package com.qmakercorp.qmaker.ui.login.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.qmakercorp.qmaker.R
import com.qmakercorp.qmaker.data.model.Student
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : androidx.fragment.app.Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signup_button.setOnClickListener {
            val args = Bundle().apply {
                putParcelable("student", Student("thomas", 28))
            }
            it.findNavController().navigate(R.id.action_loginFragment_to_signupFragment, args)
        }
    }

}
