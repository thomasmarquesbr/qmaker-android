package com.qmakercorp.qmaker.ui.main.fragments.quizzes


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.qmakercorp.qmaker.R
import com.qmakercorp.qmaker.adapters.QuestionsListAdapter
import com.qmakercorp.qmaker.data.dao.QuizzesDao
import com.qmakercorp.qmaker.data.model.Quiz
import kotlinx.android.synthetic.main.fragment_quiz.*


class QuizFragment : Fragment() {

    private lateinit var adapter: QuestionsListAdapter
    private var quiz: Quiz? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_quiz, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        quiz = arguments?.getParcelable("quiz")
        initializeViews()
    }

    override fun onResume() {
        super.onResume()
        quiz?.let {
            startLoading()
            QuizzesDao().getQuestions(it.id) {
                stopLoading()
                if (it.size > 0) {
                    adapter.questions = it
                    adapter.notifyDataSetChanged()
                } else
                    showInfo(R.string.empty_list_quizzes)
            }
        }
    }

    /** PRIVATE **/

    private fun initializeViews() {
        (activity as AppCompatActivity).supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeButtonEnabled(true)
            it.title = getString(R.string.new_quiz)
        }
        context?.let {
            adapter = QuestionsListAdapter(it, mutableListOf())
            rv_questions.adapter = adapter
            rv_questions.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }
    }

    private fun showInfo(resIdMessage: Int) {
        progress_bar.visibility = View.GONE
        rv_questions.visibility = View.GONE
        tv_info.setText(resIdMessage)
        tv_info.visibility = View.VISIBLE
    }

    private fun startLoading() {
        progress_bar.visibility = View.VISIBLE
        rv_questions.visibility = View.GONE
        tv_info.visibility = View.GONE
    }

    private fun stopLoading() {
        progress_bar.visibility = View.GONE
        rv_questions.visibility = View.VISIBLE
    }
    
}
