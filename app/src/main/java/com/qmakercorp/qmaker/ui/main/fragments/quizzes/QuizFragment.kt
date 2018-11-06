package com.qmakercorp.qmaker.ui.main.fragments.quizzes


import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

import com.qmakercorp.qmaker.R
import com.qmakercorp.qmaker.adapters.QuestionsListAdapter
import com.qmakercorp.qmaker.components.Alert
import com.qmakercorp.qmaker.components.OnStartDragListener
import com.qmakercorp.qmaker.components.SimpleItemTouchHelperCallback
import com.qmakercorp.qmaker.data.dao.QuizDao
import com.qmakercorp.qmaker.data.model.Question
import com.qmakercorp.qmaker.data.model.Quiz
import kotlinx.android.synthetic.main.fragment_quiz.*
import kotlinx.android.synthetic.main.new_question_dialog.*


class QuizFragment : Fragment(), OnStartDragListener {

    private var quiz: Quiz? = null
    private lateinit var adapter: QuestionsListAdapter
    private var questions = mutableListOf<Question>()
    private var mItemTouchHelper: ItemTouchHelper? = null

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
        getQuestions()
    }

    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
        viewHolder.let { mItemTouchHelper?.startDrag(it) }
    }

    /** PRIVATE **/

    private fun initializeViews() {
        (activity as AppCompatActivity).supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeButtonEnabled(true)
            it.title = quiz?.name
        }
        setupAutoHideFabOnScrollList()
        context?.let { context ->
            fab.setOnClickListener { didTapNewQuestion(context) }
            initializeRecyclerView(context)
        }
    }

    private fun setupAutoHideFabOnScrollList() {
        rv_questions.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 || dy < 0)
                    fab.fabTextVisibility = View.GONE
            }
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE)
                    fab.fabTextVisibility = View.VISIBLE
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
    }

    private fun initializeRecyclerView(context: Context) {
        adapter = QuestionsListAdapter(context,
                mutableListOf(),
                this,
                onclick =  { position ->
                    didTapQuestion(adapter.questions[position])
                }, onRemoved = { position ->
                    onRemoved(position)
                }, onItemMoved = {
                    quiz?.let { QuizDao().saveQuestionsOrder(it, adapter.questions) }
                })
        rv_questions.adapter = adapter
        rv_questions.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        val callback = SimpleItemTouchHelperCallback(adapter)
        mItemTouchHelper = ItemTouchHelper(callback)
        mItemTouchHelper?.attachToRecyclerView(rv_questions)
    }

    private fun onRemoved(position: Int) {
        context?.let { context ->
            Alert(context, getString(R.string.alert_remove_question))
                    .show(onClickYes = {
                        quiz?.let { quiz ->
                            QuizDao().removeQuestion(quiz, questions[position]) { error ->
                                val message = error ?: getString(R.string.question_removed)
                                view?.let { Snackbar.make(it, message, Snackbar.LENGTH_LONG).show() }
                            }
                        }
                    }, onClickNo = {
                        getQuestions()
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

    private fun getQuestions() {
        quiz?.let { quiz->
            startLoading()
            QuizDao().getQuestions(quiz.id) {
                stopLoading()
                if (it.size > 0) {
                    questions = it.toMutableList()
                    adapter.questions = it
                    adapter.notifyDataSetChanged()
                } else
                    showInfo(R.string.empty_list_questions)
            }
        }
    }

    private fun didTapQuestion(question: Question) {
        quiz?.let {
            val bundle = Bundle()
            bundle.putParcelable("quiz", quiz)
            bundle.putParcelable("question", question)
            view?.findNavController()?.navigate(R.id.action_quizFragment_to_questionFragment, bundle)
        }
    }

    private fun didTapNewQuestion(context: Context) {
        val dialog = Dialog(context)
        with(dialog) {
            setContentView(R.layout.new_question_dialog)
            button_cancel.setOnClickListener { dialog.dismiss() }
            button_add.setText(R.string.save)
            button_add.setOnClickListener {
                val description = et_question.text.toString()
                val question = Question(QuizDao().generateId(),
                        description,
                        adapter.questions.size)
                dialog.dismiss()
                if (!description.isEmpty())
                    didTapQuestion(question)
            }
            show()
        }
    }

}
