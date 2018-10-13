package com.qmakercorp.qmaker.ui.main.fragments.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.qmakercorp.qmaker.R
import com.qmakercorp.qmaker.adapters.QuizzesListAdapter
import com.qmakercorp.qmaker.data.dao.QuizzesDao
import kotlinx.android.synthetic.main.fragment_quizzes.*


class QuizzesFragment : Fragment() {

    private lateinit var quizzesAdapter: QuizzesListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_quizzes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()
    }

    override fun onResume() {
        super.onResume()
        startLoading()
        QuizzesDao().getQuizzes {
            stopLoading()
            if (it.size > 0) {
                quizzesAdapter.quizzes = it
                quizzesAdapter.notifyDataSetChanged()
            } else
                showInfo(R.string.empty_list_quizzes)
        }
    }

    private fun showInfo(resIdMessage: Int) {
        progress_bar.visibility = View.GONE
        rv_quizzes.visibility = View.GONE
        tv_info.setText(resIdMessage)
        tv_info.visibility = View.VISIBLE
    }

    private fun startLoading() {
        progress_bar.visibility = View.VISIBLE
        rv_quizzes.visibility = View.GONE
        tv_info.visibility = View.GONE
    }

    private fun stopLoading() {
        progress_bar.visibility = View.GONE
        rv_quizzes.visibility = View.VISIBLE
    }

    private fun initializeViews() {
        (activity as AppCompatActivity).supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(false)
            it.title = getString(R.string.my_quizzes)
        }
        fab.setOnClickListener { didTapNewQuiz(it) }
        initializeRecyclerView()
    }

    private fun didTapNewQuiz(view: View) {
        view.findNavController().navigate(R.id.action_tab2_to_newQuizFragment)
    }

    private fun initializeRecyclerView() {
        context?.let {
            rv_quizzes.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
            quizzesAdapter = QuizzesListAdapter(it, mutableListOf())
            rv_quizzes.adapter = quizzesAdapter
            rv_quizzes.layoutManager = LinearLayoutManager(it, RecyclerView.VERTICAL, false)
        }
    }

}
