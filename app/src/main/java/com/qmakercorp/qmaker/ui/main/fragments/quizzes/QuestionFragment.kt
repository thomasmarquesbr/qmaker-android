package com.qmakercorp.qmaker.ui.main.fragments.quizzes


import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.qmakercorp.qmaker.R
import com.qmakercorp.qmaker.adapters.AnswersAdapter
import com.qmakercorp.qmaker.components.Alert
import com.qmakercorp.qmaker.components.OnStartDragListener
import com.qmakercorp.qmaker.components.SimpleItemTouchHelperCallback
import com.qmakercorp.qmaker.data.dao.QuizDao
import com.qmakercorp.qmaker.data.model.Answer
import com.qmakercorp.qmaker.data.model.Question
import com.qmakercorp.qmaker.data.model.Quiz
import kotlinx.android.synthetic.main.fragment_question.*
import kotlinx.android.synthetic.main.new_answer_dialog.*


class QuestionFragment :
        Fragment(), OnStartDragListener {

    private lateinit var adapter: AnswersAdapter
    private lateinit var quiz: Quiz
    private lateinit var question: Question
    private var mItemTouchHelper: ItemTouchHelper? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_question, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        arguments?.let { it ->
            quiz = it.getParcelable("quiz") as Quiz
            question = it.getParcelable("question") as Question
        }
        initializeViews()
    }

    override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
        viewHolder.let { mItemTouchHelper?.startDrag(it) }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_action_bar, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_confirm -> {
                didTapConfirm()
                return true
            }
        }
        return super.onOptionsItemSelected(item) // important line
    }

    private fun initializeAnswers(): MutableList<Answer> {
        val answers = mutableListOf<Answer>()
        question.answers.forEachIndexed { index, description ->
            answers.add(Answer(index, description, false))
        }
        question.trueAnswers.forEach {
            answers[it].isTrue = true
        }
        return answers
    }

    private fun initializeViews() {
        context?.let { context ->
            val answers = initializeAnswers()
            fab.setOnClickListener { didTapNewAnswer(context) }
            setupAutoHideFabOnScrollList()
            adapter = AnswersAdapter(context, answers, this) { position ->
                didTapAnswer(context, position)
            }
            rv_answers.adapter = adapter
            rv_answers.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            val callback = SimpleItemTouchHelperCallback(adapter)
            mItemTouchHelper = ItemTouchHelper(callback)
            mItemTouchHelper?.attachToRecyclerView(rv_answers)
        }
    }

    private fun setupAutoHideFabOnScrollList() {
        rv_answers.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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

    private fun didTapNewAnswer(context: Context) {
        val dialog = Dialog(context)
        with(dialog) {
            setContentView(R.layout.new_answer_dialog)
            button_cancel.setOnClickListener { dialog.dismiss() }
            button_add.setOnClickListener {
                val description = et_answer.text.toString()
                if (!description.isEmpty()) {
                    val position = adapter.answers.size
                    val isTrue = toggle_switch.checkedTogglePosition != 0
                    val answer = Answer(position, description, isTrue)
                    adapter.answers.add(answer)
                    adapter.notifyItemInserted(position)
                }
                dialog.dismiss()
            }
            show()
        }
    }

    private fun didTapAnswer(context: Context, position: Int) {
        val answer = adapter.answers[position]
        val posToggle = if (answer.isTrue) 1 else 0
        val dialog = Dialog(context)
        with(dialog) {
            setContentView(R.layout.new_answer_dialog)
            button_cancel.setOnClickListener { dialog.dismiss() }
            button_add.setText(R.string.save)
            button_add.setOnClickListener {
                val description = et_answer.text.toString()
                if (!description.isEmpty()) {
                    val isTrue = toggle_switch.checkedTogglePosition != 0
                    adapter.answers[position].description = description
                    adapter.answers[position].isTrue = isTrue
                    adapter.notifyDataSetChanged()
                }
                dialog.dismiss()
            }
            dialog.et_answer.setText(answer.description)
            dialog.toggle_switch.checkedTogglePosition = posToggle
            show()
        }
    }

    private fun didTapConfirm() {
        view?.let { view ->
            if (adapter.answers.size > 1)
                QuizDao().saveAnswers(quiz, question, adapter.answers) { error ->
                    error?.let {
                        Snackbar.make(view, it, Snackbar.LENGTH_LONG).show()
                    } ?: run {
                        Snackbar.make(view, R.string.answers_save_successful, Snackbar.LENGTH_LONG).show()
                        findNavController().popBackStack()
                    }
                }
            else
                Alert(view.context, getString(R.string.error_empty_answers)).show()
        }
    }

}
