package com.qmakercorp.qmaker.ui.main.fragments.quizzes


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nikhilpanju.recyclerviewenhanced.OnActivityTouchListener
import com.nikhilpanju.recyclerviewenhanced.RecyclerTouchListener

import com.qmakercorp.qmaker.R
import com.qmakercorp.qmaker.adapters.QuestionsListAdapter
import com.qmakercorp.qmaker.data.dao.QuizzesDao
import com.qmakercorp.qmaker.data.model.Question
import com.qmakercorp.qmaker.data.model.Quiz
import kotlinx.android.synthetic.main.fragment_quiz.*


class QuizFragment : Fragment(), RecyclerTouchListener.RecyclerTouchListenerHelper {

    private var quiz: Quiz? = null
    private lateinit var adapter: QuestionsListAdapter
    private lateinit var onTouchListener: RecyclerTouchListener
    private var touchListener: OnActivityTouchListener? = null

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
            rv_questions.addOnItemTouchListener(onTouchListener)
        }
    }

    override fun onPause() {
        super.onPause()
        rv_questions.removeOnItemTouchListener(onTouchListener)
    }

    override fun setOnActivityTouchListener(listener: OnActivityTouchListener?) {
        this.touchListener = listener
    }

    /** PRIVATE **/

    private fun initializeViews() {
        (activity as AppCompatActivity).supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeButtonEnabled(true)
            it.title = getString(R.string.new_quiz)
        }
        initializeRecyclerView()
    }

    private fun initializeRecyclerView() {
        context?.let { context ->
            adapter = QuestionsListAdapter(context, mutableListOf())
            rv_questions.adapter = adapter
            rv_questions.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            onTouchListener = RecyclerTouchListener(activity, rv_questions)
                    .setClickable(object : RecyclerTouchListener.OnRowClickListener {
                        override fun onRowClicked(position: Int) {
                            didTapQuestion(adapter.questions[position])
                        }

                        override fun onIndependentViewClicked(independentViewID: Int, position: Int) {}
                    })
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

    private fun didTapQuestion(question: Question) {
        quiz?.let {
            val bundle = Bundle()
            bundle.putParcelable("quiz", quiz)
            bundle.putParcelable("question", question)
            view?.findNavController()?.navigate(R.id.action_quizFragment_to_questionFragment, bundle)
        }

    }

}
