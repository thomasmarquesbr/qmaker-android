package com.qmakercorp.qmaker.ui.main.fragments.quizzes


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.qmakercorp.qmaker.R
import com.qmakercorp.qmaker.data.model.Question
import com.qmakercorp.qmaker.data.model.Quiz

class QuestionFragment : Fragment() {

    private var quiz: Quiz? = null
    private var question: Question? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_question, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        quiz = arguments?.getParcelable("quiz")
        question = arguments?.getParcelable("question")
    }


}
