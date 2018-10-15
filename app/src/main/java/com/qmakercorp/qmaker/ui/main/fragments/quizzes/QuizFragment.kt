package com.qmakercorp.qmaker.ui.main.fragments.quizzes


import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import com.qmakercorp.qmaker.R
import com.qmakercorp.qmaker.data.model.Quiz
import com.qmakercorp.qmaker.data.model.Student


class QuizFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_quiz, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val quiz: Quiz? = arguments?.getParcelable("quiz")
        Toast.makeText(view.context,"${quiz?.name}  ${quiz?.code} ${quiz?.questions?.size}", Toast.LENGTH_LONG).show()
        initializeViews()
    }

    private fun initializeViews() {
        (activity as AppCompatActivity).supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeButtonEnabled(true)
            it.title = getString(R.string.new_quiz)
        }
    }


}
